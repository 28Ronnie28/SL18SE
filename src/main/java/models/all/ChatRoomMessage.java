package models.all;

import java.io.Serializable;

public class ChatRoomMessage implements Serializable {

    private int chatRoomID;
    private String message;
    private String messageDate;

    public ChatRoomMessage(int chatRoomID, String message, String messageDate) {
        this.chatRoomID = chatRoomID;
        this.message = message;
        this.messageDate = messageDate;
    }

    public int getChatRoomID() {
        return chatRoomID;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageDate() {
        return messageDate;
    }
}
