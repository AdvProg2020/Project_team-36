package View.AllSellers;

import Controllers.SellerController;
import Models.Discount;
import Models.Product;
import View.Menu;

import java.util.regex.Matcher;

public class ManageProductsMenu extends Menu {

    private int id;

    public ManageProductsMenu(Menu parentMenu) {
        super("ManageProductsMenu", parentMenu);
        subMenus.put("view\\s+(\\d+)",getViewProduct());
        subMenus.put("view\\s+buyers\\s+(\\d+)",getViewBuyers());
        subMenus.put("edit\\s+(\\d+)",getEditProduct());
        subMenus.put("remove\\s+product\\s+(\\d+)",getRemoveProduct());
    }

    @Override
    public void help() {
        System.out.println("view [productId]\n" +
                "view products [productId]\n" +
                "edit [product]\n" +
                "remove product [productId]\n");
    }

    @Override
    public void execute() {
        int number = 1;
        Matcher matcher;
        Menu chosenMenu = null;
        for (Product product : sellerController.getSellerProducts()) {
            System.out.println(number + ") " + product.getProductId() + " " + product.getName());
            number += 1;
        }
        System.out.println("choose the product and what you want to do with it :");
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

    private Menu getViewProduct(){
        return new Menu("getViewProduct",this) {
            @Override
            public void help() { }

            @Override
            public void execute() {
                try {
                    Product product = sellerController.getProductWithId(id);
                    System.out.println(sellerController.getSellerProductDetail(product));
                } catch (SellerController.InvalidProductIdException e){
                    System.err.println("there's no product with these id");
                }
            }
        };
    }

    private Menu getViewBuyers(){
        return new Menu("getViewBuyers",this) {
            @Override
            public void help() { }

            @Override
            public void execute() {
                super.execute();
            }
        };
    }

    private Menu getEditProduct(){
        return new Menu("getEditProduct",this) {
            @Override
            public void help() { }

            @Override
            public void execute() {

            }
        };
    }

    private Menu getRemoveProduct(){
        return new Menu("getRemoveProduct",this) {
            @Override
            public void help() { }

            @Override
            public void execute() {

            }
        };
    }
}
