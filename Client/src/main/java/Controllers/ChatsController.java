package Controllers;

import GUI.Constants;
import Models.Chat;
import Models.Discount;
import Models.Query;
import Models.Response;
import Network.Client;
import Repository.SaveChat;
import Repository.SaveDiscount;
import com.google.gson.Gson;


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


}
