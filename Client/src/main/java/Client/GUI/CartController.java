package Client.GUI;

import Client.Controllers.CustomerController;
import Client.Models.SelectedItem;
import Client.Models.Seller;
import Client.Controllers.EntryController;
import Client.Models.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CartController implements Initializable {
    @FXML
    private VBox vBox;
    @FXML
    private Label totalPrice;
    @FXML
    private Button purchase;

    private Customer customer;
    private double price;

    public void logout() throws EntryController.NotLoggedInException, IOException {
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
        } else {
            this.customer = (Customer) Constants.globalVariables.getLoggedInUser();
        }

        if (customer.getCart().isEmpty()){
            purchase.setVisible(false);
        }
        else {
            for (SelectedItem selectedItem : customer.getCart()) {
                price += selectedItem.getItemTotalPrice();
                int counter = 0;
                for (Seller seller : selectedItem.getSellers()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemInCart.fxml"));
                    Parent parent = fxmlLoader.load();
                    ((ItemInCartController) fxmlLoader.getController()).fill(selectedItem.getProduct(),
                            selectedItem.getProduct().getProductFieldBySeller(seller.getUserId()),
                            selectedItem.getCountFromEachSeller().get(counter));
                    vBox.getChildren().add(parent);
                    counter++;
                }
            }
        }

        totalPrice.setText("" + price);
    }

    public void goToPurchase() throws IOException{
        try {
            Constants.customerController.startPurchase();
            Constants.getGuiManager().open("Purchase",Constants.customerController.isOnlyFileInCart()?0:1);
        }catch (CustomerController.NotEnoughSupplyInCart e){
            AlertBox.display("Error","Unfortunately we are out of some of the items you have ordered!!");
        }catch (CustomerController.EmptyCart e){
            AlertBox.display("Error","You can not purchase since your cart is empty!!");
        }
    }
}
