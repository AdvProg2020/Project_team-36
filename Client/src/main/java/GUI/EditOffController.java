package GUI;

import Controllers.SellerController;
import Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EditOffController extends SellerProfileController implements Initializable {

    @FXML private Label usernameLabel;
    @FXML private ImageView profilePicture;
    @FXML private Spinner<Integer> percentSpinner;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private Button createButton;
    @FXML private Label dateError;
    @FXML private TableView<Product> productsIncludedTable;
    @FXML private TableView<Product> availableProductsTable;
    @FXML private TableColumn<?, ?> availableProductsColumn;
    @FXML private TableColumn<?, ?> productsIncludedColumn;
    @FXML private Label idLabel;
    private User user;
    private ArrayList<Product> selectedProducts = new ArrayList<>();
    private final Sale offToEdit = Constants.sellerController.getOffToEdit();

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
        setValues();
    }

    private void setValues(){

        idLabel.setText("Off: " + offToEdit.getOffId());
        startDate.setValue(new java.sql.Date(offToEdit.getStartTime().getTime()).toLocalDate());
        endDate.setValue(new java.sql.Date(offToEdit.getEndTime().getTime()).toLocalDate());
        percentSpinner.getValueFactory().setValue(offToEdit.getSalePercentForTable());

    }

    private void setAvailableProducts(){
        selectedProducts.addAll(offToEdit.getProductsInSale());
        ArrayList<Product> availableProducts = new ArrayList<>(((Seller)user).getAllProducts());
        for (Product product : selectedProducts) {
            availableProducts.remove(product);
        }
        availableProductsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsIncludedColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        availableProductsTable.getItems().addAll(availableProducts);
        productsIncludedTable.getItems().addAll(selectedProducts);
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

    public void editOff() {
        Sale copiedOff = Constants.sellerController.getOffCopy(offToEdit);
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        if (start==null || end==null ){
            dateError.setVisible(true);
            return;
        }
        int percent = percentSpinner.getValue();
        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Constants.sellerController.editOffEndDate(dateFormat.format(endDate));
            Constants.sellerController.editOffStartDate(dateFormat.format(startDate));
            Constants.sellerController.editOffPercent(Integer.toString(percent));
            selectedProducts.forEach(product -> Constants.sellerController.addProductToOff(product));
            Constants.sellerController.finalizeAddingProducts();
            Constants.sellerController.sendEditOffRequest();
        } catch (SellerController.StartDateAfterEndDateException | SellerController.InvalidDateFormatException | SellerController.InvalidRangeException | SellerController.EndDateBeforeStartDateException e) {
            e.printStackTrace();
        }

        AlertBox.display("Done","Request To edit Off Was Sent SuccessFully");
    }
}
