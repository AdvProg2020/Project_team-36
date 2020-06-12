package GUI;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Test2Controller {
    public void open() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/test.fxml"));
        Constants.getGuiManager().open(fxmlLoader.load());
    }

    public void back(){
        Constants.getGuiManager().back();
    }
}
