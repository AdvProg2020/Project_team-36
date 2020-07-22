package Models;

import GUI.Constants;
import Network.Client;
import Repository.SaveChat;
import Repository.SaveSupporter;
import Repository.SaveUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Supporter extends User {
    private SaveSupporter saveSupporter;

    public Supporter(SaveSupporter saveSupporter){
        super(saveSupporter);
        this.saveSupporter = saveSupporter;
    }

    @Override
    public String getType() {
        return "supporter";
    }

    @Override
    public ArrayList<Chat> getChats() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Chat");
        this.saveSupporter.getChatsIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Chat>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveChat>>(){}.getType();
            List<SaveChat> allSaveChats = gson.fromJson(response.getData(),type);
            ArrayList<Chat> allChats = new ArrayList<>();
            allSaveChats.forEach(saveChat -> allChats.add(new Chat(saveChat)));
            return allChats;
        }else {
            System.out.println(response);
            return null;
        }
    }
}
