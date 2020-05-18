package Repository;

import Models.Manager;
import Models.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveManager {
    private int userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String password;
    private Status status;
    private static int lastId = 0;

    private SaveManager() {
    }

    public static void save(Manager manager){
        SaveManager saveManager = new SaveManager();
        saveManager.userId = manager.getUserId();
        saveManager.username = manager.getUsername();
        saveManager.firstname = manager.getFirstname();
        saveManager.lastname = manager.getLastname();
        saveManager.email = manager.getEmail();
        saveManager.phoneNumber = manager.getPhoneNumber();
        saveManager.password = manager.getPassword();
        saveManager.status = manager.getStatus();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveManagerGson = gson.toJson(saveManager);
        FileUtil.write(FileUtil.generateAddress(Manager.class.getName(),saveManager.userId),saveManagerGson);
    }

    public static Manager load(int id){
        lastId = Math.max(lastId,id);
        if(Manager.getManagerById(id) != null){
            return Manager.getManagerById(id);
        }

        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Manager.class.getName(),id));
        if (data == null){
            return null;
        }
        SaveManager saveManager = gson.fromJson(data,SaveManager.class);
        Manager manager = new Manager(saveManager.userId,saveManager.username,saveManager.firstname,
                saveManager.lastname,saveManager.email,saveManager.phoneNumber,saveManager.password,saveManager.status);
        Manager.addToAllManager(manager);
        return manager;
    }
}
