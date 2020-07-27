package Models;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static Models.Status.*;

public abstract class User{
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static ArrayList<String> allUsernames = new ArrayList<>();
    protected int userId;
    protected String username;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String phoneNumber;
    protected String password;
    static Random random = new Random();
    private static int totalUsersMade = random.nextInt(4988 - 1000) + 1000;
    private Status status;
    private String profilePictureUrl;
    private static User userToView;
    private ArrayList<Chat> chats;

    public User(String username){
        totalUsersMade++;
        this.username = username;
        this.userId = totalUsersMade;
        this.status = AVAILABLE;
        chats = new ArrayList<>();
    }



    public static void addTest(){
        Seller customer = new Seller("sahar");
        Manager customer1 = new Manager("sayeh");
        customer.setEmail("dsbh@c.co");
        customer.setPassword("sahar");
        customer.setFirstname("jk");
        customer.setLastname("hidf");
        customer.setProfilePictureUrl("D:\\myprj\\project\\AP_Project\\src\\main\\resources\\images\\buyer.png");
        customer1.setEmail("dsbh@c.co");
        customer1.setPassword("sahar");
        customer1.setFirstname("jk");
        customer1.setLastname("hidf");
        customer1.setProfilePictureUrl("D:\\myprj\\project\\AP_Project\\src\\main\\resources\\images\\seller.png");
        allUsers.add(customer);
        allUsers.add(customer1);
        Seller.getAllSellers().add(customer);
        allUsernames.add("sayeh");
        allUsernames.add("sahar");

        Customer customer2 = new Customer("karaneh");
        Customer customer3 = new Customer("nazanin");
        allUsers.add(customer2);
        allUsers.add(customer3);
        Customer.getAllCustomers().add(customer2);
        Customer.getAllCustomers().add(customer3);
        allUsernames.add("karaneh");
        allUsernames.add("nazanin");
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChat(Chat chat) {
        chats.add(chat);
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
        updateAllUsers();
        return allUsers;
    }

    public void setImageURL(String path){
        this.profilePictureUrl = path;
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
            if(newUsername.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
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

    public User(int userId, String username, String firstname, String lastname,
                String email, String phoneNumber, String password, Status status,String profilePictureURL) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = status;
        this.profilePictureUrl = profilePictureURL;
        this.chats = new ArrayList<>();
    }

    public static void addToAllUsers(User user){
        allUsers.add(user);
    }

    public static User getUserById(int id){
        for (User user : allUsers) {
            if (user.userId == id){
                return user;
            }
        }
        return null;
    }
  
    public static void updateAllUsers(){
        ArrayList<User> temp = new ArrayList<>();
        for (User user : allUsers) {
            if(user.getStatus().equals(DELETED)) {
                temp.add(user);
            }
        }
        Customer.updateAllCustomers();
        Seller.updateSellers();
        Manager.updateManagers();
        Supporter.updateAllSupporters();
        allUsers.removeAll(temp);
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public static void setTotalUsersMade(int totalUsersMade) {
        User.totalUsersMade = totalUsersMade;
    }

    public static int getTotalUsersMade() {
        return totalUsersMade;
    }

    public static User getUserToView() {
        return userToView;
    }

    public static void addUsernameToList(String username){
        allUsernames.add(username);
    }

    public static void setUserToView(User userToView) {
        User.userToView = userToView;
    }
}
