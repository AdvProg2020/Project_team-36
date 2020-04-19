package Models;

import java.util.ArrayList;

public class Manager extends User implements Packable {
    private static ArrayList<Manager> allManagers;

    public static ArrayList<Manager> getAllManagers() {
        return allManagers;
    }
}
