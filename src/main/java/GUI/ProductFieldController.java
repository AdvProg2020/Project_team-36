package GUI;

import Models.ProductField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ProductFieldController {
    @FXML
    private Label companyName;
    @FXML
    private Label price;
    @FXML
    private Label priceAfterSale;

    public void fill(ProductField productField) {
        companyName.setText(productField.getSeller().getCompanyName());
        price.setText("" + productField.getPrice());
        if (productField.getSale() != null){
            priceAfterSale.setText("" + (productField.getPrice() * (1 - productField.getSale().getSalePercent())));
            price.setTextFill(Color.RED);
        }else {
            priceAfterSale.setText("");
        }
    }
}
