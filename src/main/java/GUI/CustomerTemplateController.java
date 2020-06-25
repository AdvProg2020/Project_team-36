package GUI;

import Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class CustomerTemplateController implements Initializable{
    private User user;
    private PersonalInfoController personalInfoController;
    @FXML
    private Label username;
    @FXML
    private ImageView profilePicture;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(int id) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        } else {
            this.user = Constants.globalVariables.getLoggedInUser();
        }

        username.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());
    }

    public void logout(){
        Constants.getGuiManager().logout();
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    public void showPersonalInfo() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PersonalInfo.fxml"));
        Parent parent = fxmlLoader.load();
        this.personalInfoController = fxmlLoader.getController();
        personalInfoController.initialize(user.getUserId());
        scrollPane.setContent(parent);
    }


    public void viewOrders() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Orders.fxml"));
        Parent parent = fxmlLoader.load();
        ((OrdersController) fxmlLoader.getController()).fill();
        scrollPane.setContent(parent);
    }

    public void viewDiscounts() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Orders.fxml"));
        Parent parent = fxmlLoader.load();
        ((OrdersController) fxmlLoader.getController()).fill();
        scrollPane.setContent(parent);
    }

    public void goToCart() throws IOException {
        Constants.getGuiManager().open("Cart",user.getUserId());
    }
}
