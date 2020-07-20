package GUI;

import Models.Sale;
import Models.Seller;
import Models.User;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.List;

public class ViewOffsController extends SellerProfileController implements Initializable {

    public TableColumn<?, ?> percentColumn;
    public TableColumn<?, ?> offIdColumn;
    public TableView<Sale> allOffsTable;
    public Label usernameLabel;
    public ImageView profilePicture;

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

        List<Sale> allOffs = ((Seller) user).getAllSales();
        setTheTable(allOffs);
    }

    private void setTheTable(List<Sale> allOffs){
        allOffsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("salePercentForTable"));
        offIdColumn.setCellValueFactory(new PropertyValueFactory<>("offId"));
        allOffsTable.getItems().addAll(allOffs);

    }

    public void viewAction() throws IOException {
        TableView.TableViewSelectionModel<Sale> selectedOff = allOffsTable.getSelectionModel();

        if (selectedOff.isEmpty()) {
            return;
        }

        Sale toBeViewed = selectedOff.getSelectedItem();
        Constants.sellerController.setOffToView(toBeViewed);
        Constants.getGuiManager().open("ViewEachOff",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void editAction() throws IOException {
        TableView.TableViewSelectionModel<Sale> selectedOff = allOffsTable.getSelectionModel();

        if (selectedOff.isEmpty()) {
            return;
        }

        Sale toBeEdited = selectedOff.getSelectedItem();
        Constants.sellerController.setOffToEdit(toBeEdited);
        Constants.getGuiManager().open("EditOff",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void removeAction() {
        TableView.TableViewSelectionModel<Sale> selectedOff = allOffsTable.getSelectionModel();

        if (selectedOff.isEmpty()) {
            return;
        }

        Sale toBeRemoved = selectedOff.getSelectedItem();
        Constants.sellerController.removeSale(toBeRemoved);
        allOffsTable.getItems().remove(toBeRemoved);
    }

    public void openCreateOff() throws IOException {
        Constants.getGuiManager().open("AddOff",Constants.globalVariables.getLoggedInUser().getUserId());
    }
}
