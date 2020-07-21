package Repository;

import Models.Manager;
import Models.Status;
import Models.Supporter;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.image.ImageView;

public class SaveSupporter {
    protected int userId;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String phoneNumber;
    protected String password;
    private Status status;
    private String profilePictureUrl;
    private static int lastId = 0;

    public SaveSupporter(Supporter supporter){
        this.profilePictureUrl = supporter.getProfilePictureUrl();
        this.userId = supporter.getUserId();
        this.username = supporter.getUsername();
        this.firstname = supporter.getFirstname();
        this.lastname = supporter.getLastname();
        this.email = supporter.getEmail();
        this.phoneNumber = supporter.getPhoneNumber();
        this.password = supporter.getPassword();
        this.status = supporter.getStatus();
    }

    public static void save(Supporter supporter){
        SaveSupporter saveSupporter = new SaveSupporter(supporter);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveSupporterGson = gson.toJson(saveSupporter);
        FileUtil.write(FileUtil.generateAddress(Manager.class.getName(),saveSupporter.userId),saveSupporterGson);
    }

    public static Supporter load(int id){
        lastId = Math.max(lastId,id);
        Supporter potentialSupporter = Supporter.getSupporterById(id);
        if(potentialSupporter != null){
            return potentialSupporter;
        }

        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Supporter.class.getName(),id));
        if (data == null){
            return null;
        }
        SaveSupporter saveSupporter = gson.fromJson(data,SaveSupporter.class);
        Supporter supporter = new Supporter(saveSupporter.userId,saveSupporter.username,saveSupporter.firstname,
                saveSupporter.lastname,saveSupporter.email,saveSupporter.phoneNumber,
                saveSupporter.password,saveSupporter.status,saveSupporter.profilePictureUrl);
        Supporter.addToAllSupporter(supporter);
        User.addToAllUsers(supporter);
        return supporter;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() {
        return status;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public static int getLastId() {
        return lastId;
    }
}
