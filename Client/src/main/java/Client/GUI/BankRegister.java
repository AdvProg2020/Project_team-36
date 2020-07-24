package Client.GUI;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class BankRegister {
    static String returning;

    public static String display() {
        Stage window = new Stage();


        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("bank");
        window.setMinWidth(300);
        window.setMinHeight(300);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(12,12,12,12));
        layout.setSpacing(8);

         layout.setAlignment(Pos.CENTER);
         TextField firstname = new TextField();
         firstname.setPromptText("firstname");
         TextField lastname = new TextField();
         lastname.setPromptText("lastname");
         TextField username = new TextField();
         username.setPromptText("username");
         PasswordField password = new PasswordField();
         password.setPromptText("password");
         PasswordField repassword= new PasswordField();
         repassword.setPromptText("repassword");
         Label label = new Label();
         label.setStyle("-fx-text-fill: red");
        Button button = new Button("send");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String result = "";
                if(firstname.getText().contains(" ")){
                    label.setText("firstname should not contain space");
                    return;
                }else if(lastname.getText().contains(" ")){
                    label.setText("lastname should not contain space");
                    return;
                }else if(username.getText().contains(" ")){
                    label.setText("username should not contain space");
                    return;
                }else if(password.getText().contains(" ")){
                    label.setText("password should not contain space");
                    return;
                }else if(repassword.getText().contains(" ")){
                    label.setText("repassword should not contain space");
                    return;
                }
                result = Constants.bankController.createAccount(firstname.getText(),lastname.getText(),username.getText(),password.getText(),repassword.getText());
                if(result.equals("passwords do not match")){
                    label.setText("invalid username or password!");
                    return;
                }else if(result.equals("username is not valid")){
                    label.setText("invalid username!");
                    return;
                }else{
                    returning =result;
                    window.close();
                }
            }
        });
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return returning;
    }
}
