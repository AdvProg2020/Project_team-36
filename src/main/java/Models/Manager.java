package Models;

import java.util.ArrayList;

public class Manager extends User {

    private static ArrayList<Manager> allManagers = new ArrayList<>();


    public Manager(String username) {
        super(username);
    }

    @Override
    public String getType() {
        return "manager";
    }


    public static ArrayList<Manager> getAllManagers() {
        return allManagers;
    }

    public static boolean canManagerRegister(){
        return allManagers.isEmpty();
    }

    public static void addNewManager(Manager manager){
        allManagers.add(manager);
    }

//    public static boolean isMainManager(String username){
//        return mainManager.getUsername().equals(username);
//    }

    public static Manager getManagerById(int id){
        for (Manager manager : allManagers) {
            if(manager.userId == id){
                return manager;
            }
        }
        return null;
    }
    public Manager(int userId, String username, String firstname, String lastname, String email,
                   String phoneNumber, String password, Status status) {
        super(userId, username, firstname, lastname, email, phoneNumber, password, status);
    }

    public static void addToAllManager(Manager manager){
        allManagers.add(manager);
    }
    @Override
    public String toString() {
        return "Manager{" +
                "username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static void updateManagers(){
        ArrayList<Manager> toBeRemoved =new ArrayList<>();
        for (Manager manager : allManagers) {
            if(manager.getStatus().equals(Status.DELETED))
                toBeRemoved.add(manager);
        }
        allManagers.removeAll(toBeRemoved);
}