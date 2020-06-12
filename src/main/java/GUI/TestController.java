package GUI;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class TestController {
    public void open() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/test2.fxml"));
        Constants.getGuiManager().open(fxmlLoader.load());
    }

    public void back(){
        Constants.getGuiManager().back();
    }
}
