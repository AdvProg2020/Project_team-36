package GUI;

import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class CustomerTemplateController implements Initializable{
    private User user;
    private PersonalInfoController personalInfoController;
    @FXML
    private Label username;
    @FXML
    private Button editInfo;

    @Override
    public void initialize(int id) throws IOException {
        this.user = User.getUserById(id);
        if (Constants.globalVariables.getLoggedInUser() != user) {
            Constants.getGuiManager().back();
        }
        username.setText(user.getUsername());
        editInfo.setVisible(false);
    }

}
