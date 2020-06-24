package GUI;

import java.io.IOException;

public abstract class ManagerProfileController {
    public void openPersonalInfo() throws IOException {
        Constants.getGuiManager().open("test2",1);
    }
    public void openAllProducts() throws IOException {
        Constants.getGuiManager().open("ManageProducts",1);
    }
    public void openAllUsers() throws IOException {
        Constants.getGuiManager().open("ManageUsers",1);
    }
    public void openDiscountCodes() throws IOException {
        Constants.getGuiManager().open("ManageDiscountCodes",1);
    }
    public void openCategories() throws IOException {
        Constants.getGuiManager().open("ManageCategories",1);
    }
    public void openRequests () throws IOException {
        Constants.getGuiManager().open("ManageRequests",1);
    }
    public void back() throws IOException {
        Constants.getGuiManager().back();
    }
    public void logout(){
        //TODO: logout process
    }
}
