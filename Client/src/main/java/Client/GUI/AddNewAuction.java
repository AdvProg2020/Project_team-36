package Client.GUI;

import Client.Models.Product;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddNewAuction extends SellerProfileController implements Initializable{
    public Label dateAlert;
    public DatePicker datePicker;
    public Label chooseProductAlert;
    public ComboBox<String> chooseProduct;
    public Label successAlert;
    private List<Product> products = new ArrayList<>();

    @Override
    public void initialize(int id) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        }
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        for (Product product : Constants.auctionController.getAllAvailableProducts()) {
            chooseProduct.getItems().add(product.getName());
            products.add(product);
        }
    }

    public void addAuction(ActionEvent actionEvent) {
        if(datePicker.getValue()==null){
            dateAlert.setVisible(true);
            if(chooseProduct.getValue() !=null)
                return;
        }
        if(chooseProduct.getValue() ==null){
            chooseProductAlert.setVisible(true);
         return;
        }
        LocalDate date = datePicker.getValue();
        for (Product product : products) {
            if(product.getName().equals(chooseProduct.getValue())) {
                Date endDate = java.sql.Date.valueOf(date);
                Constants.auctionController.addNewAuction(product.getProductId(), endDate);
                successAlert.setVisible(true);
                try {
                    Constants.getGuiManager().reopen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
