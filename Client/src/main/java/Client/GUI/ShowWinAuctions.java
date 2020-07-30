package Client.GUI;

import Client.Controllers.ProductsController;
import Client.Models.Auction;
import Client.Models.Customer;
import Client.Models.Product;
import Client.Models.User;
import Client.Network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ShowWinAuctions extends CustomerTemplateController implements Initializable {

    @FXML
    private TableView<Auction> tableView;
    @FXML
    private Button download;
    @FXML
    private TableColumn<Auction,String> productName;
    @FXML
    private TableColumn<Auction,Long> price;
    @FXML
    private TextArea addressBar;
    @FXML
    private TextField phoneBar;
    @FXML
    private Label errorLabel;
    @FXML
    private Label username;
    @FXML
    private ImageView profilePicture;

    @Override
    public void initialize(int id) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        }

        User user = Constants.globalVariables.getLoggedInUser();
        username.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        setTables();
    }

    private void setTables(){
        tableView.getItems().addAll(((Customer)Constants.globalVariables.getLoggedInUser()).getWinningAuctions());
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        price.setCellValueFactory(new PropertyValueFactory<>("highestPrice"));
    }

    public void buy(ActionEvent actionEvent) throws IOException {
        if(phoneBar.getText().equals("")||phoneBar.getText()==null||addressBar.getText()==null||addressBar.getText().equals("")){
            errorLabel.setVisible(true);
            return;
        }
        Constants.auctionController.buyAuction(tableView.getSelectionModel().getSelectedItem().getId(),addressBar.getText(),phoneBar.getText());
        AlertBox.display("Success","product successfully bought");
   Constants.getGuiManager().reopen();
    }

    public void download(ActionEvent actionEvent) throws IOException, ProductsController.NoProductWithId {
        Auction auction = tableView.getSelectionModel().getSelectedItem();
        Constants.auctionController.buyAuction(auction.getId(),"-","-");
        String path = getDirectoryAddress();
        downloadFiles(auction.getProduct(),path);
        Constants.getGuiManager().reopen();
    }

    public String getDirectoryAddress(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose files location!");
        File selectedDirectory = chooser.showDialog(Constants.getGuiManager().getLoginStage());
        return selectedDirectory.getPath();
    }

    public void downloadFiles(Product product, String path) throws IOException {
            File downloaded = new File(path+"/"+product.getFileProduct().getFileName()+"."+product.getFileProduct().getFileType());
            int count = 1;
            while(downloaded.exists()&&downloaded.isFile()){
                downloaded = new File(path+"/"+product.getFileProduct().getFileName()+"("+count+")."+product.getFileProduct().getFileType());
                count++;
            }
            OutputStream os = new FileOutputStream(downloaded);
            os.write(Client.readFile(product.getFileProduct().getFilePath()));
            os.close();
    }

    public void choose(ActionEvent actionEvent) {
        Auction auction = tableView.getSelectionModel().getSelectedItem();
        if(auction==null){
            return;
        }
        try {
            if(auction.getProduct().isFileProduct()){
                download.setVisible(true);
            }else{
                addressBar.setVisible(true);
                phoneBar.setVisible(true);
            }
        } catch (ProductsController.NoProductWithId noProductWithId) {
            noProductWithId.printStackTrace();
        }
    }
}
