package GUI;

import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class SellerPersonalInfoController extends SellerProfileController implements Initializable {

    public Label usernameLabel;
    public ImageView profilePicture;
    public ScrollPane scrollPane;
    private User user;
    private PersonalInfoController personalInfoController;

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
        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

    }



    public void showPersonalInfo() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PersonalInfo.fxml"));
        Parent parent = fxmlLoader.load();
        this.personalInfoController = fxmlLoader.getController();
        personalInfoController.initialize(user.getUserId());
        scrollPane.setContent(parent);
    }



}
