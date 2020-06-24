package GUI;

import Models.Customer;
import Models.Manager;
import Models.Seller;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public void logout(){
        Constants.getGuiManager().logout();
    }

    public void chooseBackground()  {
        FileChooser fileChooser = new FileChooser();
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
        Image image = new Image(new FileInputStream("src/main/resources/images/Background1.jpg"));
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
        if ( user instanceof Manager){
            Constants.getGuiManager().open("ManagerTemplate",user.getUserId());
        }else if (user instanceof Seller){
            Constants.getGuiManager().open("SellerTemplate",user.getUserId());
        }else if (user instanceof Customer){
            Constants.getGuiManager().open("CustomerTemplate",user.getUserId());
        }
    }

    public void goToProducts() throws IOException {
        Constants.getGuiManager().open("ProductsMenu",1);
    }

    public void goToOffs() throws IOException {
        Constants.getGuiManager().open("ProductsMenu",2);
    }
}
