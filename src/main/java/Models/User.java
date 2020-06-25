package Models;


import GUI.ManageUsersController;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
    private ImageView profilePicture;
    private String profilePictureUrl;
    private static ManageUsersController manageUsersController;
    private static User userToView;

    public User(String username){
        totalUsersMade++;
        this.username = username;
        this.userId = totalUsersMade;
        this.status = AVAILABLE;
    }

    public static void addTest(){
        Customer customer = new Customer("sahar");
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
        Customer.getAllCustomers().add(customer);
        allUsernames.add("sayeh");
        allUsernames.add("sahar");
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
                String email, String phoneNumber, String password, Status status) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = status;
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
        allUsers.removeAll(temp);
    }

    public ImageView getProfilePicture() {
        return profilePicture;
    }

    public Hyperlink getViewHyperlink(){
        Hyperlink view = new Hyperlink();
        view.setText("view");
        view.setStyle("");
        view.setOnAction(e->{
            userToView = this;
            try {
                manageUsersController.viewUserAction();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        return view;
    }

    public ImageView getProfilePicture(int height,int width) throws MalformedURLException {
        File file = new File (this.profilePictureUrl);
        String path = file.toURI().toURL().toString();
        Image image = new Image(path,width,height,false,false);
        return new ImageView(image);
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public ImageView getSmallProfilePicture() throws MalformedURLException {
        File file = new File (this.profilePictureUrl);
        String path = file.toURI().toURL().toString();
        return new ImageView(new Image(path,50,50,false,false));
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public static void setManageUsersController(ManageUsersController manageUsersController) {
        User.manageUsersController = manageUsersController;
    }

    public static User getUserToView() {
        return userToView;
    }
}
