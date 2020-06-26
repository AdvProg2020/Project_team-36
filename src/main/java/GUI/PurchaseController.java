package GUI;

import java.io.IOException;

public class PurchaseController implements Initializable{
    @Override
    public void initialize(int id) throws IOException {
        //todo
    }

    public void apply(){
       //todo
    }

    public void checkout(){
        //todo
    }

    public void logout(){
        Constants.getGuiManager().logout();
    }

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }
}
