package View.AllSellers;

import Controllers.NewProductController;
import Controllers.SellerController;
import Models.*;
import View.ManageCategoriesMenu;
import View.Menu;

import java.util.ArrayList;
import java.util.Date;

public class AddProductMenu extends Menu {


    public AddProductMenu(Menu parentMenu) {
        super("AddProductMenu", parentMenu);
    }

    @Override
    public void help() {
        System.out.println("you can send a request to manager for selling a new product or an existing one here.");
    }

    @Override
    public void execute() {
        System.out.println("do you want to sell a new product or a product which already exists? [new\\existing]");
        String choice = scanner.nextLine();
        while (!((choice.equalsIgnoreCase("back")) || (choice.equalsIgnoreCase("help")) ||
                (choice.equalsIgnoreCase("logout")))) {
            if (choice.equalsIgnoreCase("new")) {
                addNewProduct().execute();
            } else if (choice.equalsIgnoreCase("existing")) {
                addExistingProduct().execute();
            } else {
                System.out.println("wrong command. try again.");
            }
            System.out.println("do you want to sell a new product or a product which already exists? [new\\existing]");
            choice = scanner.nextLine();
        }
        if (choice.equalsIgnoreCase("back")) {
            this.parentMenu.execute();
        } else if (choice.equalsIgnoreCase("logout")) {
            logoutChangeMenu();
        } else if (choice.equalsIgnoreCase("help")) {
            this.help();
            this.execute();
        }
    }

    private Menu addNewProduct() {
        return new Menu("addNewProduct", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                NewProductController newProduct = new NewProductController(sellerController);
                System.out.println("please enter the required information:");
                System.out.println("name: ");
                newProduct.setName(getStringInput());
                System.out.println("company: ");
                newProduct.setCompany(getStringInput());
                System.out.println("information: ");
                newProduct.setInformation(getStringInput());
                System.out.println("choose the category this product is under: ");
                getCategory(newProduct);
                System.out.println("enter desired value for each field: ");
                getCategoryFields(newProduct);
                getProductFieldInfo(newProduct);
                newProduct.sendNewProductRequest();
            }
        };
    }

    private Menu addExistingProduct() {
        return new Menu("addExistingProduct", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                int number = 1;
                Product chosenProduct;
                long price;
                int supply;
                for (Product product : sellerController.getAllProducts()) {
                    System.out.println(number + ") " + product.getProductId() + " " + product.getName());
                    number += 1;
                }
                System.out.println("which product do you want to sell?");
                while (true) {
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("back")) {
                        this.parentMenu.execute();
                    } else if (input.equalsIgnoreCase("logout")) {
                        logoutChangeMenu();
                    }
                    try {
                        int id = Integer.parseInt(input);
                        chosenProduct = sellerController.getProductWithId(id);
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("you can't enter anything but number as id");
                    } catch (SellerController.InvalidProductIdException e) {
                        System.err.println("no product exists with this id");
                    }
                }
                System.out.println("enter wanted fields: ");
                price = getPrice();
                supply = getSupply();
                sellerController.sendAddSellerToProductRequest(price, supply, chosenProduct);
            }
        };
    }

    private void getProductFieldInfo(NewProductController newProduct) {
        long price = getPrice();
        int supply = getSupply();
        newProduct.setProductField(price, supply);
    }

    private void getCategoryFields(NewProductController newProduct) {
        for (Field field : newProduct.getNeededFields()) {
            while (true) {
                System.out.println("    " + field.getName() + ": ");
                String value = scanner.nextLine();
                if (value.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                } else if (value.equalsIgnoreCase("logout")) {
                    logoutChangeMenu();
                }
                try {
                    newProduct.setEachCategoryField(value, field);
                    break;
                } catch (NewProductController.InvalidFieldValue e) {
                    System.err.println("you can only enter numbers for this field");
                }
            }
        }
    }

    private void getCategory(NewProductController newProduct) {
        System.out.println("All Categories:");
        ManageCategoriesMenu.printCategoryTree(sellerController.getMainCategory());
        while (true) {
            try {
                String category = scanner.nextLine();
                if (category.equalsIgnoreCase("back")) {
                    this.parentMenu.execute();
                } else if (category.equalsIgnoreCase("logout")) {
                    logoutChangeMenu();
                }
                newProduct.setCategory(category);
                return;
            } catch (NewProductController.InvalidCategoryName e) {
                System.err.println("there's no category with this name. try again.");
            }
        }
    }

    private String getStringInput() {
        String input;
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("back")) {
            this.parentMenu.execute();
        } else if (input.equalsIgnoreCase("logout")) {
            logoutChangeMenu();
        }
        return input;
    }

    private long getPrice() {
        long price;
        while (true) {
            System.out.println("product price: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")) {
                logoutChangeMenu();
            }
            try {
                price = Long.parseLong(input);
                break;
            } catch (NumberFormatException e) {
                System.err.println("you can't enter anything but number for price");
            }
        }
        return price;
    }

    private int getSupply() {
        int supply;
        while (true) {
            System.out.println("initial supply: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("back")) {
                this.parentMenu.execute();
            } else if (input.equalsIgnoreCase("logout")) {
                logoutChangeMenu();
            }
            try {
                supply = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.err.println("you can't enter anything but number for supply");
            }
        }
        return supply;
    }

}
