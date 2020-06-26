package GUI;

import Models.Customer;
import Models.Discount;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CreateDiscountCodeController extends ManagerProfileController implements Initializable {
    public Button createButton;
    public TableView<Customer> customersIncludedTable;
    public TableColumn<? extends Object, ? extends Object> customersIncludedColumn;
    public TableView<Customer> availableCustomersTable;
    public TableColumn<? extends Object, ? extends Object> availableCustomersColumn;
    public Button removeCustomerButton;
    public Button addCustomerButton;
    @FXML
    private Label dateError;
    @FXML
    private Label limitError;
    @FXML
    private Spinner<Integer> repetitionSpinner;
    @FXML
    private TextField limit;
    @FXML
    private Spinner<Integer> percentSpinner;
    @FXML
    private ScrollPane customersIncluded;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label usernameLabel;
    private User user;
    private ArrayList<Customer> selectedCustomers = new ArrayList<>();

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
        setAvailableCustomers();

    }


    private void setAvailableCustomers() {

        ArrayList<Customer> availableCustomers = Customer.getAllCustomers();

        availableCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        customersIncludedColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        availableCustomersTable.getItems().addAll(availableCustomers);

    }

    public void removeAction() {
        TableView.TableViewSelectionModel<Customer> selectedUser = customersIncludedTable.getSelectionModel();

        if (selectedUser.isEmpty()) {
            return;
        }

        Customer toBeRemoved = selectedUser.getSelectedItem();
        selectedCustomers.remove(toBeRemoved);
        customersIncludedTable.getItems().remove(toBeRemoved);
        availableCustomersTable.getItems().add(toBeRemoved);
    }

    public void addAction(){
        TableView.TableViewSelectionModel<Customer> selectedUser = availableCustomersTable.getSelectionModel();

        if (selectedUser.isEmpty()) {
            return;
        }

        Customer toBeAdded = selectedUser.getSelectedItem();
        selectedCustomers.add(toBeAdded);
        availableCustomersTable.getItems().remove(toBeAdded);
        customersIncludedTable.getItems().add(toBeAdded);
    }

    private void addTextFieldListener() {
        startDate.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (startDate.getEditor().getText().contains("[^\\d]"))
                startDate.getEditor().setText(startDate.getEditor().getText().replaceAll("[^\\d]", ""));
        });

        endDate.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (endDate.getEditor().getText().contains("[^\\d]"))
                endDate.getEditor().setText(endDate.getEditor().getText().replaceAll("[^\\d]", ""));
        });
    }

    public void createDiscountCode() throws IOException {

        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (start==null || end==null ){
            dateError.setVisible(true);
            return;
        }

        if(limit.getText()==null || !limit.getText().matches("\\d+")){
            limitError.setVisible(true);
            return;
        }

        int limit = Integer.parseInt(this.limit.getText());
        int percent = percentSpinner.getValue();
        int repetition = repetitionSpinner.getValue();
        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end);

        Discount discount = new Discount(startDate, endDate, percent*0.01, limit, repetition, selectedCustomers);
        discount.giveDiscountCodeToCustomers();

        AlertBox.display("Done","Discount Code Was Created SuccessFully");
        Constants.getGuiManager().reopen();
    }

}
