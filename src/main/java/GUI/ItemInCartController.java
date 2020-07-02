package GUI;

import Controllers.CustomerController;
import Models.Product;
import Models.ProductField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;

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
    @FXML
    private ImageView image;

    private Product product;
    private ProductField productField;

    public void fill(Product product, ProductField productField, int count) throws MalformedURLException {
        image.setImage(product.getProductImage(150,125).getImage());
        this.product = product;
        this.productField = productField;
        productName.setText(product.getName());
        sellerUserName.setText(productField.getSeller().getUsername());
        double price =productField.getPrice();
        if(productField.getSale()!=null){
             price = (1 - productField.getSale().getSalePercent()) * productField.getPrice();
        }
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

    public void increase() throws IOException, CustomerController.NoProductWithIdInCart {
        count.setText("" + (Integer.parseInt(count.getText()) + 1));
        fill(product,productField,Integer.parseInt(count.getText()));
        try {
            Constants.customerController.increaseProductInCart(productField.getMainProductId());
        }catch (CustomerController.NotEnoughSupply e){
            AlertBox.display("Out of this","We do not have more supply of this item for now!!");
        }catch (CustomerController.MoreThanOneSellerForItem e){
            try {
                Constants.customerController.increaseProductInCart(productField.getSeller().getUserId(),productField.getMainProductId());
            }catch ( CustomerController.NotEnoughSupply ee){
                AlertBox.display("Out of this","We do not have more supply of this item for now!!");
            }
        }

        Constants.getGuiManager().reopen();
    }

    public void decrease() throws IOException, CustomerController.NoProductWithIdInCart {
        count.setText("" + (Integer.parseInt(count.getText()) - 1));
        fill(product,productField,Integer.parseInt(count.getText()));
        try {
            Constants.customerController.decreaseProductInCart(productField.getMainProductId());
        }catch (CustomerController.MoreThanOneSellerForItem e){
            Constants.customerController.decreaseProductInCart(productField.getSeller(),productField.getMainProductId());
        }
        Constants.getGuiManager().reopen();
    }
}
