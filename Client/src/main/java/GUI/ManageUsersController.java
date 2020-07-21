package GUI;

import Controllers.ProductsController;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageUsersController extends ManagerProfileController implements Initializable{


    @FXML private ImageView profilePicture;
    @FXML private TableView<User> allUsersTable;
    @FXML private TableColumn<Object, Object> profilePictureColumn;
    @FXML private TableColumn<?, ?> usernameColumn;
    @FXML private TableColumn<?, ?> roleColumn;
    @FXML private Label usernameLabel;
    @FXML private ComboBox sortName;
    @FXML private CheckBox isAscending;
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

        ArrayList<User> allUsers = Constants.managerController.getAllUsers();
        setTheTable(allUsers);
    }

    private void setTheTable(ArrayList<User> allUsers){
        allUsersTable.getItems().clear();
        allUsersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProfilePicture"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        allUsersTable.getItems().addAll(allUsers);

    }

    public void removeAction() {
        TableView.TableViewSelectionModel<User> selectedUser = allUsersTable.getSelectionModel();

        if (selectedUser.isEmpty()) {
            return;
        }

        User toBeRemoved = selectedUser.getSelectedItem();

        if(toBeRemoved.equals(user)){
            AlertBox.display("remove can't be done","you are removing your own account!");
            return;
        }

        Constants.managerController.deleteUser(toBeRemoved);
        allUsersTable.getItems().remove(toBeRemoved);
    }

    public void viewUserAction() throws IOException {
        TableView.TableViewSelectionModel<User> selectedUser = allUsersTable.getSelectionModel();

        if (selectedUser.isEmpty()) {
            return;
        }

        User toBeViewed = selectedUser.getSelectedItem();
        Constants.userController.setUserToView(toBeViewed);
        Constants.getGuiManager().open("ViewUser",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void openCreateNewManager() throws IOException {
        Constants.getGuiManager().open("CreateNewManager",Constants.globalVariables.getLoggedInUser().getUserId());
    }


    public void sort() throws ProductsController.NoSortException {
        isAscending.setDisable(false);
        if(isAscending.isSelected()){
            ArrayList<User> users =Constants.managerController.sortUsers(sortName.getValue().toString(),"ascending");
            setTheTable(users);
        }
        else{
            ArrayList<User> users = Constants.managerController.sortUsers(sortName.getValue().toString(),"descending");
            setTheTable(users);
        }
    }

    public void openCreateNewSupporter(ActionEvent actionEvent) throws IOException {
        Constants.getGuiManager().open("AddNewSupporter",Constants.globalVariables.getLoggedInUser().getUserId());
    }
}
