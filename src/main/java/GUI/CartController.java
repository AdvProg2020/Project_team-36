package GUI;

import Models.Customer;
import Models.SelectedItem;
import Models.Seller;
import Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CartController implements Initializable {
    @FXML
    private VBox vBox;
    @FXML
    private Label totalPrice;

    private Customer customer;
    private double price;

    public void logout() {
        Constants.getGuiManager().logout();
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

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

        for (SelectedItem selectedItem : customer.getCart()) {
            price += selectedItem.getItemTotalPrice();
            int counter = 0;
            for (Seller seller : selectedItem.getSellers()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemInCart.fxml"));
                Parent parent = fxmlLoader.load();
                ((ItemInCartController) fxmlLoader.getController()).fill(selectedItem.getProduct(),
                        selectedItem.getProduct().getProductFieldBySeller(seller),
                        selectedItem.getCountFromEachSeller().get(counter));
                vBox.getChildren().add(parent);
                counter++;
            }
        }

        totalPrice.setText("" + price);
    }
}
