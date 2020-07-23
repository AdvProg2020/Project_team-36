package View;

import Controllers.CustomerController;
import Models.SelectedItem;

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
        try {
            customerController.startPurchase();
        } catch (CustomerController.EmptyCart emptyCart) {
            System.err.println("Empty cart!");
            return;
        } catch (CustomerController.NotEnoughSupplyInCart notEnoughSupplyInCart) {
            System.out.println("There is/are items in your cart that don't have enough supply");
            System.out.println("items:");
            for (SelectedItem item : notEnoughSupplyInCart.getItems()) {
                System.out.println(item.getProduct().getName());
            }
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
