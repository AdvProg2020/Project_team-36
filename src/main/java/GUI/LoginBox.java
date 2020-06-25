package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginBox {

    private Stage window;

    public LoginBox() {
        this.window = new Stage();
        window.setTitle("Entry page");
        window.initModality(Modality.APPLICATION_MODAL);
    }

    public void display() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginMenu.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        window.setScene(scene);
        window.showAndWait();
    }

    public Stage getWindow() {
        return window;
    }
}
