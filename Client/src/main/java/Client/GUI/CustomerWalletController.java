package Client.GUI;

import Client.Models.Customer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CustomerWalletController implements Initializable {
    public TextField chargeField;
    public Label totalMoneyLabel;
    private int customerId;
    private CustomerTemplateController customerTemplateController;

    public void setCustomerTemplateController(CustomerTemplateController customerTemplateController) {
        this.customerTemplateController = customerTemplateController;
    }

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

    public void chargeAction() throws IOException {
        if (chargeField.getText().isEmpty()) {
            return;
        }
        long money = Long.parseLong(chargeField.getText());
        String output = Constants.bankController.createReceiptAndPay("move", money + "",
                ((Customer) Constants.globalVariables.getLoggedInUser()).getWallet().getBankAccount(), "", "walletCharged");
        while (output.equals("token is invalid") || output.equals("token expired")) {
            BankGetToken.display();
            output = Constants.bankController.createReceiptAndPay("move", money + "",
                    ((Customer) Constants.globalVariables.getLoggedInUser()).getWallet().getBankAccount(), "", "walletCharged");
        }
        if (output.equals("done successfully")) {
            Constants.customerController.chargeWallet(money, customerId);
            AlertBox.display("Done", "wallet was successfully charged.");
            chargeField.setText("");
            customerTemplateController.openWallet();
        } else {
            AlertBox.display("Error", output);
        }
    }
}
