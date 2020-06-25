package GUI;

import Models.Category;
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
       User.addTest();
        Product.addTest();
        Category.addTest();
//        Menu.setScanner(new Scanner(System.in));
//        Menu.setControllers();
//        Menu runMenu = new MainMenu();
//        runMenu.execute();
        Product.addTest();
        Constants.setControllers();
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Constants.getGuiManager().setStage(stage);
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
       // FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("/fxml/productsMenu.fxml"));
        //FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("/fxml/te.fxml"));

        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
      //  Constants.globalVariables.setLoggedInUser(User.getUserById(2));
       Constants.getGuiManager().open("MainMenu",2);
        stage.show();
    }
}
