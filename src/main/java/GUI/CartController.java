package GUI;

import java.io.IOException;

public class CartController {

    public void logout(){
        Constants.getGuiManager().logout();
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

}
