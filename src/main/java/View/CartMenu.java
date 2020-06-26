package View;

import Controllers.CustomerController;
import Controllers.ProductsController;
import Models.*;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class CartMenu extends Menu {
    private int productId;

    public CartMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("show\\s+products", getShowCartMenu());
        subMenus.put("view\\s+(\\d+)", getViewProductMenu());
        subMenus.put("increase\\s+(\\d+)", getIncreaseAmountMenu());
        subMenus.put("decrease\\s+(\\d+)", getDecreaseAmountMenu());
        subMenus.put("show\\s+total\\s+price", getShowTotalPriceMenu());
        subMenus.put("purchase", new ReceiveInformationMenu("receiveInformationMenu", this));


    }

    private Menu getShowCartMenu() {
        return new Menu("ShowCartMenu", this) {
            @Override
            public void help() {
                System.out.println("logout\n" +
                        "help\n" +
                        "sort by [field] [ascending\\descending]\n" +
                        "back");
                System.out.println("Fields you can sort by:\nproduct name\ncount\ntotal price");
            }

            @Override
            public void execute() {
                System.out.println("NOTE: any product that is deleted by manager cannot be seen in your cart!");
                ArrayList<SelectedItem> cart = customerController.getCart();
                if (cart.isEmpty()) {
                    System.err.println("Nothing in cart!");
                    return;
                }
                printCart(cart);
                getSortType();
            }
        };
    }

    private void getSortType(){
        System.out.println("\nyou can sort your cart or type back or logout");
        String input;
        Matcher matcher;
        while(!(input=scanner.nextLine().trim()).matches("back|logout")){
            if(input.matches("help"))
                help();
            if((matcher = getMatcher(input,"sort\\s+by\\s+(.+)\\s+(.+)")).matches()){
                if(!matcher.group(2).matches("asscending|descending"))
                    System.err.println("Invalid type!");
                else{
                    try {
                        printCart(customerController.sortCart(matcher.group(1),matcher.group(2)));
                    } catch (ProductsController.NoSortException e) {
                        System.err.println("there is no field with this name!");
                    }
                }
            }
        }
        if(input.matches("back"))
            return;
        else if(input.matches("logout"))
            logoutChangeMenu();
    }

    private void printCart(ArrayList<SelectedItem>cart){
        System.out.format("%20s%9s%s%s", "Product name", " ProductId  ", "  count in cart  ", "Availability");
        boolean typeNote = false;
        for (SelectedItem item : cart) {
            Product product = item.getProduct();
            System.out.format("%20s%9d%10d%s", product.getName(), product.getProductId(), item.getCount(), item.getTag());
            if (item.getTag().equals(CartTag.NOT_ENOUGH_SUPPLY))
                typeNote = true;
        }
        if (typeNote)
            System.out.println("NOTE: you have to decrease the \"NOT_ENOUGH_SUPPLY\" objects before purchase!");
    }

    private Menu getViewProductMenu() {
        return new Menu("viewProductMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                if (!customerController.isThereProductInCart(productId)) {
                    System.err.println("There is no product with this id in your cart!");
                    return;
                }
                try {
                    productsController.setChosenProduct(productId);
                    new ProductMenu(this).execute();
                } catch (ProductsController.NoProductWithId noProductWithId) {
                    System.err.println("there is no product with id!");
                }

            }
        };
    }

    private Menu getIncreaseAmountMenu() {
        return new Menu("increaseAmountMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    customerController.increaseProductInCart(productId);
                    System.out.println("Increased successfully!");
                    return;
                } catch (CustomerController.NoProductWithIdInCart e) {
                    System.err.println("There is no product with id in your cart!");
                    return;
                } catch (CustomerController.MoreThanOneSellerForItem e) {
                    multiSellerForIncrease(e.getSellers());
                } catch (CustomerController.NotEnoughSupply e) {
                    System.err.println("There is not enough supply for this product! ");
                    return;
                }


            }

        };
    }

    private void multiSellerForIncrease(ArrayList<Seller> sellers) {
        String input;
        System.out.println("You are buying from many sellers. Choose which seller you want to increase supply from by entering number");
        int i = 1;
        for (Seller seller : sellers) {
            System.out.println(i + ". " + seller.getFirstname() + " " + seller.getLastname());
            i++;
        }
        while (true) {
            input = scanner.nextLine().trim();
            if (input.matches("logout"))
                logoutChangeMenu();
            if (input.matches("back"))
                return;
            else if (!input.matches("\\d+") || Integer.parseInt(input) >= i) {
                System.out.println("invalid command! Try again please");
            } else {
                try {
                    int sellerId=0;
                    customerController.increaseProductInCart(sellerId, (productId));
                    System.out.println("Increased successfully");
                    return;
                } catch (CustomerController.NotEnoughSupply e) {
                    System.err.println("There is not enough supply for product!Try again ");
                }

            }
        }
    }

    private Menu getDecreaseAmountMenu() {
        return new Menu("decreaseAmountMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    customerController.decreaseProductInCart(productId);
                    System.out.println("Decreased successfully");
                } catch (CustomerController.NoProductWithIdInCart e) {
                    System.err.println("There is no product with id in your cart");
                } catch (CustomerController.MoreThanOneSellerForItem e) {
                    multiSellerDecrease(e.getSellers());
                }
            }

        };
    }

    private void multiSellerDecrease(ArrayList<Seller> sellers) {
        String input;
        System.out.println("You are buying from many sellers. Choose which seller you want to decrease supply from by entering number");
        int i = 1;
        for (Seller seller : sellers) {
            System.out.println(i + ". " + seller.getFirstname() + " " + seller.getLastname());
            i++;
        }
        while (true) {
            input = scanner.nextLine().trim();
            if (input.matches("logout"))
                logoutChangeMenu();
            if (input.matches("back"))
                return;
            else if (!input.matches("\\d+") || Integer.parseInt(input) >= i) {
                System.out.println("invalid command! Try again please");
            } else {
                customerController.decreaseProductInCart(sellers.get(Integer.parseInt(input) - 1), (productId));
                System.out.println("Decreased successfully");
                return;
            }
        }
    }

    private Menu getShowTotalPriceMenu() {
        return new Menu("totalPriceMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                System.out.print("Total price: ");
                System.out.println(customerController.getTotalCartPrice());
            }
        };
    }

    @Override
    public void help() {
        System.out.println("show products\n" +
                "view [productId]\n" +
                "increase [productId]\n" +
                "decrease [productId]\n" +
                "show total price\n" +
                "purchase\n" +
                "logout");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine().trim();
        Menu chosenMenu = null;
        if (input.matches("logout"))
            logoutChangeMenu();
        if (input.matches("back")) {
            this.parentMenu.execute();
        } else if (input.matches("help")) {
            help();
            execute();
        } else {
            for (String regex : subMenus.keySet()) {
                Matcher matcher = getMatcher(input, regex);
                if (matcher.matches()) {
                    chosenMenu = subMenus.get(regex);
                    try {
                        this.productId = Integer.parseInt(matcher.group(1));
                    } catch (IndexOutOfBoundsException e){

                    }catch (NumberFormatException e) {
                        System.err.println("There is no product with this id!");
                    }
                    break;
                }
            }
            if (chosenMenu == null) {
                System.err.println("Invalid command! Try again please");
                this.execute();
            } else {
                chosenMenu.execute();
                this.execute();
            }
        }
    }
}
