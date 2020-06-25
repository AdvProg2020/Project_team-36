package GUI;

import Controllers.EntryController;
import Controllers.GlobalVariables;
import Models.User;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LoginMenu {
private EntryController entryController;

    public Button back;
    public Label loginLabel;
    public TextField username;
    public PasswordField password;
    public CheckBox robotChecking;
    public Button loginButton;
public LoginMenu(){
    this.entryController = new EntryController(new GlobalVariables());
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
    //todo change to register menu
    }


}
