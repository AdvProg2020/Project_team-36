package GUI;

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
//        Menu.setScanner(new Scanner(System.in));
//        Menu.setControllers();
//        Menu runMenu = new MainMenu();
//        runMenu.execute();
        Constants.setControllers();
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Constants.getGuiManager().setStage(stage);
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("/fxml/test.fxml"));
        Parent parent = fxmlLoader.load();
       // stage.setMaximized(true);
        stage.setScene(new Scene(parent));
        Constants.getGuiManager().addToOpenedParents(parent);
        stage.show();
    }
}
