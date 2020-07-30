package Client.GUI;

import Client.Models.Request;
import Client.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class RequestDetailsController extends ManagerProfileController implements Initializable{

    public Label usernameLabel;
    public ImageView profilePicture;
    @FXML private TextArea information;
    @FXML private Label status;
    @FXML private Label requestType;
    @FXML private Label requestId;

    @Override
    public void initialize(int id) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        }
        User user = Constants.globalVariables.getLoggedInUser();
        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        Request request = ManageRequestsController.getChosenRequest();
        status.setText(request.getStatus().toString());
        information.setText(request.getPendableRequest().toString());
        requestType.setText(request.getType());
        requestId.setText(Integer.toString(request.getRequestId()));
    }
}
