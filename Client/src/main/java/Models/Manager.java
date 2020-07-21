package Models;

import Repository.SaveManager;
import Repository.SaveUser;

public class Manager extends User {

    public Manager(SaveManager saveManager) {
        super(saveManager);
    }

    @Override
    public String getType() {
        return "manager";
    }
}
