package GUI;

import Models.Product;
import Models.ProductField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ItemInCartController {
    @FXML
    private Label productName;
    @FXML
    private Label sellerUserName;
    @FXML
    private Label price;
    @FXML
    private Label totalPrice;
    @FXML
    private TextField count;
    @FXML
    private Button increase;
    @FXML
    private Button decrease;

    private Product product;
    private ProductField productField;

    public void fill(Product product, ProductField productField, int count) {
        this.product = product;
        this.productField = productField;
        productName.setText(product.getName());
        sellerUserName.setText(productField.getSeller().getUsername());
        double price = (1 - productField.getSale().getSalePercent()) * productField.getPrice();
        this.price.setText("" + price);
        this.count.setEditable(false);
        this.count.setText("" + count);
        this.totalPrice.setText((price * count) + "");
        if (count == productField.getSupply()) {
            increase.setVisible(false);
        }

        if (count == 0) {
            decrease.setVisible(false);
        }
    }

    public void increase() {
        count.setText("" + (Integer.parseInt(count.getText()) + 1));
        fill(product,productField,Integer.parseInt(count.getText()));
        //todo: taghirate inja ro tu carti k tucustomer zakhire shode hm byd emal kni hala chjuri idk
    }

    public void decrease(){
        count.setText("" + (Integer.parseInt(count.getText()) - 1));
        fill(product,productField,Integer.parseInt(count.getText()));
        //todo: taghirate inja ro tu carti k tucustomer zakhire shode hm byd emal kni hala chjuri idk
    }
}
