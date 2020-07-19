package Models;

import Repository.SaveCustomer;
import Repository.SaveManager;
import Repository.SaveSeller;
import Repository.SaveUser;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

public abstract class User {
    protected int userId;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String phoneNumber;
    protected String password;
    private Status status;
    private ImageView profilePicture;
    private String profilePictureUrl;

    public User(SaveCustomer saveCustomer){
        this.userId = saveCustomer.getUserId();
        this.username = saveCustomer.getUsername();
        this.firstname = saveCustomer.getFirstname();
        this.lastname = saveCustomer.getLastname();
        this.email = saveCustomer.getEmail();
        this.phoneNumber = saveCustomer.getPhoneNumber();
        this.password = saveCustomer.getPassword();
        this.status = saveCustomer.getStatus();
        this.profilePictureUrl = saveCustomer.getProfilePictureURL();

    }

    public User(SaveSeller saveSeller){
        this.userId = saveSeller.getUserId();
        this.username = saveSeller.getUsername();
        this.firstname = saveSeller.getFirstname();
        this.lastname = saveSeller.getLastname();
        this.email = saveSeller.getEmail();
        this.phoneNumber = saveSeller.getPhoneNumber();
        this.password = saveSeller.getPassword();
        this.status = saveSeller.getStatus();
        this.profilePictureUrl = saveSeller.getProfilePictureURL();

    }

    public User(SaveManager saveManager){
        this.userId = saveManager.getUserId();
        this.username = saveManager.getUsername();
        this.firstname = saveManager.getFirstname();
        this.lastname = saveManager.getLastname();
        this.email = saveManager.getEmail();
        this.phoneNumber = saveManager.getPhoneNumber();
        this.password = saveManager.getPassword();
        this.status = saveManager.getStatus();
        this.profilePictureUrl = saveManager.getProfilePictureURL();

    }

    public static User generateUser(SaveUser saveUser){
        if (saveUser.getSaveCustomer() != null){
            return new Customer(saveUser.getSaveCustomer());
        }

        if (saveUser.getSaveManager() != null){
            return new Manager(saveUser.getSaveManager());
        }

        if (saveUser.getSaveSeller() != null){
            return new Seller(saveUser.getSaveSeller());
        }

        return null;
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

    public ImageView getProfilePicture() {
        return profilePicture;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
