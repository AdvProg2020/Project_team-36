package View.Products;

import Controllers.EntryController;
import Controllers.ProductsController;
import Models.Product;
import View.EntryMenu;
import View.ManageCategoriesMenu;
import View.Menu;
import View.ProductMenu;

import java.util.regex.Matcher;

public class ProductsMenu extends Menu {

    public ProductsMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("view\\s+categories", getViewCategoryMenu());
       // subMenus.put("filtering", new FilteringMenu("filter", this));
        subMenus.put("sorting", new SortingMenu("SortingMenu", this,productsController));
      //  subMenus.put("show\\s+products", getShowProductsMenu());

    }

    @Override
    public void help() {
        System.out.println("view categories\n" +
                "filtering\n" +
                "sorting\n" +
                "show products\n" +
                "show product [productId]\n" +
                "logout\n" +
                "login\n" +
                "register");
    }

    private Menu getViewCategoryMenu() {
        return new Menu("ViewCategory", this) {
            @Override
            public void help() {}

            @Override
            public void execute() {
                ManageCategoriesMenu.printCategoryTree(productsController.getMainCategory());
            }
        };
    }

    private Menu getShowProductsMenu() {
        return new Menu("show products menu", this) {
            @Override
            public void help() {

            }
        };
    }

    @Override
    public void execute() {
        Matcher matcher;
        String input;
        while (!(input = scanner.nextLine().trim()).matches("back")) {
            if (input.equalsIgnoreCase("help"))
                help();
            else if ((matcher = getMatcher(input, "show\\s+product\\s+(\\S+)")).matches())
                goToProductMenu(matcher.group(1));
            else if (input.matches("logout")) {
                try {
                    entryController.logout();
                } catch (EntryController.NotLoggedInException e) {
                    System.err.println("You are not logged in!");
                }
            }else if(input.matches("login|register")){
                if(entryController.isUserLoggedIn())
                    System.err.println("You are loggedIn!");
                else
                    new EntryMenu(this).execute();
            }else{
                Menu menu = null;
                for (String regex : this.subMenus.keySet()) {
                    if(input.matches(regex)) {
                        menu = subMenus.get(regex);
                        break;
                    }
                }
                if(menu==null)
                    System.err.println("Invalid command!");
                else
                    menu.execute();
            }
        }
        this.parentMenu.execute();
    }

    private void goToProductMenu(String id) {
        int productId;
        try {
            productId = Integer.parseInt(id);
        } catch (Exception e) {
            System.err.println("Invalid productId type!Try again");
            return;
        }
        try {
            Product product = productsController.getProduct(productId);
            new ProductMenu(product).execute();

        } catch (ProductsController.NoProductWithId noProductWithId) {
            System.err.println("There is no product with this id!");
            return;
        }
    }
}
