package GUI;

import Controllers.ManagerController;
import Models.Customer;
import Models.Discount;
import Models.User;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class EditDiscountCodeController extends ManagerProfileController implements Initializable {


    public Button editButton;
    public Spinner<Integer> repetitionSpinner;
    public TextField limit;
    public DatePicker endDate;
    public DatePicker startDate;
    public Spinner<Integer> percentSpinner;
    public ImageView profilePicture;
    public Label usernameLabel;
    public Label codeLabel;
    public Label limitError;
    public Button removeCustomerButton;
    public Button addCustomerButton;
    public TableColumn<? extends Object, ? extends Object> customersIncludedColumn;
    public TableColumn<? extends Object, ? extends Object> availableCustomersColumn;
    public TableView<Customer> availableCustomersTable;
    public TableView<Customer> customersIncludedTable;
    private User user;
    private Discount discountToEdit = managerController.getDiscountToEdit();
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
        ArrayList<Customer> includedCustomers = discountToEdit.getCustomersIncluded();

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

        ArrayList<Customer> toBeRemoved = new ArrayList<>();
        Customer customer = selectedUser.getSelectedItem() ;
        toBeRemoved.add(customer);
        discountToEdit.removeCustomersIncluded(toBeRemoved);
        selectedCustomers.remove(customer);
        customersIncludedTable.getItems().remove(customer);
        availableCustomersTable.getItems().add(customer);
        customer.removeDiscount(discountToEdit);
    }

    public void addAction(){
        TableView.TableViewSelectionModel<Customer> selectedUser = availableCustomersTable.getSelectionModel();

        if (selectedUser.isEmpty()) {
            return;
        }

        ArrayList<Customer> toBeAdded = new ArrayList<>();
        Customer customer = selectedUser.getSelectedItem() ;
        toBeAdded.add(customer);
        discountToEdit.setCustomersIncluded(toBeAdded);
        availableCustomersTable.getItems().remove(customer);
        customersIncludedTable.getItems().add(customer);
        selectedCustomers.add(customer);
        customer.setDiscountForCustomer(discountToEdit);
    }

    private void setValues() {
        Discount discount = Discount.getDiscountToEdit();

        codeLabel.setText("Discount Code: " + discount.getId());
        startDate.setValue(new Date(discount.getStartTime().getTime()).toLocalDate());
        endDate.setValue(new Date(discount.getEndTime().getTime()).toLocalDate());
        limit.setText(Long.toString(discount.getDiscountLimit()));
        repetitionSpinner.getValueFactory().setValue(discount.getRepetitionForEachUser());
        percentSpinner.getValueFactory().setValue(discount.getDiscountPercentForTable());


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

        discountToEdit.setEndTime(endDate);
        discountToEdit.setStartTime(startDate);
        discountToEdit.setDiscountPercent(percent*0.01);
        discountToEdit.setDiscountLimit(limit);
        managerController.editDiscountRepetitionForEachUser(Integer.toString(repetition),discountToEdit);

        AlertBox.display("Done","Discount Code Was Edited SuccessFully");
    }

    private void updateCustomersIncluded(){

        ArrayList<Customer> temp = new ArrayList<>();

        for (Customer customer : discountToEdit.getCustomersIncluded()) {
            if(!selectedCustomers.contains(customer)){
                temp.add(customer);
                customer.removeDiscount(discountToEdit);
            }
        }
        discountToEdit.removeCustomersIncluded(temp);
        temp.clear();

        for (Customer customer : selectedCustomers) {
            if(!discountToEdit.getCustomersIncluded().contains(customer)){
                temp.add(customer);
                customer.setDiscountForCustomer(discountToEdit);
            }
        }
        discountToEdit.setCustomersIncluded(temp);
    }

}
