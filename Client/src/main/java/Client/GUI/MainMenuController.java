package Client.GUI;

import Client.Controllers.EntryController;
import Client.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainMenuController implements Initializable{
    @FXML
    private Button logout;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button account;

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    public void logout() throws EntryController.NotLoggedInException, IOException {
        Constants.getGuiManager().logout();
    }

    public void chooseBackground()  {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Background");
        File file = fileChooser.showOpenDialog(Constants.getGuiManager().getStage());
        if (file != null){
            try {
                Image image = new Image(new FileInputStream(file));
                anchorPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(100,100,true,true,false,true))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(int id) throws IOException {
        Image image = new Image(new FileInputStream("./Client/src/main/resources/images/Background1.jpg"));
        anchorPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100,100,true,true,false,true))));
        if (Constants.globalVariables.getLoggedInUser() == null){
            logout.setVisible(false);
            account.setText("Login/Register");
            account.setOnAction(actionEvent -> Constants.getGuiManager().login());
        }else {
            account.setText("Account");
            account.setOnAction(actionEvent -> {
                try {
                    goToAccount();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void goToAccount() throws IOException {
        User user = Constants.globalVariables.getLoggedInUser();
        if ( user instanceof Client.Models.Manager){
            Constants.getGuiManager().open("ManagerPersonalInfo",user.getUserId());
        }else if (user instanceof Client.Models.Seller){
            Constants.getGuiManager().open("SellerPersonalInfo",user.getUserId());
        }else if (user instanceof Client.Models.Customer){
            Constants.getGuiManager().open("CustomerTemplate",user.getUserId());
        }else if(user instanceof Client.Models.Supporter){
            Constants.getGuiManager().open("SupporterPersonalInfo",user.getUserId());
        }
    }

    public void goToProducts() throws IOException {
        Constants.getGuiManager().open("ProductsMenu",1);
    }

    public void goToOffs() throws IOException {
        Constants.getGuiManager().open("ProductsMenu",2);
    }
}
