package Client.GUI;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CustomerWalletController implements Initializable {
    public TextField chargeField;
    public Label totalMoneyLabel;
    private int customerId;

    @Override
    public void initialize(int id) throws IOException {
        customerId=id;
        long total = Constants.customerController.getMoneyInWallet(id);
        totalMoneyLabel.setText(Long.toString(total));

        chargeField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]"))
                chargeField.setText(chargeField.getText().replaceAll("[^\\d]", ""));
        });
    }

    public void chargeAction() {
        if(chargeField.getText().isEmpty()){
            return;
        }

        //todo check if bank account has enough to charge

        long money = Long.parseLong(chargeField.getText());
        Constants.customerController.chargeWallet(money, customerId);
    }
}
