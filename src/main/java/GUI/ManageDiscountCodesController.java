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
    private User manager;


    @Override
    public void initialize(int id) throws IOException {

//        manager = Constants.loggedInUser.getLoggedInUser();
//        Image profile = new Image(getClass().getResource(manager.getProfilePictureUrl()).toExternalForm(),150,150,false,false);
//        profilePicture.setImage(profile);
//        usernameLabel.setText(manager.getUsername());
        ArrayList<Discount> allDiscountCodes = Constants.managerController.getAllDiscountCodes();
        setTheTable(allDiscountCodes);
    }

    public void openCreateDiscountCode(ActionEvent actionEvent) {

    }

    private void setTheTable(ArrayList<Discount> allDiscountCodes){
        Discount.setManageDiscountCodesController(this);
        allDiscountCodesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("discountPercent"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("viewHyperLink"));
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
