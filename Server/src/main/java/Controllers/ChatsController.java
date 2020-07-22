package Controllers;

import Models.Chat;
import Models.Message;
import Models.Query;
import Models.Response;
import Repository.SaveChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatsController {

    public void sendNewMessage(String text, int chatId, String username){
        Message newMessage = new Message(text, username);
        Chat.getChatById(chatId).addNewMessage(newMessage);
    }

    public Chat getChatById(int id){
        return Chat.getChatById(id);
    }

    public Response processQuery(Query query) {
        switch (query.getMethodName()) {
            case "sendNewMessage":
                return processSendNewMessage(query);
            case "getChatById":
                return processGetChatById(query);
            default:
                return new Response("Error", "");
        }
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

}
