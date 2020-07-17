package GUI;

import Controllers.CustomerController;
import Controllers.EntryController;
import Models.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PurchaseController implements Initializable {
    @FXML
    private TextArea address;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField discountCode;
    @FXML
    private Label totalPrice;
    @FXML
    private Label totalPayable;

    private Customer customer;

    @Override
    public void initialize(int id) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        } else {
            this.customer = (Customer) Constants.globalVariables.getLoggedInUser();
        }

        totalPrice.setText("" + customer.getCartPrice());
        totalPayable.setText("" + customer.getCartPriceConsideringSale());
        Constants.customerController.addNewWaitingLog();
    }

    public void apply() {
        String discountCodeString = discountCode.getText();
        int discountCodeInteger = 0;
        if (!discountCodeString.equals("")) {
            try {
                discountCodeInteger = Integer.parseInt(discountCodeString);
                Constants.customerController.setDiscountCodeForPurchase(discountCodeInteger);
                totalPayable.setText("" + customer.getWaitingLog().getPayablePrice());
            } catch (CustomerController.NoDiscountAvailableWithId noDiscountAvailableWithId) {
                AlertBox.display("Error","You do not have this discount!!");
            } catch (NumberFormatException e){
                AlertBox.display("Error","Your discount code needs to be an integer!!");
            }
        }
    }

    public void checkout() throws IOException {
        if (address.getText().equals("")){
            AlertBox.display("Error","You need to fill the address bar.");
            return;
        }
        Constants.customerController.setAddressForPurchase(address.getText());
        if (phoneNumber.getText().equals("")){
            AlertBox.display("Error","You need to fill the phone number bar.");
            return;
        }
        if (!phoneNumber.getText().matches("(?<!\\d)\\d{11}(?!\\d)")){
            AlertBox.display("Error","Your phone number format is invalid.");
            return;
        }
        Constants.customerController.setPhoneNumberForPurchase(phoneNumber.getText());
        try {
            Constants.customerController.purchase();
            ((CustomerTemplateController) Constants.getGuiManager().
                    open("CustomerTemplate",customer.getUserId())).viewOrders();
        } catch (CustomerController.NotEnoughMoney notEnoughMoney) {
            AlertBox.display("Error","You do not have enough money for this purchase!");
        }
    }

    public void logout() throws EntryController.NotLoggedInException, IOException {
        Constants.getGuiManager().logout();
    }

    public void back() throws IOException {
        Constants.customerController.cancelPurchase();
        Constants.getGuiManager().back();
    }
}
