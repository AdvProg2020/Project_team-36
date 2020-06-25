package GUI;

import Models.Customer;
import Models.ProductField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ProductFieldController {
    @FXML
    private Label companyName;
    @FXML
    private Label price;
    @FXML
    private Label priceAfterSale;
    @FXML
    private HBox hBox;
    @FXML
    private Button addToCart;

    public void fill(ProductField productField) {
        if (Constants.globalVariables.getLoggedInUser() == null){
            hBox.getChildren().remove(addToCart);
        }else if (!(Constants.globalVariables.getLoggedInUser() instanceof Customer)){
            hBox.getChildren().remove(addToCart);
        }
        companyName.setText(productField.getSeller().getCompanyName());
        price.setText("" + productField.getPrice());
        if (productField.getSale() != null){
            priceAfterSale.setText("" + (productField.getPrice() * (1 - productField.getSale().getSalePercent())));
            price.setTextFill(Color.RED);
        }else {
            priceAfterSale.setText("");
        }
    }

    public void addToCart(){
        //todo: add to cart ro hm nmidunm controllerash chian
    }
}
