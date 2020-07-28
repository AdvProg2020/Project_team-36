package Client.GUI;

import Client.Controllers.EntryController;
import Client.Controllers.ProductsController;
import Client.Models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersController extends ManagerProfileController implements Initializable{

    public TableColumn<?,?> online;
    @FXML private ImageView profilePicture;
    @FXML private TableView<User> allUsersTable;
    @FXML private TableColumn<Object, Object> profilePictureColumn;
    @FXML private TableColumn<?, ?> usernameColumn;
    @FXML private TableColumn<?, ?> roleColumn;
    @FXML private Label usernameLabel;
    @FXML private ComboBox<String> sortName;
    @FXML private CheckBox isAscending;
    private User user;
    private Thread updateThread ;
    private User selectedTableItem;

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
        this.updateThread = update();
    }

    private void setTheTable(ArrayList<User> allUsers){
        allUsersTable.getItems().clear();
        online.setCellValueFactory(new PropertyValueFactory<>("online"));
        allUsersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProfilePicture"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        allUsersTable.getItems().addAll(allUsers);

    }

    public void removeAction() {
        User toBeRemoved = allUsersTable.getSelectionModel().getSelectedItem();
        if (toBeRemoved ==null) {
            return;
        }
        if(toBeRemoved.equals(user)){
            AlertBox.display("remove can't be done","you are removing your own account!");
            return;
        }
        Constants.managerController.deleteUser(toBeRemoved);
        allUsersTable.getItems().remove(toBeRemoved);
    }

    public void viewUserAction() throws IOException {
        User toBeViewed = allUsersTable.getSelectionModel().getSelectedItem();
        if (toBeViewed==null) {
            return;
        }
        Constants.userController.setUserToView(toBeViewed);
        intteruptUpdate();
        Constants.getGuiManager().open("ViewUser",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void openCreateNewManager() throws IOException {
        intteruptUpdate();
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
        intteruptUpdate();
        Constants.getGuiManager().open("AddNewSupporter",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    @Override
    public void back() throws IOException {
        intteruptUpdate();
        super.back();
    }

    @Override
    public void logout() throws EntryController.NotLoggedInException, IOException {
        intteruptUpdate();
        super.logout();
    }

    public Thread update(){
      Thread thread =   new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                   break;
                }
                selectedTableItem = allUsersTable.getSelectionModel().getSelectedItem();
                ArrayList<User> allUsers = Constants.managerController.getAllUsers();
                setTheTable(allUsers);
                if(selectedTableItem!= null){
                    for (User allUser : allUsers) {
                        if(allUser.getUserId()==selectedTableItem.getUserId()) {
                            allUsersTable.getSelectionModel().select(allUser);
                            return;
                        }
                    }
                }
            }

        });
      thread.start();
      return thread;
    }

    @Override
    public void openAllProducts() throws IOException {
        intteruptUpdate();
        super.openAllProducts();
    }

    @Override
    public void openAllUsers() throws IOException {
        intteruptUpdate();
        super.openAllUsers();
    }

    @Override
    public void openCategories() throws IOException {
        intteruptUpdate();
        super.openCategories();
    }

    @Override
    public void openCustomerLogs() throws IOException {
        intteruptUpdate();
        super.openCustomerLogs();
    }

    @Override
    public void openDiscountCodes() throws IOException {
      intteruptUpdate();
        super.openDiscountCodes();
    }

    @Override
    public void openFinancialManagements() throws IOException {
        intteruptUpdate();
        super.openFinancialManagements();
    }

    @Override
    public void openPersonalInfo() throws IOException {
        intteruptUpdate();
        super.openPersonalInfo();
    }

    @Override
    public void openRequests() throws IOException {
        intteruptUpdate();
        super.openRequests();
    }

    private void intteruptUpdate(){
        updateThread.interrupt();
    }
}
