package View;

import Controllers.CustomerController;
import Models.*;

import java.util.ArrayList;

public class CartMenu extends Menu {
    private int productId;

    public CartMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("show\\s+products", getShowCartMenu());
        subMenus.put("view\\s+(\\d+)", getViewProductMenu());
        subMenus.put("decrease\\s+(\\d+)", getIncreaseAmountMenu());


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
            public void execute() throws Exception {
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
                    else if (input.matches("\\d+") && Integer.parseInt(input) >= i)
                        System.out.println("invalid command! Try again please");
                    else {
                        try {
                            customerController.increaseProductInCart(Integer.parseInt(input), productId);
                            System.out.println("Increased successfully");
                        } catch (CustomerController.NotEnoughSupply e) {
                            System.err.println("There is not enough supply for product!Try again ");
                            return;
                        }

                    }
                }
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
//if matcher.group(!1) vojud dasht eriz tuye productId
    }
}
