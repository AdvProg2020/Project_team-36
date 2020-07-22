package GUI;

import Controllers.CustomerController;
import Controllers.EntryController;
import Models.Customer;
import Models.Product;
import Models.SelectedItem;
import Network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.List;

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
    private int id;

    @Override
    public void initialize(int id) throws IOException {
        this.id = id;
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else {
            this.customer = (Customer) Constants.globalVariables.getLoggedInUser();
        }
        if(id ==0){
            address.setText("-");
            address.setDisable(true);
            phoneNumber.setDisable(true);
            phoneNumber.setText("-");
        }

        totalPrice.setText("" + Constants.customerController.getCartPrice());
        totalPayable.setText("" + Constants.customerController.getCartPriceConsideringSale());
        Constants.customerController.addNewWaitingLog();
    }

    public void apply() {
        String discountCodeString = discountCode.getText();
        int discountCodeInteger = 0;
        if (!discountCodeString.equals("")) {
            try {
                discountCodeInteger = Integer.parseInt(discountCodeString);
                Constants.customerController.setDiscountCodeForPurchase(discountCodeInteger);
                totalPayable.setText("" + Constants.customerController.getWaitingLogPayable());
            } catch (CustomerController.NoDiscountAvailableWithId noDiscountAvailableWithId) {
                AlertBox.display("Error","You do not have this discount!!");
            } catch (NumberFormatException e){
                AlertBox.display("Error","Your discount code needs to be an integer!!");
            }
        }
    }

    public void checkout() throws IOException {
        if (address.getText().equals("")&&id==1){
            AlertBox.display("Error","You need to fill the address bar.");
            return;
        }
        Constants.customerController.setAddressForPurchase(address.getText());
        if (phoneNumber.getText().equals("")&&id==1){
            AlertBox.display("Error","You need to fill the phone number bar.");
            return;
        }
        if (!phoneNumber.getText().matches("(?<!\\d)\\d{11}(?!\\d)")&&id==1){
            AlertBox.display("Error","Your phone number format is invalid.");
            return;
        }
        Constants.customerController.setPhoneNumberForPurchase(phoneNumber.getText());
        try {
            Constants.customerController.purchase();
            afterPurchase();
            AlertBox.display("SUCCESS","All files downloaded successfully!\n returning to customer log");
            ((CustomerTemplateController) Constants.getGuiManager().
                    open("CustomerTemplate",customer.getUserId())).viewOrders();
        } catch (CustomerController.NotEnoughMoney notEnoughMoney) {
            AlertBox.display("Error","You do not have enough money for this purchase!");
        }
    }

    public void afterPurchase() throws IOException {
        if(!customer.getWaitingLog().isThereFile())
        ((CustomerTemplateController) Constants.getGuiManager().
                open("CustomerTemplate",customer.getUserId())).viewOrders();
        else{
            List<Product> files = customer.getWaitingLog().getFiles();
            String path = getDirectoryAddress();
            downloadFiles(files,path);
        }
    }

    public String getDirectoryAddress(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose files location!");
        File selectedDirectory = chooser.showDialog(Constants.getGuiManager().getLoginStage());
        return selectedDirectory.getPath();
    }

    public void downloadFiles( List<Product> files,String path) throws IOException {
        for (Product file : files) {
            File downloaded = new File(path+"/"+file.getFileProduct().getFileName()+"."+file.getFileProduct().getFileType());
            int count = 1;
            while(downloaded.exists()&&downloaded.isFile()){
                downloaded = new File(path+"/"+file.getFileProduct().getFileName()+"("+count+")."+file.getFileProduct().getFileType());
                count++;
            }
            OutputStream os = new FileOutputStream(downloaded);
            os.write(Client.readFile(file.getFileProduct().getFilePath()));
            os.close();
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
