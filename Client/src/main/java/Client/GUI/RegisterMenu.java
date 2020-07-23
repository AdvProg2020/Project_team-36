package Client.GUI;

import Client.Controllers.EntryController;
import Client.Network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class RegisterMenu {
    @FXML private Label customerLabel;
    @FXML private Label sellerLabel;
    @FXML private ImageView profileSeller;
    @FXML private ImageView customerImage;
    @FXML private Tab seller;
    @FXML private Tab customer;
    @FXML private TextArea companyInfo;
    @FXML private TextField companyName;
    @FXML private PasswordField sellerRePassword;
    @FXML private PasswordField sellerPassword;
    @FXML private TextField sellerPhone;
    @FXML private TextField sellerEmail;
    @FXML private TextField sellerLastname;
    @FXML private TextField sellerFirstname;
    @FXML private TextField sellerUsername;
    @FXML private PasswordField customerRePassword;
    @FXML private PasswordField customerPassword;
    @FXML private TextField customerPhone;
    @FXML private TextField customerEmail;
    @FXML private TextField customerLastname;
    @FXML private TextField customerFirstname;
    @FXML private TextField customerUsername;
    EntryController entryController;
    private File sellerProfileFile = null;
    private File customerProfileFile = null;

    public RegisterMenu() {
        this.entryController = Constants.entryController;

    }

    public void registerSeller(MouseEvent mouseEvent) {
        sellerLabel.setStyle("-fx-text-fill: red");
        setSellerTextFieldBorders();
        if (setUserName(true) && setPassword(true)&& setName(true)&& setSellerCompanyInfo()&& setPersonalInfo(true)&&setImage(true)){
            entryController.register();
            sellerLabel.setText("Successfully Entered");
            sellerLabel.setStyle("-fx-text-fill: green");
        }
    }

    public void registerCustomer(MouseEvent mouseEvent) {
        customerLabel.setStyle("-fx-text-fill: red");
        setCustomerTextFieldBorders();
        if (setUserName(false) && setPassword(false)&& setName(false)&& setPersonalInfo(false)&&setImage(false)){
            entryController.register();
            customerLabel.setText("Successfully Entered");
            customerLabel.setStyle("-fx-text-fill: green");
        }
    }

    private void setSellerTextFieldBorders() {
        sellerEmail.setStyle("-fx-border-color: WHITE");
        sellerFirstname.setStyle("-fx-border-color: WHITE");
        sellerLastname.setStyle("-fx-border-color: WHITE");
        sellerPhone.setStyle("-fx-border-color: WHITE");
        sellerPassword.setStyle("-fx-border-color: WHITE");
        sellerRePassword.setStyle("-fx-border-color: White");
        sellerUsername.setStyle("-fx-border-color: white");
        companyInfo.setStyle("-fx-border-color: white");
        companyName.setStyle("-fx-border-color: white");
    }

    private boolean setUserName(boolean isSeller) {
        TextField username = sellerUsername;
        Label label = sellerLabel;
        if(!isSeller){
            username = customerUsername;
            label = customerLabel;
        }
        if (username.getText().isEmpty()) {
            username.setStyle("-fx-border-color: red");
            label.setText("complete all fields first!");
            return false;
        }
        if (username.getText().contains(" ") || username.getText().contains("!") || username.getText().contains("?")) {
            username.setStyle("-fx-border-color: red");
            label.setText("Username cannot have ! or ? or space");
            return false;
        }
        try {
           if(isSeller) {
                entryController.setUsernameRegister("seller", username.getText());
            }else {
               entryController.setUsernameRegister("customer", username.getText());
           }
            return true;
        } catch (EntryController.InvalidTypeException | EntryController.ManagerExistsException e) {
            e.printStackTrace();
            return false;
        } catch (EntryController.InvalidUsernameException e) {
            username.setStyle("-fx-border-color: red");
            label.setText("username already exists!");
            return false;
        }
    }

    private boolean setPassword(boolean isSeller) {
        TextField password = sellerPassword;
        TextField rePassword = sellerRePassword;
        Label label = sellerLabel;
        if(!isSeller) {
            password = customerPassword;
            rePassword = customerRePassword;
            label = customerLabel;
        }
        if (password.getText().isEmpty() || rePassword.getText().isEmpty()) {
            password.setStyle("-fx-border-color: red");
            rePassword.setStyle("-fx-border-color: red");
            label.setText("complete all fields!");
            return false;
        }
        if (password.getText().length() < 8 ) {
            password.setStyle("-fx-border-color: red");
            label.setText("Password should at least be 8 letters");
            return false;
        } else if (!password.getText().equals(rePassword.getText())) {
            password.setStyle("-fx-border-color: red");
            rePassword.setStyle("-fx-border-color: red");
            label.setText("Different password boxes");
            return false;
        }
        entryController.setPassword(password.getText());
        return true;
    }

    private boolean setName(boolean isSeller){
        TextField firstname = sellerFirstname;
        TextField lastname = sellerLastname;
        Label label = sellerLabel;
        if(!isSeller){
            firstname = customerFirstname;
            lastname = customerLastname;
            label = customerLabel;
        }
        if(firstname.getText().isEmpty()){
            firstname.setStyle("-fx-border-color: red");
            label.setText("complete all fields!");
            return false;
        }else{
            entryController.setFirstname(firstname.getText());
        }
        if(lastname.getText().isEmpty()){
            lastname.setStyle("-fx-border-color: red");
            label.setText("complete all fields!");
            return false;
        }else{
            entryController.setLastname(lastname.getText());
            return true;
        }
    }

    private boolean setSellerCompanyInfo(){
        if(companyName.getText().isEmpty()){
            companyName.setStyle("-fx-border-color: red");
            sellerLabel.setText("complete all fields!");
            return false;
        }else{
            entryController.setCompany(companyName.getText());
        }
        if(companyInfo.getText().isEmpty()){
            companyInfo.setStyle("-fx-border-color: red");
            sellerLabel.setText("complete all fields!");
            return false;
        }else{
            entryController.setCompanyInfo(companyInfo.getText());
            return true;
        }
    }

    private boolean setPersonalInfo(boolean isSeller){
        TextField email = sellerEmail;
        TextField phone = sellerPhone;
        Label label = sellerLabel;
        if(!isSeller){
             email = customerEmail;
             phone = customerPhone;
             label = customerLabel;
        }
        if(email.getText().isEmpty()){
            email.setStyle("-fx-border-color: red");
            label.setText("complete all fields!");
            return false;
        }else if(!email.getText().matches(".+@.+\\..+")){
            email.setStyle("-fx-border-color: red");
            label.setText("invalid email format!");
            return false;
        }else{
            entryController.setEmail(email.getText());
        }
        if(phone.getText().isEmpty()){
            phone.setStyle("-fx-border-color: red");
            label.setText("complete all fields!");
            return false;
        }else if(!phone.getText().matches("(?<!\\d)\\d{11}(?!\\d)")){
            phone.setStyle("-fx-border-color: red");
            label.setText("invalid phone number format!");
            return false;
        }else{
            entryController.setPhoneNumber(phone.getText());
            return true;
        }
    }

    private void setCustomerTextFieldBorders() {
        customerEmail.setStyle("-fx-border-color: WHITE");
        customerFirstname.setStyle("-fx-border-color: WHITE");
        customerLastname.setStyle("-fx-border-color: WHITE");
        customerPassword.setStyle("-fx-border-color: WHITE");
        customerRePassword.setStyle("-fx-border-color: WHITE");
        customerPhone.setStyle("-fx-border-color: White");
        customerUsername.setStyle("-fx-border-color: white");
    }

    public void addSellerImage(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Profile");
        File file = fileChooser.showOpenDialog(Constants.getGuiManager().getLoginStage());
        if (file != null){
            try {
                sellerProfileFile = file;
                profileSeller.setImage(new Image(new FileInputStream(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addCustomerImage() {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Profile");
        File file = fileChooser.showOpenDialog(Constants.getGuiManager().getLoginStage());
        if (file != null){
            try {
                customerProfileFile = file;
                customerImage.setImage(new Image(new FileInputStream(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean setImage(boolean isSeller) {
        if(isSeller){
            if(sellerProfileFile==null){
                sellerLabel.setText("You need profile pic!");
                return false;
            }
            String path = null;
            try {
                path = Client.writeFile(Files.readAllBytes(sellerProfileFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            entryController.setImage(path);
            return true;
        }else{
            if(customerProfileFile ==null) {
                customerLabel.setText("You need profile pic!");
                return false;
            }
            String path = null;
            try {
                path = Client.writeFile(Files.readAllBytes(customerProfileFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            entryController.setImage(path);
            return true;
        }
    }

    public void login() {
        Constants.globalVariables.setLoggedInUser(null);
        Constants.getGuiManager().openLogin();
    }

}