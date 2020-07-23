package Client.GUI;

import Client.Controllers.EntryController;
import Client.Controllers.ProductsController;
import Client.Models.Customer;
import Client.Models.ProductField;
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

    private ProductField productField;

    public void fill(ProductField productField) {
        this.productField = productField;
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

    public void addToCart() throws ProductsController.NoSellerWithUsername, EntryController.NotLoggedInException,
            ProductsController.UserCantBuy, ProductsController.NoSellerIsChosen {
        try {
            Constants.productsController.addSellerForBuy(productField.getSeller().getUsername());
            Constants.productsController.addToCart();
        }
        catch (ProductsController.NotEnoughSupply e){
            AlertBox.display("Out of this","We are out of this item for now!!");
        }
    }
}
