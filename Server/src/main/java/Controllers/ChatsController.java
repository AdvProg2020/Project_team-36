package Controllers;

import Models.*;
import Repository.SaveChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class ChatsController {

    public void sendNewMessage(String text, int chatId, String username){
        Message newMessage = new Message(text, username);
        Chat.getChatById(chatId).addNewMessage(newMessage);
    }

    public Chat getChatById(int id){
        return Chat.getChatById(id);
    }

    public int createNewChatRoom(Supporter supporter, User user){
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(user);
        Chat chat = new Chat(supporter, userArrayList);
        return chat.getId();
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "sendNewMessage" -> processSendNewMessage(query);
            case "getChatById" -> processGetChatById(query);
            case "createNewChatRoom" -> processCreateNewChatRoom(query);
            default -> new Response("Error", "");
        };
    }

    private Response processSendNewMessage(Query query){
        int chatId = Integer.parseInt(query.getMethodInputs().get("chatId"));
        sendNewMessage(query.getMethodInputs().get("text"), chatId, query.getMethodInputs().get("username"));
        return new Response("void", "");
    }

    private Response processGetChatById(Query query){
        SaveChat saveChat = new SaveChat(getChatById(Integer.parseInt(query.getMethodInputs().get("id"))));
        Gson gson = new GsonBuilder().create();
        String saveChatGson = gson.toJson(saveChat);
        return new Response("Chat", saveChatGson);
    }

    private Response processCreateNewChatRoom(Query query){
        User user = User.getUserById(Integer.parseInt(query.getMethodInputs().get("user")));
        Supporter supporter = Supporter.getSupporterById(Integer.parseInt(query.getMethodInputs().get("supporter")));
        return new Response("int", Integer.toString(createNewChatRoom(supporter, user)));
    }


}
