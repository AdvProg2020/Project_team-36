package Models;

import GUI.Constants;
import Network.Client;
import Repository.SaveCategory;
import Repository.SaveChat;
import Repository.SaveSupporter;
import Repository.SaveUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private SaveChat saveChat;
    private ArrayList<Message> messagesInChat;
    private int id;

    public Chat(SaveChat saveChat) {
        messagesInChat = new ArrayList<>();
        this.saveChat = saveChat;
        messagesInChat.addAll(saveChat.getMessagesInChat());
        this.id = saveChat.getId();
    }

    public Supporter getSupporter() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Supporter");
        query.getMethodInputs().put("id", "" + saveChat.getId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Supporter")) {
            Gson gson = new Gson();
            SaveSupporter saveUser = gson.fromJson(response.getData(), SaveSupporter.class);
            return new Supporter(saveUser);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public ArrayList<User> getUsersInChat() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "User");
        this.saveChat.getUsersInChat().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Integer>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveUser>>(){}.getType();
            List<SaveUser> allSaveUsers = gson.fromJson(response.getData(),type);
            ArrayList<User> allUsers = new ArrayList<>();
            allSaveUsers.forEach(saveUser -> allUsers.add(User.generateUser(saveUser)));
            return allUsers;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public ArrayList<Message> getMessagesInChat() {
        return messagesInChat;
    }

    public int getId() {
        return id;
    }
}
