package models.all;

import java.io.Serializable;

public class CafeMenuItem implements Serializable {

    private int cafeMenuItemID;
    private String category;
    private String heading;
    private String description;
    private int price;

    public CafeMenuItem(int cafeMenuItemID, String category, String heading, String description, int price) {
        this.cafeMenuItemID = cafeMenuItemID;
        this.category = category;
        this.heading = heading;
        this.description = description;
        this.price = price;
    }

    public int getCafeMenuItemID() {
        return cafeMenuItemID;
    }

    public String getCategory() {
        return category;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
