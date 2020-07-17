package GUI;

import java.io.IOException;

public class Test2Controller implements Initializable{
    public void open() throws IOException {
        Constants.getGuiManager().open("test",1);
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    @Override
    public void initialize(int id) {

    }
}
