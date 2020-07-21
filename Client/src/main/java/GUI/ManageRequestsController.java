package GUI;

import Controllers.ManagerController;
import Controllers.ProductsController;
import Models.Request;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageRequestsController extends ManagerProfileController implements Initializable {

    @FXML private TableColumn<?, ?> typeColumn;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableView<Request> allRequestsTable;
    @FXML private ImageView profilePicture;
    @FXML private Label usernameLabel;
    @FXML private CheckBox isAscending;
    @FXML private ComboBox sortName;
    private User user;
    private static Request chosenRequest;

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

        ArrayList<Request> allRequests = Constants.managerController.getAllRequests();
        setTheTable(allRequests);
    }

    private void setTheTable(ArrayList<Request> allRequests) {
        allRequestsTable.getItems().clear();
        allRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        allRequestsTable.getItems().addAll(allRequests);

    }


    public void sort(ActionEvent actionEvent) throws ProductsController.NoSortException {
        if (isAscending.isDisable())
            isAscending.setDisable(false);
        ArrayList<Request> requests = Constants.managerController.sortRequests(sortName.getValue().toString(), isAscending.isSelected() ? "ascending" : "descending");
        setTheTable(requests);
    }

    public static Request getChosenRequest() {
        return chosenRequest;
    }

    public void viewAction() {
        TableView.TableViewSelectionModel<Request> selectedRequest = allRequestsTable.getSelectionModel();

        if (selectedRequest.isEmpty()) {
            return;
        }

        Request toBeViewed = selectedRequest.getSelectedItem();
        try {
            chosenRequest = toBeViewed;
            Constants.getGuiManager().open("RequestDetailsMenu", user.getUserId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptAction() {
        TableView.TableViewSelectionModel<Request> selectedRequest = allRequestsTable.getSelectionModel();

        if (selectedRequest.isEmpty()) {
            return;
        }

        Request toBeAccepted = selectedRequest.getSelectedItem();
        Constants.managerController.acceptRequest(toBeAccepted.getRequestId());
        allRequestsTable.getItems().remove(toBeAccepted);
    }

    public void declineAction() {
        TableView.TableViewSelectionModel<Request> selectedRequest = allRequestsTable.getSelectionModel();

        if (selectedRequest.isEmpty()) {
            return;
        }

        Request toBeDeclined = selectedRequest.getSelectedItem();
        try {
            Constants.managerController.declineRequest(toBeDeclined.getRequestId());
        } catch (ManagerController.InvalidRequestIdException e) {
            e.printStackTrace();
        }
        allRequestsTable.getItems().remove(toBeDeclined);
    }
}
