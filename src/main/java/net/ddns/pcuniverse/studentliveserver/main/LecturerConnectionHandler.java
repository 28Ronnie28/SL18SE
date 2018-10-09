package net.ddns.pcuniverse.studentliveserver.main;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.all.*;
import models.lecturer.AttendanceRecord;
import models.lecturer.LecturerStudentAttendance;
import models.lecturer.LecturerStudentResult;
import models.lecturer.UploadFile;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LecturerConnectionHandler extends ConnectionHandler implements Runnable {

    private String lecturerNumber;
    private ObjectProperty<Lecturer> lecturer = new SimpleObjectProperty<>();
    private ObservableList<Notice> notices = FXCollections.observableArrayList();
    private ObservableList<ContactDetails> contactDetails = FXCollections.observableArrayList();
    private ObservableList<ImportantDate> importantDates = FXCollections.observableArrayList();
    private ObservableList<LecturerStudentAttendance> studentAttendance = FXCollections.observableArrayList();
    private ObservableList<LecturerStudentResult> studentResults = FXCollections.observableArrayList();
    private ObservableList<ChatRoom> chatRooms = FXCollections.observableArrayList();
    private ObservableList<CafeOrder> orders = FXCollections.observableArrayList();
    private ObservableList<InfoMessages> messages = FXCollections.observableArrayList();
    public volatile ObservableList<Object> outputQueue = FXCollections.observableArrayList();
    public volatile BooleanProperty updateLecturer = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateNotices = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateContactDetails = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateImportantDates = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateStudentAttendance = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateStudentResults = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateChatRooms = new SimpleBooleanProperty(false);
    public volatile BooleanProperty updateOrders = new SimpleBooleanProperty(false);

    public LecturerConnectionHandler(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, String lecturerNumber, ObservableList<ConnectionHandler> connectionsList, DatabaseHandler dh) {
        super(socket, objectInputStream, objectOutputStream, connectionsList, dh);
        this.lecturerNumber = lecturerNumber;
    }

    public void run() {
        updateLecturer.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateLecturer();
                updateLecturer.set(false);
            }
        });
        updateNotices.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateNotices();
                updateNotices.set(false);
            }
        });
        updateContactDetails.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateContactDetails();
                updateContactDetails.set(false);
            }
        });
        updateImportantDates.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateImportantDates();
                updateImportantDates.set(false);
            }
        });
        updateStudentAttendance.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateStudentAttendance();
                updateStudentAttendance.set(false);
            }
        });
        updateStudentResults.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateStudentResults();
                updateStudentResults.set(false);
            }
        });
        updateChatRooms.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateChatRooms();
                updateChatRooms.set(false);
            }
        });
        updateOrders.addListener((obs, oldV, newV) -> {
            if (newV) {
                updateOrders();
                updateOrders.set(false);
            }
        });
        lecturer.addListener(e -> {
            outputQueue.add(0, lecturer.get());
        });
        notices.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(notices.toArray()));
        });
        contactDetails.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(contactDetails.toArray()));
        });
        importantDates.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(importantDates.toArray()));
        });
        studentAttendance.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(studentAttendance.toArray()));
        });
        studentResults.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(studentResults.toArray()));
        });
        chatRooms.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(chatRooms.toArray()));
        });
        orders.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(orders.toArray()));
        });
        messages.addListener((InvalidationListener) e -> {
            outputQueue.add(0, Arrays.asList(messages.toArray()));
        });
        updateLecturer();
        updateNotices();
        updateContactDetails();
        updateImportantDates();
        updateStudentAttendance();
        updateStudentResults();
        updateChatRooms();
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
                        if (text.startsWith("cp:")) {
                            changePassword(text.substring(3).split(":")[0], text.substring(3).split(":")[1]);
                        } else if (text.startsWith("gf:")) {
                            getFile(text.substring(3).split(":")[0], text.substring(3).split(":")[1]);
                        } else if (text.startsWith("ha:")) {
                            if (dh.hasAttendance(Integer.parseInt(text.substring(3)))) {
                                sendData("ha:y");
                            } else {
                                sendData("ha:n");
                            }
                        } else if (text.startsWith("idp:")) {
                            isDefaultPassword();
                        } else if (text.startsWith("cdp:")) {
                            dh.log("Lecturer " + lecturerNumber + "> Requested Change Default Password");
                            changeDefaultPassword(text.substring(4));
                        } else if (text.startsWith("df:")) {
                            dh.log("Lecturer " + lecturerNumber + "> Delete file");
                            dh.deleteFile(Integer.parseInt(text.split(":")[1]), text.split(":")[2]);
                        } else {
                            System.out.println("Unknown command: " + input);
                        }
                    } else if (input instanceof UploadFile) {
                        try {
                            UploadFile uploadFile = (UploadFile) input;
                            File newFile = new File(Server.FILES_FOLDER.getAbsolutePath() + "/" + uploadFile.getClassID() + "/" + uploadFile.getFileName());
                            newFile.getParentFile().mkdirs();
                            Files.write(newFile.toPath(), uploadFile.getFileData());
                            dh.notifyUpdatedFiles(uploadFile.getClassID());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (input instanceof AttendanceRecord) {
                        dh.addAttendance((AttendanceRecord) input);
                    } else if (input instanceof ChatRoom){
                        dh.updateChatroom((ChatRoom) input);
                    } else if (input instanceof ChatRoomMessage){
                        dh.addMessageToChatRoom((ChatRoomMessage) input);
                    } else if (input instanceof CafeOrder){
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
                        outputQueue.remove(out);
                    }
                    Thread.sleep(20);
                } catch (Exception ex) {
                    System.out.println("Server> OutputProcessor> " + ex);
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
            System.out.println("Server> sendData> " + ex);
        }
    }

    public void addMessage(String message, String studentName) {
        outputQueue.add(0, "sm:" + message + ":" + studentName);
    }

    private void isDefaultPassword() {
        if (dh.isDefaultLecturerPassword(lecturerNumber)) {
            outputQueue.add(0, "idp:y");
        } else {
            outputQueue.add(0, "idp:n");
        }
    }

    private void changeDefaultPassword(String newPassword) {
        if (dh.changeLecturerDefaultPassword(lecturerNumber, newPassword)) {
            outputQueue.add(0, "cdp:y");
        } else {
            outputQueue.add(0, "cdp:n");
        }
    }

    private void changePassword(String prevPassword, String newPassword) {
        String sPassword = dh.getLecturerPassword(lecturerNumber);
        if (prevPassword.matches(sPassword)) {
            dh.changeLecturerPassword(lecturerNumber, newPassword);
            outputQueue.add(0, "cp:y");
        } else {
            outputQueue.add(0, "cp:n");
        }
    }

    public String getLecturerNumber() {
        return lecturerNumber;
    }

    public Lecturer getLecturer() {
        return lecturer.getValue();
    }

    private void getFile(String classID, String fileName) {
        File file = new File(Server.FILES_FOLDER.getAbsolutePath() + "/" + classID + "/" + fileName);
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            int size = 0;
            while (size < fileBytes.length) {
                System.out.println(Math.min(Server.BUFFER_SIZE, fileBytes.length - size));
                outputQueue.add(new FilePart(Arrays.copyOfRange(fileBytes, size, size + Math.min(Server.BUFFER_SIZE, fileBytes.length - size)), Integer.parseInt(classID), fileName));
                size += Math.min(Server.BUFFER_SIZE, fileBytes.length - size);
                System.out.println("Total size: " + size);
                dh.log("");
            }
        } catch (Exception ex) {
            System.out.println("Server> getFile> " + ex);
        }
    }

    private Boolean updateStudents(String classID) {
        List<String> students = dh.getStudentsInClass(Integer.parseInt(classID));
        for (ConnectionHandler ch : connectionsList) {
            if (ch instanceof StudentConnectionHandler) {
                for (int i = 0; i < students.size(); i++) {
                    if (((StudentConnectionHandler) ch).getStudentNumber().matches(students.get(i))) {
                        ((StudentConnectionHandler) ch).updateStudent.setValue(true);
                        i = students.size();
                    }
                }
            }
        }
        return true;
    }

    private void updateLecturer() {
        lecturer.setValue(dh.getLecturer(lecturerNumber));
    }

    private void updateNotices() {
        notices.clear();
        notices.addAll(dh.getNotices(lecturerNumber, "Lecturer"));
    }

    private void updateContactDetails() {
        contactDetails.clear();
        List<ContactDetails> out = new ArrayList<>();
        out.addAll(dh.getContactDetails());
        out.addAll(dh.getStudentContactDetails(lecturerNumber));
        contactDetails.addAll(out);
    }

    private void updateImportantDates() {
        importantDates.clear();
        importantDates.addAll(dh.getImportantDates());
    }

    private void updateStudentAttendance() {
        studentAttendance.clear();
        studentAttendance.addAll(dh.getAllStudentsInClassAttendance(lecturerNumber));
    }

    private void updateStudentResults() {
        studentResults.clear();
        studentResults.addAll(dh.getAllStudentsInClassResults(lecturerNumber));
    }

    private void updateChatRooms() {
        chatRooms.clear();
        chatRooms.addAll(dh.getAllChatRooms(lecturerNumber));
    }

    private void updateOrders() {
        orders.clear();
        orders.addAll(dh.getAllSLOrders(lecturerNumber));
    }


}
