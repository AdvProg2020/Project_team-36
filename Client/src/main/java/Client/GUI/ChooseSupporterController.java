package Client.GUI;

import Client.Controllers.ChatsController;
import Client.Models.Supporter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;

public class ChooseSupporterController implements Initializable {

    @FXML private TableView<Client.Models.Supporter> supportersTable;
    @FXML private TableColumn<Client.Models.Supporter, ImageView> profilePictureColumn;
    @FXML private TableColumn<Client.Models.Supporter, String> usernameColumn;
    private final ChatsController chatsController = new ChatsController();

    @Override
    public void initialize(int id) throws IOException {
        supportersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        supportersTable.setPlaceholder(new Label("there is no online supporter right now!"));

        ArrayList<Client.Models.Supporter> onlineSupporters = new ArrayList<>(Constants.customerController.getOnlineSupporters());

        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProfilePicture"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        supportersTable.getItems().addAll(onlineSupporters);

    }

    public void openChatRoom() throws IOException {
        TableView.TableViewSelectionModel<Client.Models.Supporter> selected = supportersTable.getSelectionModel();

        if (selected.isEmpty()) {
            return;
        }

        Supporter selectedSupporter = selected.getSelectedItem();
        int chatId = chatsController.createNewChatRoom(selectedSupporter, Constants.globalVariables.getLoggedInUser());
        Constants.getGuiManager().open("ChatRoom",chatId);
    }
}
