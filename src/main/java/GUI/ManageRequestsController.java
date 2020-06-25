package GUI;

import Models.Discount;
import Models.Request;
import Models.User;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageRequestsController extends ManagerProfileController implements Initializable {

    public TableColumn<?, ?> declineColumn;
    public TableColumn<?, ?> acceptColumn;
    public TableColumn<?, ?> viewColumn;
    public TableColumn<?, ?> typeColumn;
    public TableColumn<?, ?> idColumn;
    public TableView<Request> allRequestsTable;
    public ImageView profilePicture;
    public Label usernameLabel;
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

        ArrayList<Request> allRequests = Constants.managerController.getAllRequests();
        setTheTable(allRequests);
    }

    private void setTheTable(ArrayList<Request> allRequests){
        Request.setManageRequestsController(this);
        allRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("viewHyperLink"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        acceptColumn.setCellValueFactory(new PropertyValueFactory<>("acceptHyperlink"));
        declineColumn.setCellValueFactory(new PropertyValueFactory<>("declineHyperlink"));
        allRequestsTable.getItems().addAll(allRequests);
    }


    public void removeAction(Request request){
        allRequestsTable.getItems().remove(request);
    }

    public void viewAction(Request request){

    }

}
