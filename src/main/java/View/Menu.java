package View;

import Controllers.*;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Menu {
    protected String name;
    protected HashMap<String,Menu> subMenus;//regex
    protected Menu parentMenu;
    protected EntryMenu entryMenu;
    protected ManagerController managerController;
    protected CustomerController customerController;
    protected SellerController sellerController;
    protected OffController offController;
    protected EntryController entryController;
    protected Scanner scanner;
}
