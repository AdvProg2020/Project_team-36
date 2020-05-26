package View;

import Controllers.ProductsController;
import Models.Discount;
import View.Products.OffsMenu;
import View.Products.ProductsMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class CustomerMenu extends Menu {
    public CustomerMenu(String name, Menu parentMenu) {
        super("customerMenu", parentMenu);
        subMenus.put("view\\s+personal\\s+info", new PersonalInfoMenu("personal information", this));
        subMenus.put("view\\s+cart", new CartMenu("cartMenu", this));
        subMenus.put("view\\s+discount\\s+codes", getDiscountCodesMenu());
        subMenus.put("view\\s+balance", getBalanceMenu());
        subMenus.put("view\\s+orders", new OrderMenu("orderMenu", this));
        subMenus.put("offs", new OffsMenu("Off menu", this));
        subMenus.put("products", new ProductsMenu("ProductsMenu", this));
    }

    @Override
    public void help() {
        System.out.println("view personal info\n" +
                "view cart\n" +
                "view orders\n" +
                "view balance\n" +
                "view discount codes\n" +
                "logout");
    }

    private Menu getDiscountCodesMenu() {
        return new Menu("DiscountCodeMenu", this) {
            @Override
            public void help() {
                System.out.println("back\n" +
                        "logout\n" +
                        "sort by [field] ascending\\descending");
                System.out.println("Fields can be:percent/end time/limit");
            }

            @Override
            public void execute() {
                ArrayList<Discount> discounts = customerController.getDiscountCodes();
                if (discounts.isEmpty()) {
                    System.out.println("You have no available discount!");
                    this.getParentMenu().execute();
                }
                printDiscounts(discounts);
                String input;
                Matcher matcher;
                while (!(input = scanner.nextLine().trim()).matches("back|logout")) {
                    if (input.matches("help"))
                        this.help();
                    else if ((matcher = getMatcher(input, "sort\\s+by\\s+(.*)\\s+(.*)")).matches()) {
                        if (matcher.group(2).matches("ascending|descending")) {
                            try {
                                printDiscounts(customerController.sortDiscounts(matcher.group(1), matcher.group(2)));
                            } catch (ProductsController.NoSortException e) {
                                System.err.println("Three is no type with this name!");
                            }
                        }
                        else System.out.println("invalid type!");
                    }

                }
                if(input.matches("logout"))
                    logoutChangeMenu();
                else if(input.matches("back"))
                    this.parentMenu.execute();
            }
        };
    }

    private void printDiscounts(ArrayList<Discount> discounts) {
        System.out.println("Discount code    percent    EndTime");
        for (Discount discount : discounts) {
            System.out.printf("%5s%18s%35s", discount.getId(), discount.getDiscountPercent()*100, discount.getEndTime());
            System.out.println();
        }
    }

    private Menu getBalanceMenu() {
        return new Menu("showBalance", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                System.out.println("your total balance is: " + customerController.getBalance());
                this.getParentMenu().execute();
            }
        };
    }

    public void execute() {
        String input = scanner.nextLine().trim();
        Menu chosenMenu = null;
        if (input.matches("back")) {
            parentMenu.getParentMenu().execute();

        } else if (input.matches("help")) {
            help();
            this.execute();
        } else if (input.matches("logout")) {
            logoutChangeMenu();
        } else {
            for (String regex : subMenus.keySet()) {
                if (input.matches(regex)) {
                    chosenMenu = subMenus.get(regex);
                    break;
                }
            }
        }
        if (chosenMenu != null)
            chosenMenu.execute();
        else {
            System.err.println("Invalid command! Try again please!");
            this.execute();
        }

    }

}
