package Repository;

import Models.Manager;
import Models.Status;
import Models.User;
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
    private String profilePictureURL;
    private static int lastId = 0;

    private SaveManager() {
    }

    public SaveManager(Manager manager){
        this.profilePictureURL = manager.getProfilePictureUrl();
        this.userId = manager.getUserId();
        this.username = manager.getUsername();
        this.firstname = manager.getFirstname();
        this.lastname = manager.getLastname();
        this.email = manager.getEmail();
        this.phoneNumber = manager.getPhoneNumber();
        this.password = manager.getPassword();
        this.status = manager.getStatus();
    }

    public static void save(Manager manager){
        SaveManager saveManager = new SaveManager(manager);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveManagerGson = gson.toJson(saveManager);
        FileUtil.write(FileUtil.generateAddress(Manager.class.getName(),saveManager.userId),saveManagerGson);
    }

    public static Manager load(int id){
        lastId = Math.max(lastId,id);
        Manager potentialManager = Manager.getManagerById(id);
        if(potentialManager != null){
            return potentialManager;
        }

        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Manager.class.getName(),id));
        if (data == null){
            return null;
        }
        SaveManager saveManager = gson.fromJson(data,SaveManager.class);
        Manager manager = new Manager(saveManager.userId,saveManager.username,saveManager.firstname,
                saveManager.lastname,saveManager.email,saveManager.phoneNumber,
                saveManager.password,saveManager.status,saveManager.profilePictureURL);
        Manager.addToAllManager(manager);
        User.addToAllUsers(manager);
        return manager;
    }

    public static int getLastId() {
        return lastId;
    }
}
