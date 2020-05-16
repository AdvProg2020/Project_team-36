package View.Products;

import Controllers.ProductsController;
import Models.Product;
import View.Menu;
import View.ProductMenu;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class OffsMenu extends Menu {

    public OffsMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("sorting", new SortingMenu("SortingMenu", this, offController));
        subMenus.put("filtering", new FilteringMenu("filtering menu", this, offController));
        subMenus.put("show\\s+all\\s+offs",getShowProductsMenu());

    }

    @Override
    public void help() {
        System.out.println("sorting\n" +
                "filtering\n" +
                "login\n" +
                "register\n" +
                "logout\n" +
                "help" +
                "show all offs");
    }

    private Menu getShowProductsMenu() {
        return new Menu("show off products", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                ArrayList<Product> products = offController.getAllInSaleProducts();
                if(products.isEmpty())
                    System.out.println("There is nothing to show!");
                else{
                    for (Product product : products) {
                        System.out.println(product);
                        System.out.println("Before sale: "+product.getBestSale().getPrice());
                        System.out.println("After sale: "+product.getBestSale().getCurrentPrice());
                    }
                }
            }
        };
    }

    @Override
    public void execute() {
        Matcher matcher;
        String input;
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("back")) {
            if(input.matches("help|login|logout|register"))
                sideCommands(input);
            else if((matcher = getMatcher(input,"show\\s+product\\s+(\\.+)")).matches()){
               goProductMenu(matcher);
            }else{
                Menu chosenMenu = null;
                for (String regex : subMenus.keySet()) {
                    if(input.matches(regex)){
                       chosenMenu = subMenus.get(regex);
                       break;
                    }
                }
                if(chosenMenu!=null)
                    chosenMenu.execute();
                else
                    System.out.println("Invalid command!");
            }
        }
        this.parentMenu.execute();
    }

    private void goProductMenu(Matcher matcher){
        if(!matcher.group(1).matches("\\d+"))
            System.out.println("Wrong format of product id!");
        else{
            int productId = Integer.parseInt(matcher.group(1));
            try{
                new ProductMenu(productsController.getProduct(productId)).execute();

            } catch (ProductsController.NoProductWithId noProductWithId) {
                System.out.println("there is no product with this id");
            }
        }

    }
    //ArsiA
}
