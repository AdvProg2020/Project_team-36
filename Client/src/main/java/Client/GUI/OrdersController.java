package Client.GUI;

import Client.Models.CustomerLog;
import Client.Models.Customer;
import Models.ItemInLog;
import Models.LogStatus;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;

public class OrdersController {
    @FXML
    private TableColumn<Client.Models.CustomerLog, Integer> id;
    @FXML
    private TableColumn<Client.Models.CustomerLog, Date> date;
    @FXML
    private TableColumn<Client.Models.CustomerLog, Long> totalPrice;
    @FXML
    private TableColumn<Client.Models.CustomerLog, Long> totalPayable;
    @FXML
    private TableColumn<Client.Models.CustomerLog, LogStatus> status;
    @FXML
    private TableColumn<ItemInLog, String> productName;
    @FXML
    private TableColumn<ItemInLog, String> sellerUsername;
    @FXML
    private TableColumn<ItemInLog, Long> initialPrice;
    @FXML
    private TableColumn<ItemInLog, Long> currentPrice;
    @FXML
    private TableColumn<ItemInLog, Integer> count;
    @FXML
    private TableView<CustomerLog> customerLog;
    @FXML
    private TableView<ItemInLog> itemInLog;

    public void fill() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalPayable.setCellValueFactory(new PropertyValueFactory<>("totalPayable"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        status.setCellValueFactory(new PropertyValueFactory<>("logStatus"));

        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        sellerUsername.setCellValueFactory(new PropertyValueFactory<>("sellerUsername"));
        initialPrice.setCellValueFactory(new PropertyValueFactory<>("initialPrice"));
        currentPrice.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        count.setCellValueFactory(new PropertyValueFactory<>("count"));

        customerLog.setItems(FXCollections.
                observableList(((Customer) Constants.globalVariables.getLoggedInUser()).getAllLogs()));

        customerLog.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->
                itemInLog.setItems(FXCollections.observableList(newValue.getAllItems())));
    }

}
