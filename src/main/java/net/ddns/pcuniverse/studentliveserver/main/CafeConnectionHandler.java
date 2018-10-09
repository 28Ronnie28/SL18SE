package net.ddns.pcuniverse.studentliveserver.main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.all.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class CafeConnectionHandler extends ConnectionHandler implements Runnable {

    private String username;
    private ObservableList<Notice> notices = FXCollections.observableArrayList();
    private ObservableList<ImportantDate> importantDates = FXCollections.observableArrayList();
    private ObservableList<CafeOrder> orders = FXCollections.observableArrayList();
    public volatile BooleanProperty updateNotices = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateImportantDates = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateOrders = new SimpleBooleanProperty(false);
    public volatile ObservableList<Object> outputQueue = FXCollections.observableArrayList();

    public CafeConnectionHandler(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, String username, ObservableList<ConnectionHandler> connectionsList, DatabaseHandler dh) {
        super(socket, objectInputStream, objectOutputStream, connectionsList, dh);
        this.username = username;
    }

    public void run() {
        updateNotices.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateNotices();
                updateNotices.set(false);
            }
        });
        updateImportantDates.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateImportantDates();
                updateImportantDates.set(false);
            }
        });
        updateOrders.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateOrders();
                updateOrders.set(false);
            }
        });
        updateImportantDates();
        updateNotices();
        updateOrders();
        new InputProcessor().start();
        new OutputProcessor().start();
    }

    private class InputProcessor extends Thread {
        public void run() {
            while (running.get()) {
                Object input;
                if ((input = getReply()) != null) {
                    if (input instanceof String) {
                        String text = input.toString();
                        if (text.startsWith("idp:")) {
                            isDefaultPassword();
                        } else if (text.startsWith("cco:")){
                            dh.refundCafeOrder(Integer.parseInt(text.substring(4).split(":")[0]));
                        }
                    } else if (input instanceof CafeMenuItem) {
                        dh.updateCafeMenuItem((CafeMenuItem) input);
                    } else if (input instanceof CafeOrder) {
                        //TODO Notify student and process when finished
                        dh.updateCafeOrder((CafeOrder) input);
                    }
                }
            }
        }
    }

    private class OutputProcessor extends Thread {
        public void run() {
            while (running.get()) {
                try {
                    if (!outputQueue.isEmpty()) {
                        Object out = outputQueue.get(0);
                        sendData(out);
                        dh.log("Admin " + username + "> OutputProcessor> Sent: " + outputQueue.get(0));
                        outputQueue.remove(out);
                    }
                    Thread.sleep(20);
                } catch (Exception ex) {
                    dh.log("Server> OutputProcessor> " + ex);
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendData(Object data) {
        try {
            synchronized (objectOutputStream) {
                objectOutputStream.writeObject(data);
                objectOutputStream.flush();
                objectOutputStream.reset();
            }
        } catch (Exception ex) {
            terminateConnection();
            dh.log("Server> sendData> " + ex);
            ex.printStackTrace();
        }
    }

    private void isDefaultPassword() {
        if (dh.isDefaultAdminPassword(username)) {
            outputQueue.add(0, "idp:y");
        } else {
            outputQueue.add(0, "idp:n");
        }
    }

    private void changeDefaultPassword(String newPassword) {
        if (dh.changeAdminDefaultPassword(username, newPassword)) {
            outputQueue.add(0, "cdp:y");
        } else {
            outputQueue.add(0, "cdp:n");
        }
    }

    private void updateNotices() {
        notices.setAll(dh.getAllNotices());
        outputQueue.add(Arrays.asList(notices.toArray()));
    }

    private void updateImportantDates() {
        importantDates.setAll(dh.getImportantDates());
        outputQueue.add(Arrays.asList(importantDates.toArray()));
    }

    private void updateOrders() {
        orders.setAll(dh.getAllCafeOrders());
        outputQueue.add(Arrays.asList(importantDates.toArray()));
    }

}
