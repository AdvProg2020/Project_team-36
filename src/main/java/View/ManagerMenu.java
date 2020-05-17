package View;

import View.Products.OffsMenu;
import View.Products.ProductsMenu;

public class ManagerMenu extends Menu {


    public ManagerMenu(Menu parentMenu) {
        super("ManagerMenu",parentMenu);
        subMenus.put("view\\s+personal\\s+info", new PersonalInfoMenu("Personal information",this));
        subMenus.put("manage\\s+categories", new ManageCategoriesMenu(this));
        subMenus.put("manage\\s+requests", new ManageRequestsMenu(this));
        subMenus.put("view\\s+discount\\s+codes", new ViewDiscountCodesMenu(this));
        subMenus.put("manage\\s+users", new ManageUsersMenu(this));
        subMenus.put("manage\\s+all\\s+products", new ManageAllProductsMenu(this));
        subMenus.put("create\\s+discount\\s+code",new CreateDiscountCodeMenu(this));
        subMenus.put("create\\s+new\\s+gift",new GiftMenu("GiftMenu",this));
        subMenus.put("offs",new OffsMenu("Off menu",this));
        subMenus.put("products",new ProductsMenu("ProductsMenu",this));

    }

    @Override
    public void help() {
        System.out.println("view personal info\n" +
                "manage categories\n" +
                "manage requests\n" +
                "view discount codes\n" +
                "create new gift\n" +
                "manage users\n" +
                "manage all products\n" +
                "create discount code\n");

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
        }else{
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




