package Models;

import java.util.ArrayList;

public class Manager extends User implements Packable {

    private static Manager mainManager;
    private static ArrayList<Manager> allManagers = new ArrayList<>();


    public Manager(String username){
        super(username);
    }

    @Override
    public String getType() {
        return "manager";
    }

    public static void setMainManager(Manager mainManager) {
        Manager.mainManager = mainManager;
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

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}