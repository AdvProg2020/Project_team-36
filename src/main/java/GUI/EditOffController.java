package GUI;

import Models.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EditOffController extends SellerProfileController implements Initializable {

    public Label usernameLabel;
    public ImageView profilePicture;
    public Spinner percentSpinner;
    public DatePicker startDate;
    public DatePicker endDate;
    public Button createButton;
    public Label dateError;
    public TableView<Product> productsIncludedTable;
    public TableView<Product> availableProductsTable;
    public TableColumn<? extends Object, ? extends Object> availableProductsColumn;
    public TableColumn<? extends Object, ? extends Object> productsIncludedColumn;
    public Label idLabel;
    private User user;
    private ArrayList<Product> selectedProducts = new ArrayList<>();
    private Sale offToEdit = Sale.getOffToEdit();

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

        addTextFieldListener();
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

    private void addTextFieldListener() {
        startDate.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]+"))
                startDate.getEditor().setText(startDate.getEditor().getText().replaceAll("[^\\d]", ""));
        });

        endDate.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]+"))
                endDate.getEditor().setText(endDate.getEditor().getText().replaceAll("[^\\d]", ""));
        });
    }

    public void editOff(ActionEvent actionEvent) {
        Sale copiedOff = new Sale(offToEdit);

        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        if (start==null || end==null ){
            dateError.setVisible(true);
            return;
        }
        int percent = (int) percentSpinner.getValue();
        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end);

        copiedOff.setEndTime(endDate);
        copiedOff.setStartTime(startDate);
        copiedOff.setSalePercent(percent*0.01);
        copiedOff.setProductsInSale(selectedProducts);

        new Request(copiedOff,Status.TO_BE_EDITED);

        AlertBox.display("Done","Request To edit Off Was Sent SuccessFully");
    }
}
