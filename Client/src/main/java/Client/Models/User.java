package Client.Models;

import Client.GUI.Constants;
import Client.Network.Client;
import Models.Status;
import Repository.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public abstract class User {
    protected int userId;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String phoneNumber;
    protected String password;
    private Status status;
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

    public User(SaveSupporter saveSupporter){
        this.userId = saveSupporter.getUserId();
        this.username = saveSupporter.getUsername();
        this.firstname = saveSupporter.getFirstname();
        this.lastname = saveSupporter.getLastname();
        this.email = saveSupporter.getEmail();
        this.phoneNumber = saveSupporter.getPhoneNumber();
        this.password = saveSupporter.getPassword();
        this.status = saveSupporter.getStatus();
        this.profilePictureUrl = saveSupporter.getProfilePictureUrl();
    }

    public abstract String getType();

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

        if (saveUser.getSaveSupporter() != null){
            return new Supporter(saveUser.getSaveSupporter());
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

    public ImageView getProfilePicture(int height,int width) throws MalformedURLException {
        byte[] bytes =null;
        try {
            bytes = Client.readFile(profilePictureUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image img = new Image(new ByteArrayInputStream(bytes),width,height,false,false);
        return new ImageView(img);
    }

    public ImageView getSmallProfilePicture() throws MalformedURLException {
        return getProfilePicture(50,50);
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public abstract ArrayList<Chat> getChats();

    public String getOnline(){
        for (User user : Constants.managerController.getOnlineUsers()) {
            if(user.equals(this))
                return "online";
        }
        return "offline";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(username, user.username) &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(password, user.password) &&
                status == user.status &&
                Objects.equals(profilePictureUrl, user.profilePictureUrl);
    }

}
