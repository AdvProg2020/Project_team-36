package GUI;

import Models.Customer;
import Models.Seller;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ViewUsersController extends ManagerProfileController implements Initializable {

    @FXML private TextField userUsername;
    @FXML private Label username;
    @FXML private  ImageView profilePicture;
    @FXML private  Label companyNameLabel;
    @FXML private  Label companyInfoLabel;
    @FXML private Label creditLabel;
    @FXML private TextField firstname;
    @FXML private TextField email;
    @FXML private TextField companyName;
    @FXML private TextField lastname;
    @FXML private TextField phoneNumber;
    @FXML private TextField companyInfo;
    @FXML private TextField credit;
    @FXML private ImageView userProfilePicture;


    @Override
    public void initialize(int id) throws IOException {
        User user = Constants.userController.getUserToView();
        User loggedInUser = Constants.userController.getUserById(id);
//        if (Constants.globalVariables.getLoggedInUser() != user) {
//            Constants.getGuiManager().back();
//        }
        username.setText(loggedInUser.getUsername());
        profilePicture.setImage(loggedInUser.getProfilePicture(150,150).getImage());

        userProfilePicture.setImage(user.getProfilePicture(100,100).getImage());
        userUsername.setText(user.getUsername());
        userUsername.setEditable(false);
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

}
