package Client.GUI;

import Client.Controllers.EntryController;
import javafx.event.ActionEvent;

import java.io.IOException;

public abstract class SellerProfileController {
    public void openPersonalInfo() throws IOException {
        Constants.getGuiManager().open("SellerPersonalInfo",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openSalesHistory() throws IOException {
        Constants.getGuiManager().open("SalesHistory",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openManageProducts() throws IOException {
        Constants.getGuiManager().open("SellerProducts",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void openShowCategories() throws IOException {
        Constants.getGuiManager().open("ShowCategories",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void openWallet() throws IOException {
        Constants.getGuiManager().open("SellerWallet",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void openViewOffs() throws IOException {
        Constants.getGuiManager().open("ViewOffs",Constants.globalVariables.getLoggedInUser().getUserId());
    }
    public void back() throws IOException {
        Constants.getGuiManager().back();
    }
    public void logout() throws EntryController.NotLoggedInException, IOException {
        Constants.getGuiManager().logout();
    }

    public void openAuction(ActionEvent actionEvent) throws IOException {
        Constants.getGuiManager().open("AddNewAuction",Constants.globalVariables.getLoggedInUser().getUserId());
    }
}
