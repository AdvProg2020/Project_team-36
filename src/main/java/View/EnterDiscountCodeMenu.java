package View;

import Controllers.CustomerController;

public class EnterDiscountCodeMenu extends Menu {

    public EnterDiscountCodeMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("PurchaseMenu",new PurchaseMenu("Purchase Menu",this));
    }

    @Override
    public void help() {
        System.out.println("next\nback");
    }

    @Override
    public void execute() {
        String input;
        System.out.println("enter a discount code then/or type next!");
        while ((input = scanner.nextLine()).matches("back|next")) {
            if (input.matches("\\d+")) {
                try {
                    customerController.setDiscountCodeForPurchase(Integer.parseInt(input));
                    System.out.println("Discount added successfully");
                } catch (CustomerController.NoDiscountAvailableWithId e) {
                    System.err.println("There is no available discount with this code for you! Try again or type next!");
                }
            } else if (input.matches("help")) {
                help();
            } else {
                System.err.println("Invalid type of discount code! Try again or press next");
            }
        }
        if (input.matches("back"))
            this.parentMenu.execute();
        else if (input.matches("next")) {
            subMenus.get("Purchase Menu").execute();
        }
    }
}
