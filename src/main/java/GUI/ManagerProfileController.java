package GUI;

import java.io.IOException;

public abstract class ManagerProfileController {
    public void openPersonalInfo() throws IOException {
        Constants.getGuiManager().open("ManagerPersonalInfo",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openAllProducts() throws IOException {
        Constants.getGuiManager().open("ManageProducts",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openAllUsers() throws IOException {
        Constants.getGuiManager().open("ManageUsers",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openDiscountCodes() throws IOException {
        Constants.getGuiManager().open("ManageDiscountCodes",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openCategories() throws IOException {
        Constants.getGuiManager().open("ManageCategories",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openRequests () throws IOException {
        Constants.getGuiManager().open("ManageRequests",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void back() throws IOException {
        Constants.getGuiManager().back();
    }
    public void logout(){
        //TODO: logout process
    }
}
