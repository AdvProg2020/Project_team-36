package GUI;

import Controllers.NewOffController;
import Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddOffController extends SellerPersonalInfoController implements Initializable {


    @FXML
    private Button addCustomerButton;
    @FXML
    private Button removeCustomerButton;
    @FXML
    private TableColumn<Product,?> availableProductsColumn;
    @FXML
    private TableView<Product> availableProductsTable;
    @FXML
    private TableColumn<Product,?> productsIncludedColumn;
    @FXML
    private TableView<Product> productsIncludedTable;
    @FXML
    private DatePicker endDate;
    @FXML
    private DatePicker startDate;
    @FXML
    private Spinner percentSpinner;
    @FXML
    private Label dateError;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView profilePicture;
    private User user;
    private ArrayList<Product> selectedProducts = new ArrayList<>();
    NewOffController newOff = new NewOffController();

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

        startDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                LocalDate end = endDate.getValue();
                if (end == null) {
                    setDisable(empty || date.compareTo(today) < 0);
                } else {
                    setDisable(empty || date.compareTo(today) < 0 || date.compareTo(end) > 0);
                }
            }
        });

        endDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                LocalDate start = startDate.getValue();
                setDisable(empty || date.compareTo(Objects.requireNonNullElse(start, today)) < 0);
            }
        });

        setAvailableProducts();
    }

    private void setAvailableProducts(){
        List<Product> availableProducts = ((Seller)user).getAllProducts();

        availableProductsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsIncludedColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        availableProductsTable.getItems().addAll(availableProducts);
    }

    public void removeAction(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel<Product> selectedProduct = productsIncludedTable.getSelectionModel();

        if (selectedProduct.isEmpty()) {
            return;
        }

        Product toBeRemoved = selectedProduct.getSelectedItem();
        selectedProducts.remove(toBeRemoved);
        productsIncludedTable.getItems().remove(toBeRemoved);
        availableProductsTable.getItems().add(toBeRemoved);
    }

    public void addAction(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel<Product> selectedProduct = availableProductsTable.getSelectionModel();

        if (selectedProduct.isEmpty()) {
            return;
        }

        Product toBeAdded = selectedProduct.getSelectedItem();
        selectedProducts.add(toBeAdded);
        availableProductsTable.getItems().remove(toBeAdded);
        productsIncludedTable.getItems().add(toBeAdded);
    }


    public void createOff(ActionEvent actionEvent) throws IOException {

        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (start==null || end==null ){
            dateError.setVisible(true);
            return;
        }

        int percent = (int) percentSpinner.getValue();
        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end);

        selectedProducts.forEach(product -> {
            try {
                newOff.setProductsInSale(product.getProductId());
            } catch (NewOffController.InvalidProductIdException e) {
                e.printStackTrace();
            } });
        newOff.setStartTime(startDate);
        try {
            newOff.setEndTime(endDate);
        } catch (NewOffController.EndDateBeforeStartDateException | NewOffController.EndDatePassedException e) {
            e.printStackTrace(); }
        newOff.setSalePercent(percent*0.01);

        newOff.sendNewOffRequest();

        AlertBox.display("Done","Off Request Was Sent SuccessFully");
        Constants.getGuiManager().reopen();
    }
}

