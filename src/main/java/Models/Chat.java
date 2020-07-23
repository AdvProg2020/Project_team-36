package Models;

import java.util.ArrayList;

public class Chat {
    private Supporter supporter;
    private ArrayList<User> usersInChat;
    private ArrayList<Message> messagesInChat;
    private int id;
    private static int allChatsMade = 0;
    private static ArrayList<Chat> allChats = new ArrayList<>();

    public Chat(Supporter supporter, ArrayList<User> usersInChat) {
        this.supporter = supporter;
        messagesInChat = new ArrayList<>();
        this.usersInChat = new ArrayList<>(usersInChat);
        this.id = (allChatsMade+=1);
        supporter.setChat(this);
        usersInChat.forEach(user -> user.setChat(this));
    }


    public void addNewMessage(Message message){
        messagesInChat.add(message);
    }

    public void addNewUserToChat(User user){
        usersInChat.add(user);
    }

    public Supporter getSupporter() {
        return supporter;
    }

    public ArrayList<User> getUsersInChat() {
        return usersInChat;
    }

    public ArrayList<Message> getMessagesInChat() {
        return messagesInChat;
    }

    public int getId() {
        return id;
    }

    public static ArrayList<Chat> getAllChats() {
        return allChats;
    }

    public static Chat getChatById(int id){
        for (Chat chat : allChats) {
            if(chat.getId()==id){
                return chat;
            }
        }
        return null;
    }

}
