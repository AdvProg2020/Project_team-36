package Models;

import java.util.Date;

public class Message {
    private String text;
    private String senderUsername;
    private long time;

    public Message(String text, String username) {
        this.text = text;
        this.senderUsername = username;
        this.time = new Date().getTime();
    }

    public String getText() {
        return text;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public long getTime() {
        return time;
    }
}
