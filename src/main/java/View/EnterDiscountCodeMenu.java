package View;

import Controllers.CustomerController;

public class EnterDiscountCodeMenu extends Menu {

    public EnterDiscountCodeMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("PurchaseMenu",new PurchaseMenu("Purchase Menu",this));
    }

    @Override
    public void help() {
        System.out.println("next\n" +
                "back\n" +
                "logout");
    }

    @Override
    public void execute() {
        System.out.println("enter a discount code then/or type next!");
        String input = scanner.nextLine().trim();
        while (!((input.equalsIgnoreCase("back")) || (input.equalsIgnoreCase("help"))||
                (input.equalsIgnoreCase("logout")))) {
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
            input = scanner.nextLine().trim();
        }
        if (input.matches("back")){
            customerController.cancelPurchase();
            this.parentMenu.execute();}
        else if (input.matches("next")) {
            subMenus.get("Purchase Menu").execute();
        }else if(input.matches("logout")){
            customerController.cancelPurchase();
            logoutChangeMenu();
        }
    }
}
