package GUI;

import Models.Discount;
import Models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageDiscountCodesController extends ManagerProfileController implements Initializable{

    public Label usernameLabel;
    public ImageView profilePicture;
    public TableView<Discount> allDiscountCodesTable;
    public TableColumn<? , ?> percentColumn;
    public TableColumn<? , ?> viewColumn;
    public TableColumn<?, ?> discountCodeColumn;
    public TableColumn<?, ?> editColumn;
    public TableColumn<?, ?> removeColumn;
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

        ArrayList<Discount> allDiscountCodes = Constants.managerController.getAllDiscountCodes();
        setTheTable(allDiscountCodes);
    }

    public void openCreateDiscountCode(ActionEvent actionEvent) throws IOException {
        Constants.getGuiManager().open("CreateDiscountCode",Constants.globalVariables.getLoggedInUser().getUserId());

    }

    private void setTheTable(ArrayList<Discount> allDiscountCodes){
        Discount.setManageDiscountCodesController(this);
        allDiscountCodesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("discountPercentForTable"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("viewHyperlink"));
        discountCodeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        editColumn.setCellValueFactory(new PropertyValueFactory<>("editHyperlink"));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>("removeHyperlink"));
        allDiscountCodesTable.getItems().addAll(allDiscountCodes);

    }

    public void removeAction(Discount discount){
        allDiscountCodesTable.getItems().remove(discount);
    }

    public void viewAction(Discount discount){

    }

    public void editAction(Discount discount){

    }

}
