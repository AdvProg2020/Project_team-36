package GUI;

import Controllers.EntryController;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AddNewSupporter extends ManagerProfileController implements Initializable{

    @FXML
    private TextField username;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField rePassword;
    @FXML
    private ImageView image;
    @FXML
    private Label alertLabel;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label usernameLabel;
    private String imagePath = "";
    private EntryController entryController = Constants.entryController;
    private User user;
    private final User loggedInUser = Constants.globalVariables.getLoggedInUser();

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

    public void addImage(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Profile");
        File file = fileChooser.showOpenDialog(Constants.getGuiManager().getLoginStage());
        if (file != null) {
            try {
                imagePath = file.getPath();
                image.setImage(new Image(new FileInputStream(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ;
    }

    public void register(MouseEvent mouseEvent) throws IOException {
        alertLabel.setStyle("-fx-text-fill: red");
        setTextFieldBorders();
        if (setUserName() && setPassword() && setName() && setPersonalInfo() && setImage()) {
            entryController.register();
            Constants.globalVariables.setLoggedInUser(loggedInUser);
            AlertBox.display("Profile Created","A New Supporter Profile Was Created!");
            Constants.getGuiManager().reopen();
        }
    }


    private void setTextFieldBorders() {
        email.setStyle("-fx-border-color: WHITE");
        firstname.setStyle("-fx-border-color: WHITE");
        lastname.setStyle("-fx-border-color: WHITE");
        password.setStyle("-fx-border-color: WHITE");
        rePassword.setStyle("-fx-border-color: WHITE");
        phone.setStyle("-fx-border-color: White");
        username.setStyle("-fx-border-color: white");
    }


    private boolean setUserName() {
        if (username.getText().isEmpty()) {
            username.setStyle("-fx-border-color: red");
            alertLabel.setText("complete all fields first!");
            return false;
        }
        if (username.getText().contains(" ") || username.getText().contains("!") || username.getText().contains("?")) {
            username.setStyle("-fx-border-color: red");
            alertLabel.setText("Username cannot have ! or ? or space");
            return false;
        }
        try {
            entryController.setUsernameRegister("supporter", username.getText());
            return true;
        } catch (EntryController.InvalidTypeException | EntryController.ManagerExistsException e) {
            e.printStackTrace();
            return false;
        } catch (EntryController.InvalidUsernameException e) {
            username.setStyle("-fx-border-color: red");
            alertLabel.setText("username already exists!");
            return false;
        }
    }

    private boolean setPassword() {
        if (password.getText().isEmpty() || rePassword.getText().isEmpty()) {
            password.setStyle("-fx-border-color: red");
            rePassword.setStyle("-fx-border-color: red");
            alertLabel.setText("complete all fields!");
            return false;
        }
        if (password.getText().length() < 8) {
            password.setStyle("-fx-border-color: red");
            alertLabel.setText("Password should at least be 8 letters");
            return false;
        } else if (!password.getText().equals(rePassword.getText())) {
            password.setStyle("-fx-border-color: red");
            rePassword.setStyle("-fx-border-color: red");
            alertLabel.setText("Different password boxes");
            return false;
        }
        entryController.setPassword(password.getText());
        return true;
    }

    private boolean setName() {
        if (firstname.getText().isEmpty()) {
            firstname.setStyle("-fx-border-color: red");
            alertLabel.setText("complete all fields!");
            return false;
        } else {
            entryController.setFirstname(firstname.getText());
        }
        if (lastname.getText().isEmpty()) {
            lastname.setStyle("-fx-border-color: red");
            alertLabel.setText("complete all fields!");
            return false;
        } else {
            entryController.setLastname(lastname.getText());
            return true;
        }
    }

    private boolean setPersonalInfo() {
        if (email.getText().isEmpty()) {
            email.setStyle("-fx-border-color: red");
            alertLabel.setText("complete all fields!");
            return false;
        } else if (!email.getText().matches(".+@.+\\..+")) {
            email.setStyle("-fx-border-color: red");
            alertLabel.setText("invalid email format!");
            return false;
        } else {
            entryController.setEmail(email.getText());
        }
        if (phone.getText().isEmpty()) {
            phone.setStyle("-fx-border-color: red");
            alertLabel.setText("complete all fields!");
            return false;
        } else if (!phone.getText().matches("(?<!\\d)\\d{11}(?!\\d)")) {
            phone.setStyle("-fx-border-color: red");
            alertLabel.setText("invalid phone number format!");
            return false;
        } else {
            entryController.setPhoneNumber(phone.getText());
            return true;
        }
    }

    private boolean setImage() {
        if (imagePath.isEmpty()) {
            alertLabel.setText("You need profile pic!");
            return false;
        }
        entryController.setImage(imagePath);
        return true;
    }


}
