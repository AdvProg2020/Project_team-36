package View;

import View.Products.OffsMenu;
import View.Products.ProductsMenu;

public class MainMenu extends Menu {

    public MainMenu() {
        super("MainMenu", null);
        this.subMenus.put("user\\s+area", new UserAreaMenu(this));
        subMenus.put("offs",new OffsMenu("Off menu",this));
        subMenus.put("products",new ProductsMenu("ProductsMenu",this));
    }


    @Override
    public void help() {
        System.out.println("user area\n" +
                "offs\n" +
                "products");
    }

    @Override
    public void execute() {
        String input = scanner.nextLine().trim();
        Menu chosenMenu = null;

        if (input.matches("help")) {
            help();
            execute();
        } else if(input.matches("logout")&&subMenus.get("user\\s+area").getSubMenus().containsKey("UserMenu")){
            logoutChangeMenu();
        }
        else {
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
