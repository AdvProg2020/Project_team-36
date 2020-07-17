package GUI;

import Controllers.ProductsController;
import Models.Discount;
import Models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageDiscountCodesController extends ManagerProfileController implements Initializable{

    public Label usernameLabel;
    public ImageView profilePicture;
    public TableView<Discount> allDiscountCodesTable;
    public TableColumn<? , ?> percentColumn;
    public TableColumn<?, ?> discountCodeColumn;
    public ComboBox sortName;
    public CheckBox isAscending;
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
        allDiscountCodesTable.getItems().clear();
        Discount.setManageDiscountCodesController(this);
        allDiscountCodesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("discountPercentForTable"));
        discountCodeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allDiscountCodesTable.getItems().addAll(allDiscountCodes);
    }

    public void removeAction(){
        TableView.TableViewSelectionModel<Discount> selectedDiscount = allDiscountCodesTable.getSelectionModel();

        if (selectedDiscount.isEmpty()) {
            return;
        }

        Discount toBeRemoved = selectedDiscount.getSelectedItem();
        managerController.removeDiscount(toBeRemoved.getId);
        allDiscountCodesTable.getItems().remove(discount);
    }

    public void viewAction() throws IOException {
        TableView.TableViewSelectionModel<Discount> selectedDiscount = allDiscountCodesTable.getSelectionModel();

        if (selectedDiscount.isEmpty()) {
            return;
        }

        Discount toBeViewed = selectedDiscount.getSelectedItem();
        managerController.setDiscountToView(toBeViewed);
        Constants.getGuiManager().open("ViewDiscountCode",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void editAction() throws IOException {
        TableView.TableViewSelectionModel<Discount> selectedDiscount = allDiscountCodesTable.getSelectionModel();

        if (selectedDiscount.isEmpty()) {
            return;
        }

        Discount toBeEdited = selectedDiscount.getSelectedItem();
        managerController.setDiscountToEdit(toBeEdited);
        Constants.getGuiManager().open("EditDiscountCode",Constants.globalVariables.getLoggedInUser().getUserId());
    }


    public void sort(ActionEvent actionEvent) throws ProductsController.NoSortException {
        if(isAscending.isDisable())
         isAscending.setDisable(false);

        if(isAscending.isSelected()){
            ArrayList<Discount> discounts =Constants.managerController.sortDiscountCodes(sortName.getValue().toString(),"ascending");
            setTheTable(discounts);
        }
        else{
            ArrayList<Discount> discounts = Constants.managerController.sortDiscountCodes(sortName.getValue().toString(),"descending");
            setTheTable(discounts);
        }
    }
}
