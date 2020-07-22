package GUI;

import Models.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Date;

public class ManageAllOrdersController extends SellerProfileController implements Initializable {
    public Label usernameLabel;
    public ImageView profilePicture;
    public TableColumn<CustomerLog,Integer> id;
    public TableColumn<CustomerLog, Date> date;
    public TableView<ItemInLog> itemInLog;
    public TableColumn<CustomerLog,Long> totalPrice;
    public TableColumn<CustomerLog,Long> discount;
    public TableColumn<CustomerLog, LogStatus> status;
    public TableColumn<CustomerLog,String> productName;
    public TableColumn<ItemInLog,Long> initialPrice;
    public TableColumn<ItemInLog,String> offPercent;
    public TableColumn<ItemInLog,Integer> count;
    public Label phoneNumber;
    public TextField address;
    public TableView<CustomerLog> customerLogs;
    public TableColumn<CustomerLog,String> customerName;
    public Button deliverButton;
    public Label alertDeliver;
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
        customerLogs.getItems().clear();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        status.setCellValueFactory(new PropertyValueFactory<>("logStatus"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discountAmount"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        initialPrice.setCellValueFactory(new PropertyValueFactory<>("initialPrice"));
        count.setCellValueFactory(new PropertyValueFactory<>("count"));
        offPercent.setCellValueFactory(new PropertyValueFactory<>("salePercentForTable"));

        customerLogs.getItems().addAll(Constants.managerController.getAllCustomerLogs());

        customerLogs.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
            itemInLog.setItems(FXCollections.observableList(newValue.getAllItems()));
            address.setText(newValue.getAddress());
            phoneNumber.setText(newValue.getPhoneNumber());
            if(newValue.isOnlyFile())
            deliverButton.setDisable(true);
            else
                deliverButton.setDisable(false);
        });



    }

    public void deliver(ActionEvent actionEvent) {
        if(customerLogs.getSelectionModel().getSelectedItem().getLogStatus().equals(LogStatus.SENT)) {
            alertDeliver.setStyle("-fx-text-fill: red");
            alertDeliver.setText("It is delivered!");
        }
        else{
            Constants.managerController.setLogSent(customerLogs.getSelectionModel().getSelectedItem().getId());
            fill();
            alertDeliver.setText("Sent successfully");
            alertDeliver.setStyle("-fx-text-fill: green");
        }
    }
}
