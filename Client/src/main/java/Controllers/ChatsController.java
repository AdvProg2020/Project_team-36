package Controllers;

import GUI.Constants;
import Models.*;
import Network.Client;
import Repository.SaveChat;
import Repository.SaveDiscount;
import com.google.gson.Gson;

import java.util.ArrayList;


public class ChatsController {
    private String controllerName = "ChatsController";

    public void sendNewMessage(String text, int chatId, String username){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sendNewMessage");
        query.getMethodInputs().put("text", text);
        query.getMethodInputs().put("chatId", Integer.toString(chatId));
        query.getMethodInputs().put("username", username);
        Client.process(query);
    }

    public Chat getChatById(int id){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getChatById");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        if (response.getReturnType().equals("Chat")) {
            Gson gson = new Gson();
            SaveChat saveChat = gson.fromJson(response.getData(), SaveChat.class);
            return new Chat(saveChat);
        } else {
            return null;
        }
    }

    public int createNewChatRoom(Supporter supporter, User user){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getChatById");
        query.getMethodInputs().put("user", Integer.toString(user.getUserId()));
        query.getMethodInputs().put("supporter", Integer.toString(supporter.getUserId()));
        Response response = Client.process(query);
        if (response.getReturnType().equals("int")) {
            return Integer.parseInt(response.getData());
        } else {
            return -1;
        }
    }


}
