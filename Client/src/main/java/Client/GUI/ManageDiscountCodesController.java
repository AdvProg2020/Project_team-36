package Client.GUI;

import Client.Controllers.ProductsController;
import Client.Models.Discount;
import Client.Controllers.ManagerController;
import Client.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;

public class ManageDiscountCodesController extends ManagerProfileController implements Initializable{

    @FXML private Label usernameLabel;
    @FXML private ImageView profilePicture;
    @FXML private TableView<Client.Models.Discount> allDiscountCodesTable;
    @FXML private TableColumn<? , ?> percentColumn;
    @FXML private TableColumn<?, ?> discountCodeColumn;
    @FXML private ComboBox sortName;
    @FXML private CheckBox isAscending;


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
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        ArrayList<Client.Models.Discount> allDiscountCodes = Constants.managerController.getAllDiscountCodes();
        setTheTable(allDiscountCodes);
    }

    public void openCreateDiscountCode() throws IOException {
        Constants.getGuiManager().open("CreateDiscountCode",Constants.globalVariables.getLoggedInUser().getUserId());

    }

    private void setTheTable(ArrayList<Client.Models.Discount> allDiscountCodes){
        allDiscountCodesTable.getItems().clear();
        allDiscountCodesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("discountPercentForTable"));
        discountCodeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allDiscountCodesTable.getItems().addAll(allDiscountCodes);
    }

    public void removeAction(){
        TableView.TableViewSelectionModel<Client.Models.Discount> selectedDiscount = allDiscountCodesTable.getSelectionModel();

        if (selectedDiscount.isEmpty()) {
            return;
        }

        Client.Models.Discount toBeRemoved = selectedDiscount.getSelectedItem();
        try {
            Constants.managerController.removeDiscount(Integer.toString(toBeRemoved.getId()));
        } catch (ManagerController.InvalidDiscountIdException e) {
            e.printStackTrace();
        }
        allDiscountCodesTable.getItems().remove(toBeRemoved);
    }

    public void viewAction() throws IOException {
        TableView.TableViewSelectionModel<Client.Models.Discount> selectedDiscount = allDiscountCodesTable.getSelectionModel();

        if (selectedDiscount.isEmpty()) {
            return;
        }

        Client.Models.Discount toBeViewed = selectedDiscount.getSelectedItem();
        Constants.managerController.setDiscountToView(toBeViewed);
        Constants.getGuiManager().open("ViewDiscountCode",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void editAction() throws IOException {
        TableView.TableViewSelectionModel<Client.Models.Discount> selectedDiscount = allDiscountCodesTable.getSelectionModel();

        if (selectedDiscount.isEmpty()) {
            return;
        }

        Client.Models.Discount toBeEdited = selectedDiscount.getSelectedItem();
        Constants.managerController.setDiscountToEdit(toBeEdited);
        Constants.getGuiManager().open("EditDiscountCode",Constants.globalVariables.getLoggedInUser().getUserId());
    }


    public void sort() throws ProductsController.NoSortException {
        if(isAscending.isDisable())
         isAscending.setDisable(false);

        if(isAscending.isSelected()){
            ArrayList<Client.Models.Discount> discounts =Constants.managerController.sortDiscountCodes(sortName.getValue().toString(),"ascending");
            setTheTable(discounts);
        }
        else{
            ArrayList<Discount> discounts = Constants.managerController.sortDiscountCodes(sortName.getValue().toString(),"descending");
            setTheTable(discounts);
        }
    }
}
