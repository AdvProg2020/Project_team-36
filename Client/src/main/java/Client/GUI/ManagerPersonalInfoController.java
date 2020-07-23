package Client.GUI;

import Client.Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ManagerPersonalInfoController extends ManagerProfileController implements Initializable {

    private User user;
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
        showPersonalInfo();
    }

    public void showPersonalInfo() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PersonalInfo.fxml"));
        Parent parent = fxmlLoader.load();
        PersonalInfoController personalInfoController = fxmlLoader.getController();
        personalInfoController.initialize(user.getUserId());
        scrollPane.setContent(parent);
    }


}
