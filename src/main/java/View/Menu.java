package View;

import Controllers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    private String name;
    protected HashMap<String, Menu> subMenus;//regex
    public Menu parentMenu;
    protected static EntryMenu entryMenu;
    protected static ManagerController managerController;
    protected static CustomerController customerController;
    protected static SellerController sellerController;
    protected static EntryController entryController;
    protected static ProductsController productsController;
    protected static OffController offController;
    protected static Scanner scanner;
    protected static ProductController productController;
    protected static UserController userController;

    public Menu(String name, Menu parentMenu) {
        this.name = name;
        this.parentMenu = parentMenu;
        this.subMenus = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Menu getParentMenu() {
        return parentMenu;
    }

    public static void setScanner(Scanner scanner) {
        Menu.scanner = scanner;
    }

    public static void setControllers() {
        GlobalVariables user = new GlobalVariables();
        Menu.customerController = new CustomerController(user);
        Menu.entryController = new EntryController(user);
        Menu.managerController = new ManagerController(user);
        Menu.offController = new OffController(user);
        Menu.sellerController = new SellerController(user);
        Menu.productsController = new ProductsController(user);
    }

    public void logoutChangeMenu() {
        Menu parent = this;
        while (!(parent instanceof UserAreaMenu)) {
            parent = parent.getParentMenu();
        }
        ((UserAreaMenu)parent).logout();
        try {
            entryController.logout();
        } catch (EntryController.NotLoggedInException e) {
            e.printStackTrace();
        }
        parent.getParentMenu().help();
        parent.getParentMenu().execute();
    }

    public void sideCommands(String input){
        if (input.equalsIgnoreCase("help"))
            help();
        else if (input.matches("logout")) {
            try {
                entryController.logout();
            } catch (EntryController.NotLoggedInException e) {
                System.err.println("You are not logged in!");
            }
        } else if (input.matches("login|register")) {
            if (entryController.isUserLoggedIn())
                System.err.println("You are loggedIn!");
            else
                new EntryMenu(this).execute();
        }
    }

    public HashMap<String, Menu> getSubMenus() {
        return subMenus;
    }

    public void execute() {
        String input = scanner.nextLine().trim();
        Menu chosenMenu = null;
        if (input.matches("back")) {
            parentMenu.execute();

        } else if (input.matches("help")) {
            help();
            this.execute();
        } else if (input.matches("logout")) {
            logoutChangeMenu();
        }else{
            for (String regex : subMenus.keySet()) {
                if (input.matches(regex)) {
                    chosenMenu = subMenus.get(regex);
                    break;
                }
            }
        }
        if (chosenMenu != null)
            chosenMenu.execute();
        else {
            System.err.println("Invalid command! Try again please!");
            this.execute();
        }

    }

    public abstract void help();

    public Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

    public static class BackIsPressed extends Exception{

    }

    public static class LogoutIsPressesException extends Exception{}

}
