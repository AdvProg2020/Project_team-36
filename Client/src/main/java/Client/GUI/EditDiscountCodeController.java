package Client.GUI;

import Client.Controllers.ManagerController;
import Client.Models.Discount;
import Client.Models.Customer;
import Client.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditDiscountCodeController extends ManagerProfileController implements Initializable {


    @FXML private Button editButton;
    @FXML private Spinner<Integer> repetitionSpinner;
    @FXML private TextField limit;
    @FXML private DatePicker endDate;
    @FXML private DatePicker startDate;
    @FXML private Spinner<Integer> percentSpinner;
    @FXML private ImageView profilePicture;
    @FXML private Label usernameLabel;
    @FXML private Label codeLabel;
    @FXML private Label limitError;
    @FXML private Button removeCustomerButton;
    @FXML private Button addCustomerButton;
    @FXML private TableColumn<?, ?> customersIncludedColumn;
    @FXML private TableColumn<?, ?> availableCustomersColumn;
    @FXML private TableView<Customer> availableCustomersTable;
    @FXML private TableView<Customer> customersIncludedTable;
    private final Discount discountToEdit = Constants.managerController.getDiscountToEdit();
    private ArrayList<Customer> selectedCustomers = new ArrayList<>();

    @Override
    public void initialize(int id) throws IOException {
        User user;
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        } else {
            user = Constants.globalVariables.getLoggedInUser();
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

        customersIncludedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        availableCustomersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setAvailableCustomers();
        setValues();

    }

    private void setAvailableCustomers() {

        ArrayList<Customer> availableCustomers = Constants.managerController.getCustomersWithoutThisCode(discountToEdit.getId());
        List<Customer> includedCustomers = discountToEdit.getCustomersIncluded();

        availableCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        customersIncludedColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        availableCustomersTable.getItems().addAll(availableCustomers);
        customersIncludedTable.getItems().addAll(includedCustomers);

        selectedCustomers.addAll(includedCustomers);
    }

    public void removeAction() {
        TableView.TableViewSelectionModel<Customer> selectedUser = customersIncludedTable.getSelectionModel();

        if (selectedUser.isEmpty()) {
            return;
        }

        Customer customer = selectedUser.getSelectedItem() ;
        Constants.managerController.removeCustomersIncludedForDiscount(customer,discountToEdit.getId());
        selectedCustomers.remove(customer);
        customersIncludedTable.getItems().remove(customer);
        availableCustomersTable.getItems().add(customer);
        Constants.customerController.removeDiscount(discountToEdit.getId(), customer.getUsername());
    }

    public void addAction(){
        TableView.TableViewSelectionModel<Customer> selectedUser = availableCustomersTable.getSelectionModel();

        if (selectedUser.isEmpty()) {
            return;
        }

        Customer customer = selectedUser.getSelectedItem() ;
        Constants.managerController.setCustomersIncludedForDiscount(customer,discountToEdit.getId());
        availableCustomersTable.getItems().remove(customer);
        customersIncludedTable.getItems().add(customer);
        selectedCustomers.add(customer);
        Constants.customerController.setDiscountForCustomer(discountToEdit.getId(), customer.getUsername());
    }

    private void setValues() {
        codeLabel.setText("Discount Code: " + discountToEdit.getId());
        startDate.setValue(new Date(discountToEdit.getStartTime().getTime()).toLocalDate());
        endDate.setValue(new Date(discountToEdit.getEndTime().getTime()).toLocalDate());
        limit.setText(Long.toString(discountToEdit.getDiscountLimit()));
        repetitionSpinner.getValueFactory().setValue(discountToEdit.getRepetitionForEachUser());
        percentSpinner.getValueFactory().setValue(discountToEdit.getDiscountPercentForTable());
    }

    public void editDiscountCode() {

        ManagerController managerController = Constants.managerController;
        updateCustomersIncluded();

        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if(limit.getText()==null || !limit.getText().matches("\\d+")){
            limitError.setVisible(true);
            return;
        }

        int limit = Integer.parseInt(this.limit.getText());
        int percent = percentSpinner.getValue();
        int repetition = repetitionSpinner.getValue();
        java.util.Date startDate = java.sql.Date.valueOf(start);
        java.util.Date endDate = java.sql.Date.valueOf(end);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            managerController.editDiscountEndTime(dateFormat.format(endDate), discountToEdit);
            managerController.editDiscountStartTime(dateFormat.format(startDate), discountToEdit);
            managerController.editDiscountPercent(Integer.toString(percent),discountToEdit);
            managerController.editDiscountLimit(Integer.toString(limit), discountToEdit);
        } catch (ManagerController.InvalidDateException | ManagerController.InvalidRangeException | ManagerController.InvalidDiscountIdException e) {
            e.printStackTrace();
        }
        managerController.editDiscountRepetitionForEachUser(Integer.toString(repetition),discountToEdit);

        AlertBox.display("Done","Discount Code Was Edited SuccessFully");
    }

    private void updateCustomersIncluded(){

        ArrayList<Customer> temp = new ArrayList<>();

        for (Customer customer : discountToEdit.getCustomersIncluded()) {
            if(!selectedCustomers.contains(customer)){
                temp.add(customer);
                Constants.customerController.removeDiscount(discountToEdit.getId(), customer.getUsername());
            }
        }
        temp.forEach(customer -> Constants.managerController.removeCustomersIncludedForDiscount(customer,discountToEdit.getId()));
        temp.clear();

        for (Customer customer : selectedCustomers) {
            if(!discountToEdit.getCustomersIncluded().contains(customer)){
                temp.add(customer);
                Constants.customerController.setDiscountForCustomer(discountToEdit.getId(), customer.getUsername());
            }
        }
        temp.forEach(customer -> Constants.managerController.setCustomersIncludedForDiscount(customer,discountToEdit.getId()));

    }

}