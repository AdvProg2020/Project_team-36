package Repository;

import Models.Status;
import Models.Supporter;
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

    public SaveSupporter(Supporter supporter){
        //TODO nazanin save supporter
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
}
