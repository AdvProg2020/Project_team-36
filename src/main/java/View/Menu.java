package View;

import Controllers.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    private String name;
    protected HashMap<String,Menu> subMenus;//regex
    public Menu parentMenu;
    protected static EntryMenu entryMenu;
    protected static ManagerController managerController;
    protected static CustomerController customerController;
    protected static SellerController sellerController;
    protected static OffController offController;
    protected static EntryController entryController;
    protected static Scanner scanner;

    public Menu(String name, Menu parentMenu) {
        this.name = name;
        this.parentMenu = parentMenu;
    }

    public static void setScanner(Scanner scanner){
        Menu.scanner = scanner;
    }

    public static void setControllers(){
        GlobalVariables user = new GlobalVariables();
        Menu.customerController = new CustomerController(user);
        Menu.entryController = new EntryController(user);
        Menu.managerController = new ManagerController(user);
        Menu.offController = new OffController(user);
        Menu.sellerController = new SellerController(user);
    }

    public abstract void execute();

    public abstract void help();

    public Matcher getMatcher(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

}
