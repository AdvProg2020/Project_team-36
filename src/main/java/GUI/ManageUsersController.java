package GUI;

import Models.Category;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageUsersController extends ManagerProfileController implements Initializable{


    public ImageView profilePicture;
    public TableView<User> allUsersTable;
    public TableColumn<Object, Object> profilePictureColumn;
    public TableColumn<?, ?> usernameColumn;
    public TableColumn<?, ?> roleColumn;
    public Label usernameLabel;
    public TableColumn<?, ?> viewColumn;
    private User user;

    @Override
    public void initialize(int id) throws IOException {

        this.user = User.getUserById(id);
        if (!Constants.globalVariables.getLoggedInUser().equals(user)) {
            Constants.getGuiManager().back();
        }
        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        ArrayList<User> allUsers = Constants.managerController.getAllUsers();
        setTheTable(allUsers);
    }

    private void setTheTable(ArrayList<User> allUsers){
        User.setManageUsersController(this);
        allUsersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProfilePicture"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("viewHyperlink"));
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

        toBeRemoved.setUserDeleted();
        User.removeUsername(toBeRemoved.getUsername());
        User.updateAllUsers();
        allUsersTable.getItems().remove(toBeRemoved);
    }

    public void viewUserAction() throws IOException {
        Constants.getGuiManager().open("ViewUser",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void openCreateNewManager(ActionEvent actionEvent) {
    }
}
