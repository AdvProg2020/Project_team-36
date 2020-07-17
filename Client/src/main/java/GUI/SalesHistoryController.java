package GUI;

import Models.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class SalesHistoryController extends SellerProfileController implements Initializable {


    public TableColumn id;
    public TableColumn date;
    public TableColumn totalPrice;
    public TableColumn customer;
    public TableColumn status;
    public TableView<ItemInLog> itemInLog;
    public TableColumn<ItemInLog, String> productName;
    public TableColumn<ItemInLog, Long> initialPrice;
    public Label phoneNumber;
    public TextField address;
    public ImageView profilePicture;
    public TableView<SellerLog> sellerLog;
    public Label usernameLabel;
    public TableColumn off;
    public TableColumn count;
    public TableColumn offPercent;
    private User user;

    @Override
    public void initialize(int id) throws IOException {

        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        } else {
            this.user = Constants.globalVariables.getLoggedInUser();
        }

        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        fill();

    }

    public void fill() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        status.setCellValueFactory(new PropertyValueFactory<>("logStatus"));
        off.setCellValueFactory(new PropertyValueFactory<>("sale"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerUsername"));

        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        initialPrice.setCellValueFactory(new PropertyValueFactory<>("initialPrice"));
        count.setCellValueFactory(new PropertyValueFactory<>("count"));
        offPercent.setCellValueFactory(new PropertyValueFactory<>("salePercentForTable"));

        sellerLog.getItems().addAll(((Seller)user).getAllLogs());

        sellerLog.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
            itemInLog.setItems(FXCollections.observableList(newValue.getAllItems()));
            address.setText(newValue.getCustomerAddress());
            phoneNumber.setText(newValue.getCustomerPhoneNumber());
        });


    }

}
