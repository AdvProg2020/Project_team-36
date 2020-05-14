package View.Products;
import View.Menu;

public class OffsMenu extends Menu {


    public OffsMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("sorting",new SortingMenu("SortingMenu",this,offController));
    }

    @Override
    public void help() {

    }
}
