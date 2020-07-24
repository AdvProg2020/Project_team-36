package Client.GUI;

import Client.Models.Customer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CustomerWalletController implements Initializable {
    public TextField chargeField;
    public Label totalMoneyLabel;
    private int customerId;

    @Override
    public void initialize(int id) throws IOException {
        customerId = id;
        long total = Constants.customerController.getMoneyInWallet(id);
        totalMoneyLabel.setText(Long.toString(total));

        chargeField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]"))
                chargeField.setText(chargeField.getText().replaceAll("[^\\d]", ""));
        });
    }

    public void chargeAction() {
        if (chargeField.getText().isEmpty()) {
            return;
        }
        long money = Long.parseLong(chargeField.getText());
        String output = Constants.bankController.createReceiptAndPay("move", money + "",
                ((Customer) Constants.globalVariables.getLoggedInUser()).getWallet().getBankAccount(), "", "walletCharged");
        while (output.equals("token is invalid") || output.equals("token expired")) {
            //todo sayeh
            output = Constants.bankController.createReceiptAndPay("move", money + "",
                    ((Customer) Constants.globalVariables.getLoggedInUser()).getWallet().getBankAccount(), "", "walletCharged");
        }
        if (output.equals("done successfully")) {
            Constants.customerController.chargeWallet(money, customerId);
            chargeField.setText("");
        } else {
            AlertBox.display("Error", output);
        }
    }
}
