package GUI;

import java.io.IOException;

public abstract class SellerProfileController {
    public void openPersonalInfo() throws IOException {
        Constants.getGuiManager().open("ManagerPersonalInfo",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openSalesHistory() throws IOException {
        Constants.getGuiManager().open("ManageProducts",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openManageProducts() throws IOException {
        Constants.getGuiManager().open("ManageUsers",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openShowCategories() throws IOException {
        Constants.getGuiManager().open("ManageDiscountCodes",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openViewBalance() throws IOException {
        Constants.getGuiManager().open("ManageCategories",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openViewOffs() throws IOException {
        Constants.getGuiManager().open("ManageRequests",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void back() throws IOException {
        Constants.getGuiManager().back();
    }
    public void logout(){
        Constants.getGuiManager().logout();
    }
}
