package GUI;

import Models.Product;
import Models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class ManageUsersController extends ManagerProfileController implements Initializable{


    public ImageView profilePicture;
    public TableView allUsersTable;
    public TableColumn numberColumn;
    public TableColumn profilePictureColumn;
    public TableColumn usernameColumn;
    public TableColumn roleColumn;
    public TableColumn removeColumn;
    public Label usernameLabel;
    private User manager;

    @Override
    public void initialize(int id) {
//        manager = Constants.loggedInUser.getLoggedInUser();
//        Image profile = new Image(getClass().getResource(manager.getProfilePictureUrl()).toExternalForm(),150,150,false,false);
//        profilePicture.setImage(profile);
//        usernameLabel.setText(manager.getUsername());

        allUsersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        allUsersTable.setStyle("-fx-alignment: CENTER-RIGHT;");
        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProfilePicture"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>("removeHyperlink"));

        ArrayList<User> allUsers = Constants.managerController.getAllUsers();
        allUsersTable.getItems().addAll(allUsers);

    }

    public void openCreateNewManager(ActionEvent actionEvent) {
    }
}
