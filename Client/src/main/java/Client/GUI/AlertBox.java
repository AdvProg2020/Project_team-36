package Client.GUI;
import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AlertBox {

    public static void display(String title, String message) {
        Stage window = new Stage();

        ClassLoader classLoader = AlertBox.class.getClassLoader();
        URL resource = classLoader.getResource("/images/star.png");
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            File file = new File(resource.getFile());
            String path = "";
            try {
                path = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            window.getIcons().add(new Image(path,50,50,false,false));
        }

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(12,12,12,12));
        layout.setSpacing(8);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}

