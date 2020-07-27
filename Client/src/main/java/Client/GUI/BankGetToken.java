package Client.GUI;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BankGetToken {

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

        TextField username = new TextField();
        username.setPromptText("username");

        PasswordField password = new PasswordField();
        password.setPromptText("password");

        Label label = new Label();
        label.setStyle("-fx-text-fill: red");
        Button button = new Button("send");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String result = "";
                if(username.getText().contains(" ")||username.getText()==null){
                    label.setText("username should not contain space or be empty");
                    return;
                }else if(password.getText().contains(" ")||password.getText()==null){
                    label.setText("password should not contain space or be empty");
                    return;
                }
                result = Constants.bankController.getToken(username.getText(),password.getText());
                if(result.equals("invalid username or password")){
                    label.setText("invalid username or password!");
                    return;
                }else{
                    returning =result;
                    window.close();
                }
            }
        });
        //Display window and wait for it to be closed before returning
        layout.getChildren().addAll(username,password,button,label);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return returning;
    }
}
