package GUI;

import Models.Customer;
import Models.Discount;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Objects;

public class CreateDiscountCodeController extends ManagerProfileController implements Initializable {
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
    private final ArrayList<CheckBox> customersCheckBoxes = new ArrayList<>();

    @Override
    public void initialize(int id) throws IOException {
        this.user = User.getUserById(id);
        if (!Constants.globalVariables.getLoggedInUser().equals(user)) {
            Constants.getGuiManager().back();
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

        setAvailableCustomers();

        percentSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            spinnerListener(percentSpinner);
        });

        repetitionSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            spinnerListener(repetitionSpinner);
        });
    }

    private void setAvailableCustomers(){
        VBox vBox = new VBox();
        customersIncluded.setContent(vBox);
        for (Customer customer : Customer.getAllCustomers()) {
            CheckBox checkBox = new CheckBox(customer.getUsername());
            checkBox.setPrefSize(120, 25);
            checkBox.setDisable(false);
            vBox.getChildren().add(checkBox);
            customersCheckBoxes.add(checkBox);
            checkBox.setOnAction(actionEvent -> {
                for (CheckBox filterCheckBox : customersCheckBoxes) {
                    if (filterCheckBox.isSelected()) {
                        selectedCustomers.add((Customer)Customer.getUserByUsername(filterCheckBox.getText()));
                    }
                }
            });
        }
    }

    public void createDiscountCode(){

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

        this.endDate.setValue(null);
        this.startDate.setValue(null);
        selectedCustomers.clear();
        repetitionSpinner.getValueFactory().setValue(0);
        percentSpinner.getValueFactory().setValue(0);
        this.limit.setText("");
        for (CheckBox customersCheckBox : customersCheckBoxes) {
            customersCheckBox.setSelected(false);
        }
        limitError.setVisible(false);
        dateError.setVisible(false);
    }

    private void spinnerListener(Spinner<Integer> spinner) {
        if (!spinner.isEditable()) return;
        try {
            String text = spinner.getEditor().getText();
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            percentSpinner.getValueFactory().setValue(0);
            repetitionSpinner.getValueFactory().setValue(0);
        }
    }
}
