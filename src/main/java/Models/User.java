package Models;

import java.util.ArrayList;
import static Models.Status.*;

public abstract class User implements Packable{
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static ArrayList<String> allUsernames = new ArrayList<>();
    protected int userId;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String phoneNumber;
    protected String password;
    private static int totalUsersMade;
    private Status status;

    public User(String username){
        this.username = username;
        this.userId = totalUsersMade+1;
        this.status = AVAILABLE;
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

    public int getUserId() {
        return userId;
    }

    public static User getUserByUsername(String username){
        for (User user : allUsers) {
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void addUsername(String username){
        allUsernames.add(username);
    }

    public static void removeUsername(String username){
        allUsernames.remove(username);
    }

    public static boolean isThereUsername(String newUsername){
        updateAllUsers();
        for (String username : allUsernames) {
            if(newUsername.equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

    private static void updateAllUsers(){
        ArrayList<User> usersToDelete = new ArrayList<>();
        for (User user : allUsers) {
            if(user.getStatus().equals(DELETED)){
                usersToDelete.add(user);
            }
        }
        allUsers.removeAll(usersToDelete);
    }

    public void setUserDeleted(){
        status = DELETED;
    }

    public static void addNewUser(User user){
        allUsers.add(user);
    }

    public abstract String getType();

    public Status getStatus() {
        return status;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }

    //-..-
}
