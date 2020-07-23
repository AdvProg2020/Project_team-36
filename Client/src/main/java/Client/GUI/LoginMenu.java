package Client.GUI;

import Client.Controllers.EntryController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class LoginMenu {
private EntryController entryController;

    @FXML private Button back;
    @FXML private Label loginLabel;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private CheckBox robotChecking;
    @FXML private Button loginButton;

    public LoginMenu(){
    this.entryController = Constants.entryController;
    }


    public void loginProcess(MouseEvent mouseEvent) {
        username.setStyle("-fx-text-box-border: WHITE;");
        password.setStyle("-fx-border-color: WHITE");
        if(username.getText().isEmpty()) {
            username.setStyle("-fx-text-box-border: RED;");
            loginLabel.setText("fill username field");
            return;
        }else if(password.getText().isEmpty()){
            password.setStyle("-fx-border-color: RED");
            loginLabel.setText("fill password field");
            return;
        }
        try {
            entryController.setUserNameLogin(username.getText());
            entryController.setPasswordLogin(password.getText());
            Constants.getGuiManager().successfulLogin();
        } catch (EntryController.InvalidUsernameException e) {
            username.setStyle("-fx-text-box-border: RED;");
            loginLabel.setText("No user with this id!");
        } catch (EntryController.WrongPasswordException e) {
            password.setStyle("-fx-border-color: RED;");
            loginLabel.setText("Wrong Password");
        }

    }

    public void checkBoxClicked(MouseEvent mouseEvent) {
        if(robotChecking.isSelected()){
            loginButton.setDisable(false);
        }
        else
            loginButton.setDisable(true);
    }

    public void createAccount(MouseEvent mouseEvent) {
        Constants.getGuiManager().openRegister();
    }


}
