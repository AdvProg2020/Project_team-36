package Client.GUI;

import Client.Models.Customer;
import Client.Models.Seller;
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
        long money = Long.parseLong(chargeField.getText());
        String output = Constants.bankController.createReceiptAndPay("move",money+"","",
                ((Customer)Constants.globalVariables.getLoggedInUser()).getWallet().getBankAccount(),"walletCharged");
        while (output.equals("token is invalid") || output.equals("token expired")){
            //todo sayeh
            output = Constants.bankController.createReceiptAndPay("move",money+"","",
                    ((Customer)Constants.globalVariables.getLoggedInUser()).getWallet().getBankAccount(),"walletCharged");
        }
        if (output.equals("done successfully")){
            Constants.sellerController.chargeWallet(money, sellerId);
            chargeField.setText("");
        }else {
            AlertBox.display("Error",output);
        }
    }

    public void withdrawAction() {
        if(withdrawField.getText().isEmpty()){
            return;
        }
        long money = Long.parseLong(withdrawField.getText());
        if(Constants.sellerController.isThereEnoughAvailable(money, sellerId)){
            String output = Constants.bankController.createReceiptAndPay("move",money + "","",
                    ((Seller)Constants.globalVariables.getLoggedInUser()).getWallet().getBankAccount(),"Withdrawed");
            if (output.equals("done successfully")){
                Constants.sellerController.withdrawFromWallet(money, sellerId);
            }else {
                AlertBox.display("Error",output);
            }
        } else {
            withdrawError.setVisible(true);
            minimumError.setVisible(true);
        }
        withdrawField.clear();

    }
}
