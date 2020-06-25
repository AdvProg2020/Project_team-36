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
    private Button editInfo;
    @FXML
    private ImageView profilePicture;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(int id) throws IOException {
        this.user = User.getUserById(id);
        if (Constants.globalVariables.getLoggedInUser() != user) {
            Constants.getGuiManager().back();
        }
        username.setText(user.getUsername());
        editInfo.setVisible(false);
        profilePicture.setImage(user.getProfilePicture().getImage());
    }

    public void logout(){
        Constants.getGuiManager().logout();
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    public void showPersonalInfo() throws IOException {
        editInfo.setVisible(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PersonalInfo.fxml"));
        Parent parent = fxmlLoader.load();
        this.personalInfoController = fxmlLoader.getController();
        personalInfoController.initialize(user.getUserId());
        scrollPane.setContent(parent);
        editInfo.setOnAction(actionEvent -> personalInfoController.editInfo());
    }

    public void goToCart() throws IOException {
        Constants.getGuiManager().open("Cart",1);
    }
}
