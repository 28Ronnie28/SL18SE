package models.all;

import java.io.Serializable;

public class InfoMessages implements Serializable {

    private String from;
    private String title;
    private String message;

    public InfoMessages(String from, String title, String message) {
        this.from = from;
        this.title = title;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
