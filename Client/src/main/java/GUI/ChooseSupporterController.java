package GUI;

import Controllers.ChatsController;
import Models.Supporter;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;

public class ChooseSupporterController implements Initializable {

    @FXML private TableView<Supporter> supportersTable;
    @FXML private TableColumn<Supporter, ImageView> profilePictureColumn;
    @FXML private TableColumn<Supporter, String> usernameColumn;
    private final ChatsController chatsController = new ChatsController();

    @Override
    public void initialize(int id) throws IOException {
        supportersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        supportersTable.setPlaceholder(new Label("there is no online supporter right now!"));

        ArrayList<Supporter> onlineSupporters = new ArrayList<>();
        //todo gereftan e hame user haie online
        for (User user : ) {
            if(user instanceof Supporter){
                onlineSupporters.add((Supporter)user);
            }
        }

        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProfilePicture"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        supportersTable.getItems().addAll(onlineSupporters);

    }

    public void openChatRoom() throws IOException {
        TableView.TableViewSelectionModel<Supporter> selected = supportersTable.getSelectionModel();

        if (selected.isEmpty()) {
            return;
        }

        Supporter selectedSupporter = selected.getSelectedItem();
        int chatId = chatsController.createNewChatRoom(selectedSupporter, Constants.globalVariables.getLoggedInUser());
        Constants.getGuiManager().open("ChatRoom",chatId);
    }
}
