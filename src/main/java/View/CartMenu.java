package View;

import Controllers.CustomerController;
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


    }

    private Menu getShowCartMenu() {
        return new Menu("ShowCartMenu", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                ArrayList<ItemInCart> cart = customerController.getCart();
                if (cart.isEmpty()) {
                    System.out.println("There is nothing to show!");
                    return;
                }
                System.out.format("%20s%9s%s", "Product name", "ProductId", "count in cart");
                for (ItemInCart item : cart) {
                    Product product = item.getProduct();
                    System.out.format("%20s%9d%10d", product.getName(), product.getProductId(), item.getCount());
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
                if (customerController.isThereProductInCart(productId)) {
                    System.err.println("There is no product with this id in your cart!");
                    return;
                }
                ProductMenu productMenu = new ProductMenu(productId);
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
                    System.err.println(e.getMessage());
                    return;
                } catch (CustomerController.MoreThanOneSellerForItem e) {
                    moreThanOneSeller(e.getSellers());
                } catch (CustomerController.NotEnoughSupply e) {
                    System.err.println("There is not enough supply for this product! ");
                    return;
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
                    input = scanner.nextLine();
                    if (input.matches("back"))
                        return;
                    else if (!input.matches("\\d+") || Integer.parseInt(input) >= i) {
                        System.out.println("invalid command! Try again please");
                    } else {
                        try {
                            customerController.increaseProductInCart(Integer.parseInt(input), productId);
                            System.out.println("Increased successfully");
                            return;
                        } catch (CustomerController.NotEnoughSupply e) {
                            System.err.println("There is not enough supply for product!Try again ");
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
                    customerController.decreaseProductInCart(productId);
                    System.out.println("Decreased successfully");
                } catch (CustomerController.NoProductWithIdInCart e) {
                    System.out.println(e.getMessage());
                } catch (CustomerController.MoreThanOneSellerForItem e) {
                    moreThanOneSeller(e.getSellers());
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
                    input = scanner.nextLine();
                    if (input.matches("back"))
                        return;
                    else if (!input.matches("\\d+") || Integer.parseInt(input) >= i) {
                        System.out.println("invalid command! Try again please");
                    } else {
                        customerController.decreaseProductInCart(Integer.parseInt(input), productId);
                        System.out.println("Decreased successfully");
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
                System.out.print("Total price: ");
                System.out.println(customerController.getTotalCartPrice());
            }
        };
    }

    @Override
    public void help() {
        System.out.println("show products\nview [productId]\n" +
                "increase [productId]\ndecrease [productId]\nshow total price\npurchase");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine().trim();
        Menu chosenMenu = null;
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
                    }
                    break;
                }
            }
            if (chosenMenu == null) {
                System.err.println("Invalid command! Try again please");
                execute();
            } else {
                chosenMenu.execute();
                this.execute();
            }
        }
    }
}
