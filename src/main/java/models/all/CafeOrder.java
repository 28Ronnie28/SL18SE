package models.all;

import java.io.Serializable;
import java.util.List;

public class CafeOrder implements Serializable {

    private int cafeOrderID;
    private String slNumber;
    private int paid;
    private int recieved;
    private List<CafeOrderDetails> items;

    public CafeOrder(int cafeOrderID, String slNumber, int paid, int recieved, List<CafeOrderDetails> items) {
        this.cafeOrderID = cafeOrderID;
        this.slNumber = slNumber;
        this.paid = paid;
        this.recieved = recieved;
        this.items = items;
    }

    public int getCafeOrderID() {
        return cafeOrderID;
    }

    public String getSlNumber() {
        return slNumber;
    }

    public int getPaid() {
        return paid;
    }

    public int getRecieved() {
        return recieved;
    }

    public List<CafeOrderDetails> getItems() {
        return items;
    }
}
