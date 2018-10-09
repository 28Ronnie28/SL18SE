package models.all;

import java.io.Serializable;

public class CafeUser implements Serializable {

    private String username;
    private String email;

    public CafeUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
