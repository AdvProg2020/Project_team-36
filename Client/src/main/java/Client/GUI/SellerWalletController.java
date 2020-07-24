package Client.GUI;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class SellerWalletController extends SellerProfileController implements Initializable {


    public Label usernameLabel;
    public ImageView profilePicture;
    public TextField chargeField;
    public Label totalMoneyLabel;
    public TextField withdrawField;
    public Label withdrawError;
    public Label minimumError;
    private int sellerId;

    @Override
    public void initialize(int id) throws IOException {
        sellerId=id;
        long total = Constants.sellerController.getMoneyInWallet(id);
        totalMoneyLabel.setText(Long.toString(total));

        chargeField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]"))
                chargeField.setText(chargeField.getText().replaceAll("[^\\d]", ""));
        });
        withdrawField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]"))
                withdrawField.setText(withdrawField.getText().replaceAll("[^\\d]", ""));
        });
    }

    public void chargeAction() {
        if(chargeField.getText().isEmpty()){
            return;
        }

        //todo check if bank account has enough to charge
        //todo az hesabe furushande be hesabe furushgah
        long money = Long.parseLong(chargeField.getText());
        Constants.sellerController.chargeWallet(money, sellerId);
        chargeField.clear();
    }

    public void withdrawAction() {
        if(withdrawField.getText().isEmpty()){
            return;
        }
        long money = Long.parseLong(withdrawField.getText());
        //todo az hesabe furushgah be hesabe furushande

        if(Constants.sellerController.isThereEnoughAvailable(money, sellerId)){
            Constants.sellerController.withdrawFromWallet(money, sellerId);
        } else {
            withdrawError.setVisible(true);
            minimumError.setVisible(true);
        }
        withdrawField.clear();

    }
}
