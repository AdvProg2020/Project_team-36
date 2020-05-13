package View;

import Models.Product;

import java.util.regex.Matcher;

public class ManageAllProductsMenu extends Menu{

    private int id;

    public ManageAllProductsMenu(Menu parentMenu) {
        super("ManageAllProductsMenu", parentMenu);
        subMenus.put("remove\\s+(\\d+)",removeProduct());
    }

    @Override
    public void help() {
        System.out.println("remove [productId]");
    }

    @Override
    public void execute() {
        int number = 1;
        Matcher matcher;
        Menu chosenMenu = null;
        for (Product product : managerController.getAllProducts()) {
            System.out.println(number + ") " + product.getProductId());
            number += 1;
        }
        System.out.println("choose the product you want to remove :");
        String input = scanner.nextLine().trim();
        while (!((input.matches("back")) || (input.matches("help")))) {
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
        if (input.matches("back")) {
            this.parentMenu.execute();
        } else if (input.matches("help")) {
            this.help();
            this.execute();
        }
    }

    private Menu removeProduct() {
        return new Menu("removeProduct", this) {
            @Override
            public void help() {
            }

            @Override
            public void execute() {
                
            }
        };
    }

    //-..-
}
