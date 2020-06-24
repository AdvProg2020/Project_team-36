package GUI;

import Models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class ManageUsersController extends ManagerProfileController implements Initializable{


    public ImageView profilePicture;
    public TableView<User> allUsersTable;
    public TableColumn<Object, Object> profilePictureColumn;
    public TableColumn<?, ?> usernameColumn;
    public TableColumn<?, ?> roleColumn;
    public TableColumn<?, ?> removeColumn;
    public Label usernameLabel;
    private User manager;

    @Override
    public void initialize(int id) {
//        manager = Constants.loggedInUser.getLoggedInUser();
//        Image profile = new Image(getClass().getResource(manager.getProfilePictureUrl()).toExternalForm(),150,150,false,false);
//        profilePicture.setImage(profile);
//        usernameLabel.setText(manager.getUsername());

        ArrayList<User> allUsers = Constants.managerController.getAllUsers();
        setTheTable(allUsers);
    }

    private void setTheTable(ArrayList<User> allUsers){
        User.setManageUsersController(this);
        allUsersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProfilePicture"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>("removeHyperlink"));
        allUsersTable.getItems().addAll(allUsers);

    }

    public void removeAction(User user){
        allUsersTable.getItems().remove(user);
    }

    public void openCreateNewManager(ActionEvent actionEvent) {
    }
}
