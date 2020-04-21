package Models;

import java.util.ArrayList;

public class Manager extends User implements Packable {

    public Manager(String username){
        super(username);
    }
    private static ArrayList<Manager> allManagers;

    public static ArrayList<Manager> getAllManagers() {
        return allManagers;
    }

    public static boolean canManagerRegister(){
        if(allManagers.isEmpty())
            return true;
        return false;
    }


    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
