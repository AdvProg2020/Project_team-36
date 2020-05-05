package View;

import Controllers.CustomerController;

public class ReceiveInformationMenu extends Menu {

    public ReceiveInformationMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
        subMenus.put("next menu", new EnterDiscountCodeMenu("DiscountCodeMenu", this));
    }

    @Override
    public void help() {
    }

    @Override
    public void execute() {
        String input;
        System.out.println("Enter your complete address");
        if ((input = scanner.nextLine().trim()).matches("back"))
            return;
        try {
            customerController.setAddressForPurchase(input);
        } catch (CustomerController.EmptyCart e) {
            System.err.println("Empty cart!");
            return;
        }
        System.out.println("Enter your phone number:");
        while (!(input = scanner.nextLine().trim()).matches("back")) {
            if (input.matches("\\d+")) {
                customerController.setPhoneNumberForPurchase(input);
                break;
            } else {
                System.err.println("Wrong format of phone number! Try again");
            }
        }
        if (input.matches("back"))
            return;
        subMenus.get("Discount Code Menu").execute();

    }
}
