package Models;

import java.util.ArrayList;

public class Manager extends User  {

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

    public static boolean canManagerRegister() {
        return allManagers.isEmpty();
    }

    public static void addNewManager(Manager manager) {
        allManagers.add(manager);
    }

    @Override
    public String toString() {
        return "username: " + username + "\nfirstname: " + firstname +
                "\nlastname: " + lastname + "\nphone: " + phoneNumber +
                "\nemail: " + email +
                "\npassword: " + password;
    }

    public static void updateManagers(){
        ArrayList<Manager> toBeRemoved =new ArrayList<>();
        for (Manager manager : allManagers) {
            if(manager.getStatus().equals(Status.DELETED))
                toBeRemoved.add(manager);
        }
        allManagers.removeAll(toBeRemoved);
    }
}