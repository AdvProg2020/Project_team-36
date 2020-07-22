package Repository;

import Models.Chat;
import Models.Message;

import java.util.ArrayList;
import java.util.List;

public class SaveChat {
    private int supporterId;
    private List<Integer> usersInChat;
    private List<Message> messagesInChat;
    private int id;

    //todo nazanin SaveChat

    public SaveChat(Chat chat) {
        usersInChat = new ArrayList<>();
        messagesInChat = new ArrayList<>();
        this.id = chat.getId();
        this.supporterId = chat.getSupporter().getUserId();
        messagesInChat.addAll(chat.getMessagesInChat());
        chat.getUsersInChat().forEach(user -> usersInChat.add(user.getUserId()));
    }

    public int getSupporterId() {
        return supporterId;
    }

    public List<Integer> getUsersInChat() {
        return usersInChat;
    }

    public List<Message> getMessagesInChat() {
        return messagesInChat;
    }

    public int getId() {
        return id;
    }
}
