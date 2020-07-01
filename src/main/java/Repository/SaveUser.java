package Repository;

import Models.User;

public class SaveUser {
    private static int lastId;
    public static User load(int id){
        lastId = Math.max(lastId,id);
        User potentialUser = User.getUserById(id);
        if(potentialUser != null){
            return potentialUser;
        }
        if(SaveManager.load(id) != null){
            return SaveManager.load(id);
        }
        if (SaveSeller.load(id) != null){
            return SaveSeller.load(id);
        }
        if (SaveCustomer.load(id) != null){
            return SaveCustomer.load(id);
        }
        return null;
    }

    public static int getLastId() {
        return lastId;
    }

    public static void setLastId(int lastId) {
        SaveUser.lastId = lastId;
    }
}
