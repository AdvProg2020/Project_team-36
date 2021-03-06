package Repository;

import Models.*;

public class SaveUser {
    private static int lastId;
    private SaveManager saveManager;
    private SaveCustomer saveCustomer;
    private SaveSeller saveSeller;
    private SaveSupporter saveSupporter;


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
        if (SaveSupporter.load(id) != null){
            return SaveSupporter.load(id);
        }
        return null;
    }

    public SaveUser(User user){
        if (user != null){
            if (user instanceof Manager){
                this.saveManager = new SaveManager((Manager) user);
            }else if (user instanceof Customer){
                this.saveCustomer = new SaveCustomer((Customer) user);
            }else if (user instanceof Seller){
                this.saveSeller = new SaveSeller((Seller) user);
            }else if (user instanceof Supporter){
                this.saveSupporter = new SaveSupporter((Supporter) user);
            }
        }
    }

    public static int getLastId() {
        return lastId;
    }

    public static void setLastId(int lastId) {
        SaveUser.lastId = lastId;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public SaveCustomer getSaveCustomer() {
        return saveCustomer;
    }

    public SaveSeller getSaveSeller() {
        return saveSeller;
    }

    public SaveSupporter getSaveSupporter() {
        return saveSupporter;
    }
}
