package models.all;

import java.io.Serializable;

public class CafeOrderDetails implements Serializable {

    private int cafeMenuItemID;
    private int quantity;

    public CafeOrderDetails(int cafeMenuItemID, int quantity) {
        this.cafeMenuItemID = cafeMenuItemID;
        this.quantity = quantity;
    }

    public int getCafeMenuItemID() {
        return cafeMenuItemID;
    }

    public int getQuantity() {
        return quantity;
    }
}
