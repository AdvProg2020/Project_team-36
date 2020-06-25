package GUI;

import Controllers.UserController;
import Models.Customer;
import Models.Seller;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PersonalInfoController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField companyName;
    @FXML
    private TextField companyInfo;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmedPassword;
    @FXML
    private TextField credit;
    @FXML
    private Label companyNameLabel;
    @FXML
    private Label companyInfoLabel;
    @FXML
    private Label creditLabel;

    private User user;

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
        username.setEditable(false);
        firstname.setText(user.getFirstname());
        lastname.setText(user.getLastname());
        phoneNumber.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
        if (user instanceof Seller) {
            companyInfo.setText(((Seller) user).getCompanyInfo());
            companyName.setText(((Seller) user).getCompanyName());
            credit.setText("" + ((Seller) user).getCredit());
            credit.setEditable(false);
        } else if (user instanceof Customer) {
            credit.setText("" + ((Customer) user).getCredit());
            credit.setEditable(false);
            companyInfo.setVisible(false);
            companyName.setVisible(false);
            companyInfoLabel.setVisible(false);
            companyNameLabel.setVisible(false);
        } else {
            companyInfo.setVisible(false);
            companyName.setVisible(false);
            credit.setVisible(false);
            companyInfoLabel.setVisible(false);
            companyNameLabel.setVisible(false);
            creditLabel.setVisible(false);
        }
    }

    public void editInfo() {
        String output = "";
        if (!firstname.getText().equals(user.getFirstname())) {
            try {
                Constants.userController.editInfo("setFirstname", firstname.getText());
                output += "First name is changed successfully\n";
            } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                output += "First name is not changed successfully\n";
            }
        }

        if (!lastname.getText().equals(user.getLastname())) {
            try {
                Constants.userController.editInfo("setLastname", lastname.getText());
                output += "Last name is changed successfully\n";
            } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                output += "Last name is not changed successfully\n";
            }
        }

        if (!email.getText().equals(user.getEmail())) {
            if (!email.getText().matches(".+@.+\\..*|back")) {
                output += "New email format is not valid\n";
            } else {
                try {
                    Constants.userController.editInfo("setEmail", email.getText());
                    output += "Email is changed successfully\n";
                } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                    output += "Email is not changed successfully\n";
                }
            }
        }

        if (!phoneNumber.getText().equals(user.getPhoneNumber())) {
            if (!phoneNumber.getText().matches("\\d+|back")) {
                output += "New phone number format is not valid\n";
            } else {
                try {
                    Constants.userController.editInfo("setPhoneNumber", phoneNumber.getText());
                    output += "Phone number is changed successfully\n";
                } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                    output += "Phone number is not changed successfully\n";
                }
            }
        }

        if (user instanceof Seller) {
            if (!companyName.getText().equals(((Seller) user).getCompanyName())) {
                try {
                    Constants.userController.editInfo("setCompanyName", companyName.getText());
                    output += "Company name is changed successfully\n";
                } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                    output += "Company name is not changed successfully\n";
                }
            }

            if (!companyInfo.getText().equals(((Seller) user).getCompanyInfo())) {
                try {
                    Constants.userController.editInfo("setCompanyInfo", companyInfo.getText());
                    output += "Company info is changed successfully\n";
                } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                    output += "Company info is not changed successfully\n";
                }
            }
        }

        if (!password.getText().equals("")) {
            if (!password.getText().equals(user.getPassword())) {
                if (!password.getText().equals(confirmedPassword.getText())) {
                    output += "Password is not confirmed successfully";
                } else {
                    try {
                        Constants.userController.editInfo("setPassword", password.getText());
                        output += "Password is changed successfully";
                    } catch (UserController.NoFieldWithThisType noFieldWithThisType) {
                        output += "Password is not changed successfully";
                    }
                }
            }
        }

        AlertBox.display("Edit Info", output);
        try {
            initialize(user.getUserId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
