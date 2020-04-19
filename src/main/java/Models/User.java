package Models;

import java.util.ArrayList;

public abstract class User implements Packable{
    private static ArrayList<User> allUsers;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String phoneNumber;
    protected String password;

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

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }
}
