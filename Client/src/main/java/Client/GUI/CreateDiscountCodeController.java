package Client.GUI;

import Client.Controllers.DiscountController;
import Client.Models.Customer;
import Client.Models.User;
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
    @FXML
    private Button createButton;
    @FXML
    private TableView<Customer> customersIncludedTable;
    @FXML
    private TableColumn<? extends Object, ? extends Object> customersIncludedColumn;
    @FXML
    private TableView<Customer> availableCustomersTable;
    @FXML
    private TableColumn<? extends Object, ? extends Object> availableCustomersColumn;
    @FXML
    private Button removeCustomerButton;
    @FXML
    private Button addCustomerButton;
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
    private DiscountController newDiscount = new DiscountController();

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
        profilePicture.setImage(user.getProfilePicture(150, 150).getImage());


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

        ArrayList<Customer> availableCustomers = newDiscount.getAllCustomers();

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

    public void addAction() {
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
            if (newValue.matches("[^\\d]+"))
                startDate.getEditor().setText(startDate.getEditor().getText().replaceAll("[^\\d]", ""));
        });

        endDate.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches("[^\\d]+"))
                endDate.getEditor().setText(endDate.getEditor().getText().replaceAll("[^\\d]", ""));
        });
    }

    public void createDiscountCode() throws IOException {

        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (start == null || end == null) {
            dateError.setVisible(true);
            return;
        }

        if (limit.getText() == null || !limit.getText().matches("\\d+")) {
            limitError.setVisible(true);
            return;
        }

        int limit = Integer.parseInt(this.limit.getText());
        int percent = percentSpinner.getValue();
        int repetition = repetitionSpinner.getValue();
        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end);

        selectedCustomers.forEach(customer -> {
            try {
                newDiscount.setCustomersForDiscountCode(customer.getUsername());
            } catch (DiscountController.InvalidUsernameException | DiscountController.CustomerAlreadyAddedException e) {
                e.printStackTrace();
            }
        });
        newDiscount.setStartTime(startDate);
        try {
            newDiscount.setEndTime(endDate);
        } catch (DiscountController.EndDateBeforeStartDateException | DiscountController.EndDatePassedException e) {
            e.printStackTrace();
        }
        newDiscount.setDiscountPercent(percent * 0.01);
        newDiscount.setDiscountLimit(limit);
        newDiscount.setRepetitionForEachUser(repetition);

        newDiscount.finalizeTheNewDiscountCode();

        AlertBox.display("Done", "Discount Code Was Created SuccessFully");
        Constants.getGuiManager().reopen();
    }

}
