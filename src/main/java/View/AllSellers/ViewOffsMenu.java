package View.AllSellers;

import Controllers.SellerController;
import Models.Product;
import Models.Sale;
import Models.Seller;
import View.Menu;

import java.util.regex.Matcher;

public class ViewOffsMenu extends Menu {

    private int id;

    public ViewOffsMenu(Menu parentMenu) {
        super("ViewOffsMenu", parentMenu);
        subMenus.put("^view\\s+(\\d+)$",getViewOff());
    }

    @Override
    public void help() {
        System.out.println("view [offId]\n" +
                "edit [offId]\n" +
                "add off\n");
    }

    @Override
    public void execute() {
        int number = 1;
        Matcher matcher;
        Menu chosenMenu = null;
        for (Sale off : sellerController.getAllSellerSales()) {
            System.out.println(number + ") " + off.getOffId() + "  " + (off.getSalePercent()*100)+"%");
            number += 1;
        }
        System.out.println("choose the off and what you want to do with it :");
        String input = scanner.nextLine().trim();
        while (!((input.equalsIgnoreCase("back"))||(input.equalsIgnoreCase("help"))||
                (input.equalsIgnoreCase("logout")))) {
            for (String regex : subMenus.keySet()) {
                matcher = getMatcher(input, regex);
                if (matcher.matches()) {
                    chosenMenu = subMenus.get(regex);
                    try {
                        this.id = Integer.parseInt(matcher.group(1));
                    } catch (NumberFormatException e) {
                        System.err.println("you can't enter anything but number as id");
                    }
                    break;
                }
            }
            if (chosenMenu == null) {
                System.err.println("Invalid command! Try again please");
            } else {
                chosenMenu.execute();
            }
            chosenMenu=null;
            input = scanner.nextLine().trim();
        }
        if (input.equalsIgnoreCase("back")) {
            this.parentMenu.execute();
        } else if (input.equalsIgnoreCase("help")) {
            this.help();
            this.execute();
        } else if(input.equalsIgnoreCase("logout")){
            logoutChangeMenu();
        }
    }

    private Menu getViewOff(){
        return new Menu("getViewOff",this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    Sale off = sellerController.getSaleWithId(id);
                    System.out.println(off);
                } catch (SellerController.InvalidOffIdException e){
                    System.err.println("you don't have an off with these id");
                }
            }
        };
    }

}
