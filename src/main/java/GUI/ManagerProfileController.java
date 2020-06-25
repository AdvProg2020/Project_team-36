package GUI;

import java.io.IOException;

public abstract class ManagerProfileController {
    public void openPersonalInfo() throws IOException {
        Constants.getGuiManager().open("ManagerPersonalInfo",2);
    }
    public void openAllProducts() throws IOException {
        Constants.getGuiManager().open("ManageProducts",2);
    }
    public void openAllUsers() throws IOException {
        Constants.getGuiManager().open("ManageUsers",2);
    }
    public void openDiscountCodes() throws IOException {
        Constants.getGuiManager().open("ManageDiscountCodes",2);
    }
    public void openCategories() throws IOException {
        Constants.getGuiManager().open("ManageCategories",2);
    }
    public void openRequests () throws IOException {
        Constants.getGuiManager().open("ManageRequests",2);
    }
    public void back() throws IOException {
        Constants.getGuiManager().back();
    }
    public void logout(){
        //TODO: logout process
    }
}
