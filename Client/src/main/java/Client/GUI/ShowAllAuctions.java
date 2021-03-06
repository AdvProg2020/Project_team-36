package Client.GUI;

import Client.Models.Auction;
import Client.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Date;

public class ShowAllAuctions extends CustomerTemplateController implements Initializable{
    @FXML
    private TableColumn<Auction,Long> highestPrice;
    @FXML
    private TableColumn<Auction, Date> endTime;
    @FXML
    private TableColumn<Auction,String> productName;
    @FXML
    private TableView<Auction> tableView;
    @FXML
    private Label enterAlert;
    @FXML
    private Label username;
    @FXML
    private ImageView profilePicture;
    private User user;

    @Override
    public void initialize(int id) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else {
            this.user = Constants.globalVariables.getLoggedInUser();
        }

        username.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150, 150).getImage());

        setTable();
    }

    private void setTable(){
        tableView.getItems().addAll(Constants.auctionController.getAllAuctions());
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        highestPrice.setCellValueFactory(new PropertyValueFactory<>("highestPrice"));
    }

    public void openWin(ActionEvent actionEvent) throws IOException {
        if(tableView.getSelectionModel().getSelectedItem()!=null)
        Constants.getGuiManager().open("ShowWinAuctions",tableView.getSelectionModel().getSelectedItem().getId());
    }

    public void openAuction(ActionEvent actionEvent) {
        Auction auction = tableView.getSelectionModel().getSelectedItem();
        if(auction==null){
            enterAlert.setVisible(true);
            return;
        }
        try {
            Constants.getGuiManager().open("OpenAuction",auction.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
