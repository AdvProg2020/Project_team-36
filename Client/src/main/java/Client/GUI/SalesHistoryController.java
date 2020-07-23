package Client.GUI;

import Client.Models.User;
import Models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Date;

public class SalesHistoryController extends SellerProfileController implements Initializable {


    @FXML private TableColumn<Client.Models.SellerLog,Integer> id;
    @FXML private TableColumn<Client.Models.SellerLog, Date> date;
    @FXML private TableColumn<Client.Models.SellerLog,Long> totalPrice;
    @FXML private TableColumn<Client.Models.SellerLog,String> customer;
    @FXML private TableView<ItemInLog> itemInLog;
    @FXML private TableColumn<ItemInLog, String> productName;
    @FXML private TableColumn<ItemInLog, Long> initialPrice;
    @FXML private Label phoneNumber;
    @FXML private TextField address;
    @FXML private ImageView profilePicture;
    @FXML private TableView<Client.Models.SellerLog> sellerLog;
    @FXML private Label usernameLabel;
    @FXML private TableColumn<Client.Models.SellerLog,Long> off;
    @FXML private TableColumn<ItemInLog,Integer> count;
    @FXML private TableColumn<ItemInLog,String> offPercent;
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
        off.setCellValueFactory(new PropertyValueFactory<>("sale"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerUsername"));

        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        initialPrice.setCellValueFactory(new PropertyValueFactory<>("initialPrice"));
        count.setCellValueFactory(new PropertyValueFactory<>("count"));
        offPercent.setCellValueFactory(new PropertyValueFactory<>("salePercentForTable"));

        sellerLog.getItems().addAll(((Client.Models.Seller)user).getAllLogs());

        sellerLog.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
            itemInLog.setItems(FXCollections.observableList(newValue.getAllItems()));
            address.setText(newValue.getCustomerAddress());
            phoneNumber.setText(newValue.getCustomerPhoneNumber());
        });


    }

}
