package GUI;


import Models.Customer;
import Models.Discount;
import Models.User;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class ViewDiscountCodeController extends ManagerProfileController implements Initializable {


    public Label usernameLabel;
    public ImageView profilePicture;
    public DatePicker startDate;
    public DatePicker endDate;
    public TextField limit;
    public ScrollPane customersIncluded;
    public Label codeLabel;
    public TextField repetition;
    public TextField percent;
    private User user;

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
                setDisable(empty || date.compareTo(startDate.getValue()) < 0 || date.compareTo(startDate.getValue()) > 0);
            }
        });

        endDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(endDate.getValue()) < 0 || date.compareTo(endDate.getValue()) > 0);
            }
        });

        setValues();

    }

    private void setValues(){
        Discount discount = managerController.getDiscountToView();

        codeLabel.setText("Discount Code: " + discount.getId());
        startDate.setValue(new Date(discount.getStartTime().getTime()).toLocalDate());
        endDate.setValue(new Date(discount.getEndTime().getTime()).toLocalDate());
        limit.setText(Long.toString(discount.getDiscountLimit()));
        repetition.setText(Integer.toString(discount.getRepetitionForEachUser()));
        percent.setText(Integer.toString(discount.getDiscountPercentForTable()));

        VBox vBox = new VBox();
        customersIncluded.setContent(vBox);
        for (Customer customer : discount.getCustomersIncluded()) {
            Label customerUsername = new Label(customer.getUsername());
            vBox.getChildren().add(customerUsername);
        }

    }


}
