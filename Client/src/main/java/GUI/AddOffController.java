package GUI;

import Models.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddOffController extends SellerPersonalInfoController implements Initializable {


    public Button addCustomerButton;
    public Button removeCustomerButton;
    public TableColumn<Product,?> availableProductsColumn;
    public TableView<Product> availableProductsTable;
    public TableColumn<Product,?> productsIncludedColumn;
    public TableView<Product> productsIncludedTable;
    public DatePicker endDate;
    public DatePicker startDate;
    public Spinner percentSpinner;
    public Label dateError;
    private User user;
    private ArrayList<Product> selectedProducts = new ArrayList<>();

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

        new Request(new Sale((Seller)user, selectedProducts, startDate, endDate, percent*0.01),Status.TO_BE_ADDED);

        AlertBox.display("Done","Off Request Was Sent SuccessFully");
        Constants.getGuiManager().reopen();
    }
}

