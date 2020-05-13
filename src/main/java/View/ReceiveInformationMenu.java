package View;

import Controllers.CustomerController;

public class ReceiveInformationMenu extends Menu {

    public ReceiveInformationMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("next menu", new EnterDiscountCodeMenu("DiscountCodeMenu", this));
    }

    @Override
    public void help() {
        System.out.println("logout\n");
    }

    @Override
    public void execute() {
        if (customerController.isThereAvailableItemInCart()) {
            System.err.println("There is nothing available in your cart! Returning to cart menu...");
            return;
        }
        String input;
        System.out.println("Enter your complete address");
        if ((input = scanner.nextLine().trim()).matches("back"))
            return;
        else if (input.matches("logout"))
            logoutChangeMenu();

        customerController.setAddressForPurchase(input);
        System.out.println("Enter your phone number:");
        while (!(input = scanner.nextLine().trim()).matches("back|logout")) {
            if (input.matches("\\d+")) {
                customerController.setPhoneNumberForPurchase(input);
                break;
            } else {
                System.err.println("Wrong format of phone number! Try again");
            }
        }
        if (input.matches("back"))
            return;
        else if(input.matches("logout"))
            logoutChangeMenu();
        subMenus.get("Discount Code Menu").execute();

    }
}
