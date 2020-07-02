package GUI;

import Models.Request;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class RequestDetailsController extends ManagerProfileController implements Initializable{
    public TextArea information;
    public Label status;
    public Label requestType;
    public Label requestId;
    private Request request;

    @Override
    public void initialize(int id) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        }
        this.request = Request.getRequestWithId(id);
        status.setText(request.getStatus().toString());
        information.setText(request.getPendableRequest().toString());
        requestType.setText(request.getType());
        requestId.setText(Integer.toString(request.getRequestId()));
    }
}
