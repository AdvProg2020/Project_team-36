package View;

import Controllers.EntryController;
import Controllers.ProductsController;
import Models.Field;
import Models.Product;
import Models.ProductField;

public class DigestMenu extends Menu {
    private String username;

    public DigestMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("add\\s+to\\s+cart", getAddToCartMenu());
        subMenus.put("select\\s+seller\\s+(\\d+)", getSelectSellerMenu());
    }

    @Override
    public void help() {
        System.out.println("add to cart\n" +
                "select seller [seller_username]\n" +
                "login\n" +
                "logout\n" +
                "register");
    }

    private Menu getAddToCartMenu() {
        return new Menu("add to cart", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    productsController.addToCart();
                    System.out.println("Product added to cart successfully");
                } catch (ProductsController.NoSellerIsChosen noSellerIsChosen) {
                    System.err.println("you first have to choose a seller");
                } catch (EntryController.NotLoggedInException e) {
                    System.err.println("You haven't logged in!");
                    new EntryMenu(this).execute();
                } catch (ProductsController.UserCantBuy userCantBuy) {
                    System.err.println("You can only buy with customer account!");
                }
            }
        };
    }

    private Menu getSelectSellerMenu() {
        return new Menu("select seller", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    productsController.addSellerForBuy(username);
                } catch (ProductsController.NotEnoughSupply notEnoughSupply) {
                    System.out.println("The product with this seller is not available now!");
                } catch (ProductsController.NoSellerWithUsername noSellerWithUsername) {
                    System.out.println("Wrong username!");
                }
            }
        };
    }

    @Override
    public void execute() {
        Product product = productsController.getChosenProduct();
        printProduct(product);
        String input;
        while (!(input = scanner.nextLine().trim()).equalsIgnoreCase("back")) {
            if (input.matches("login|logout|help|register"))
                sideCommands(input);
            else if (input.matches("select\\s+seller\\s+(\\S+)")) {
                username = getMatcher(input, "select\\s+seller\\s+(\\S+)").group(1);
                getSelectSellerMenu().execute();
            } else if (input.matches("add\\s+to\\s+cart"))
                getAddToCartMenu().execute();
            else System.err.println("invalid command!");
        }
        productsController.resetDigest();
        this.parentMenu.execute();
    }

    private void printProduct(Product product) {
        System.out.println("Name: " + product.getName());
        System.out.println("Company: " + product.getCompany());
        System.out.println("Category: " + product.getCategory());
        System.out.println("Average score:" + product.getScore());
        for (ProductField field : product.getProductFields()) {
            System.out.println(field);
        }

    }
}
