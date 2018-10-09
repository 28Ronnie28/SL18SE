package models.all;

import java.io.Serializable;
import java.util.List;

public class ChatRoom implements Serializable {

    private int chatRoomID;
    private String chatRoomName;
    private String startedDate;
    private List<ChatRoomMessage> messages;

    public ChatRoom(int chatRoomID, String chatRoomName, String startedDate, List<ChatRoomMessage> messages) {
        this.chatRoomID = chatRoomID;
        this.chatRoomName = chatRoomName;
        this.startedDate = startedDate;
        this.messages = messages;
    }

    public int getChatRoomID() {
        return chatRoomID;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public List<ChatRoomMessage> getMessages() {
        return messages;
    }
}
