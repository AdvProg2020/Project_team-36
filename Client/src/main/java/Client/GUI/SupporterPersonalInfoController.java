package Client.GUI;

import Client.Controllers.EntryController;
import Client.Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SupporterPersonalInfoController implements Initializable {

    @FXML private Label usernameLabel;
    @FXML private ImageView profilePicture;
    @FXML private ScrollPane scrollPane;
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

        showPersonalInfo();
    }



    public void showPersonalInfo() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PersonalInfo.fxml"));
        Parent parent = fxmlLoader.load();
        PersonalInfoController personalInfoController = fxmlLoader.getController();
        personalInfoController.initialize(user.getUserId());
        scrollPane.setContent(parent);
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }
    public void logout() throws EntryController.NotLoggedInException, IOException {
        Constants.getGuiManager().logout();
    }


    public void openChatRoom() throws IOException {
//        Timer t = new Timer( );
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 1000,5000);
        Constants.getGuiManager().open("ChatRoom",-1);
    }
}
