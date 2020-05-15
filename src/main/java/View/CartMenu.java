package View;

import Controllers.CustomerController;
import Exceptions.NoLoggedInUserException;
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
        subMenus.put("purchase",new ReceiveInformationMenu("receiveInformationMenu",this));


    }

    public int getProductId() {
        return productId;
    }

    private Menu getShowCartMenu() {
        return new Menu("ShowCartMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    ArrayList<SelectedItem> cart = customerController.getCart();
                    if (cart.isEmpty()) {
                        System.out.println("There is nothing to show!");
                        return;
                    }
                    System.out.format("%20s%9s%s%s", "Product name", " ProductId  ", "  count in cart  ","Availability");
                    for (SelectedItem item : cart) {
                        Product product = item.getProduct();
                        System.out.format("%20s%9d%10d%s", product.getName(), product.getProductId(), item.getCount(),item.getTag());
                    }
                    System.err.println("NOTE:");
                    System.out.println("If one or more items in your cart are not available, you can edit them of if you want to purchase," +
                            " we automatically edit your cart!");
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }
        };
    }

    private Menu getViewProductMenu() {
        return new Menu("viewProductMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    if (customerController.isThereProductInCart(((CartMenu)this.parentMenu).getProductId())) {
                        System.err.println("There is no product with this id in your cart!");
                        return;
                    }
                    ProductMenu productMenu = new ProductMenu(((CartMenu)this.parentMenu).getProductId());
                    productMenu.execute();
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
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
                    customerController.increaseProductInCart(((CartMenu)this.parentMenu).getProductId());
                    System.out.println("Increased successfully!");
                    return;
                } catch (CustomerController.NoProductWithIdInCart e) {
                    System.err.println(e.getMessage());
                    return;
                } catch (CustomerController.MoreThanOneSellerForItem e) {
                    moreThanOneSeller(e.getSellers());
                } catch (CustomerController.NotEnoughSupply e) {
                    System.err.println("There is not enough supply for this product! ");
                    return;
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }

            private void moreThanOneSeller(ArrayList<Seller> sellers) {
                String input;
                System.out.println("You are buying from many sellers. Choose which seller you want to increase supply from by entering number");
                int i = 1;
                for (Seller seller : sellers) {
                    System.out.println(i + ". " + seller.getFirstname() + " " + seller.getLastname());
                    i++;
                }
                while (true) {
                    input = scanner.nextLine().trim();
                    if(input.matches("logout"))
                        logoutChangeMenu();
                    if (input.matches("back"))
                        return;
                    else if (!input.matches("\\d+") || Integer.parseInt(input) >= i) {
                        System.out.println("invalid command! Try again please");
                    } else {
                        try {
                            customerController.increaseProductInCart(Integer.parseInt(input), ((CartMenu)this.parentMenu).getProductId());
                            System.out.println("Increased successfully");
                            return;
                        } catch (CustomerController.NotEnoughSupply e) {
                            System.err.println("There is not enough supply for product!Try again ");
                        }catch (NoLoggedInUserException e){
                            System.err.println(e.getMessage());
                        }

                    }
                }
            }
        };
    }

    private Menu getDecreaseAmountMenu() {
        return new Menu("decreaseAmountMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                try {
                    customerController.decreaseProductInCart(((CartMenu)this.parentMenu).getProductId());
                    System.out.println("Decreased successfully");
                } catch (CustomerController.NoProductWithIdInCart e) {
                    System.out.println(e.getMessage());
                } catch (CustomerController.MoreThanOneSellerForItem e) {
                    moreThanOneSeller(e.getSellers());
                }catch (NoLoggedInUserException e){
                    System.err.println(e.getMessage());
                }
            }

            private void moreThanOneSeller(ArrayList<Seller> sellers) {
                String input;
                System.out.println("You are buying from many sellers. Choose which seller you want to decrease supply from by entering number");
                int i = 1;
                for (Seller seller : sellers) {
                    System.out.println(i + ". " + seller.getFirstname() + " " + seller.getLastname());
                    i++;
                }
                while (true) {
                    input = scanner.nextLine().trim();
                    if(input.matches("logout"))
                        logoutChangeMenu();
                    if (input.matches("back"))
                        return;
                    else if (!input.matches("\\d+") || Integer.parseInt(input) >= i) {
                        System.out.println("invalid command! Try again please");
                    } else {
                        try {
                            customerController.decreaseProductInCart(sellers.get(Integer.parseInt(input)-1), ((CartMenu)this.parentMenu).getProductId());
                            System.out.println("Decreased successfully");
                        }catch (NoLoggedInUserException e){
                            System.err.println(e.getMessage());
                        }
                        return;
                    }
                }
            }
        };
    }

    private Menu getShowTotalPriceMenu() {
        return new Menu("totalPriceMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
               try {
                   System.out.print("Total price: ");
                   System.out.println(customerController.getTotalCartPrice());
               }catch (NoLoggedInUserException e){
                   System.err.println(e.getMessage());
               }
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
                "purchase\n"+
                "logout");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine().trim();
        Menu chosenMenu = null;
        if(input.matches("logout"))
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
                    } catch (Exception e) {
                        //there is no group
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
