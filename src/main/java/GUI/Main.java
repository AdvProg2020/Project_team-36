package GUI;

import Models.Category;
import Models.Manager;
import Models.Product;
import Models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


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
        String firstPage = "ManagerRegister";

        if (!Manager.canManagerRegister())
            firstPage = "MainMenu";

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + firstPage + ".fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        Constants.globalVariables.setLoggedInUser(User.getUserById(2));
        Constants.getGuiManager().open(firstPage, 1);
        stage.show();
    }
}
