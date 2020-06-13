package GUI;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class TestController implements Initializable {
    public void open() throws IOException {
        Constants.getGuiManager().open("test2",1);
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    @Override
    public void initialize(int id) {

    }
}
