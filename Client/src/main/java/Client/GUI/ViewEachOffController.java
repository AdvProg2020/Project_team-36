package Client.GUI;

import Client.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class ViewEachOffController extends SellerProfileController implements Initializable {

    @FXML private Label codeLabel;
    @FXML private TextField percent;
    @FXML private ScrollPane productIncluded;
    @FXML private DatePicker endDate;
    @FXML private DatePicker startDate;
    @FXML private Label usernameLabel;
    @FXML private ImageView profilePicture;

    @Override
    public void initialize(int id) throws IOException {

        User user;
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        } else {
            user = Constants.globalVariables.getLoggedInUser();
        }
        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        startDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(startDate.getValue()) < 0 || date.compareTo(startDate.getValue()) > 0);
            }
        });

        endDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(endDate.getValue()) < 0 || date.compareTo(endDate.getValue()) > 0);
            }
        });

        setValues();
    }

    private void setValues(){
        Client.Models.Sale offToView = Constants.sellerController.getOffToView();

        codeLabel.setText("Off: " + offToView.getOffId());
        startDate.setValue(new Date(offToView.getStartTime().getTime()).toLocalDate());
        endDate.setValue(new Date(offToView.getEndTime().getTime()).toLocalDate());
        percent.setText(Integer.toString(offToView.getSalePercentForTable()));

        VBox vBox = new VBox();
        productIncluded.setContent(vBox);
        for (Client.Models.Product product : offToView.getProductsInSale()) {
            Label productName = new Label(product.getName());
            vBox.getChildren().add(productName);
        }

    }

}
