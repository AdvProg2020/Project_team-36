package View.Products;

import Controllers.EntryController;
import View.EntryMenu;
import View.Menu;


import java.util.regex.Matcher;

public class SortingMenu extends Menu {
    private String sort;

    public SortingMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("show\\s+available\\s+sorts", getAvailableSortsMenu());
        subMenus.put("current\\s+sort", getCurrentSortMenu());
        subMenus.put("disable\\s+sort", getDisableSortMenu());
        subMenus.put("sort\\s+(\\.+)", getSortMenu());
    }

    @Override
    public void help() {
        System.out.println("show available sorts\n" +
                "sort [an available sort\n" +
                "current sort\n" +
                "disable sort\n" +
                "logout\n" +
                "login\n" +
                "register");
    }

    private Menu getAvailableSortsMenu() {
        return new Menu("Available sort menu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    private Menu getCurrentSortMenu() {
        return new Menu("Current sort menu", this) {
            @Override
            public void help() {

            }
        };
    }

    private Menu getDisableSortMenu() {
        return new Menu("DisableSort", this) {
            @Override
            public void help() {

            }
        };
    }

    private Menu getSortMenu() {
        return new Menu("Sort menu", this) {
            @Override
            public void help() {

            }
        };
    }

    @Override
    public void execute() {
        String input;
        Matcher matcher;
        Menu chosenMenu = null;
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("back")) {
            if (input.matches("help"))
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
            } else {
                Menu menu = null;
                for (String regex : this.subMenus.keySet()) {
                    if ((matcher = getMatcher(input,regex)).matches()) {
                        menu = subMenus.get(regex);
                        try{
                            this.sort = matcher.group(1);
                        }catch(Exception e){
                            //do nothing
                        }
                        break;
                    }
                }
                if (menu == null)
                    System.err.println("Invalid command!");
                else
                    menu.execute();
            }
        }
        parentMenu.execute();
    }
}