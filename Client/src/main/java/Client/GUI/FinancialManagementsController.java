package Client.GUI;

import Client.Models.User;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class FinancialManagementsController extends ManagerProfileController implements Initializable {


    public TextField wageField;
    public TextField minimumField;
    public Label usernameLabel;
    public ImageView profilePicture;
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

        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        long minimum = Constants.managerController.getMinimum();
        minimumField.setText(Long.toString(minimum));

        int wage = ((int)Constants.managerController.getWage()*100);
        wageField.setText(Integer.toString(wage));

        minimumField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]"))
                minimumField.setText(minimumField.getText().replaceAll("[^\\d]", ""));
        });
        wageField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches(".*[^\\d].*"))
                wageField.setText(wageField.getText().replaceAll("[^\\d]", ""));
            if(!newValue.equals("")) {
                if (Integer.parseInt(newValue) > 100)
                    wageField.setText(oldValue);
            }
        });

    }

    public void setWageAction() {
        if(wageField.getText().isEmpty()){
            return;
        }
        int wage = Integer.parseInt(wageField.getText());
        Constants.managerController.setWage(wage*0.01);
        AlertBox.display("Done", "Wage was successfully set.");
    }

    public void setMinimumAction() {
        if(minimumField.getText().isEmpty()){
            return;
        }
        long money = Long.parseLong(minimumField.getText());
        Constants.managerController.setMinimum(money);
        AlertBox.display("Done", "Minimum money in wallet was successfully set.");

    }

}
