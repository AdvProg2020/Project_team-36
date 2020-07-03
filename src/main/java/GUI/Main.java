package GUI;

import Models.Category;
import Models.Manager;
import Models.Product;
import Models.User;
import Repository.RepositoryManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;




public class Main extends Application {



    public static void main(String[] args) {
    //    User.addTest();
    //    Product.addTest();
    //    Category.addTest();
//        Menu.setScanner(new Scanner(System.in));
//        Menu.setControllers();
//        Menu runMenu = new MainMenu();
//        runMenu.execute();
      //  Product.addTest();
        Constants.setControllers();
        launch(args);

    }


    @Override
    public void start(Stage stage) throws Exception {
        Constants.getGuiManager().setStage(stage);
        RepositoryManager.loadData();

        String firstPage = "ManagerRegister";

        if (!Manager.canManagerRegister())
            firstPage = "MainMenu";

        File file = new File ("D:\\myprj\\project\\AP_Project\\src\\main\\resources\\images\\customerBackground.jpg");
        String path = file.toURI().toURL().toString();
        stage.getIcons().add(new Image(path,50,50,false,false));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + firstPage + ".fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        Constants.globalVariables.setLoggedInUser(User.getUserById(2));
        Constants.getGuiManager().open(firstPage, 1000);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> RepositoryManager.saveData());
    }
}
