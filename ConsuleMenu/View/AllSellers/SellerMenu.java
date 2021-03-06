package View.AllSellers;

import Controllers.SellerController;
import Models.SellerLog;
import View.ManageCategoriesMenu;
import View.Menu;
import View.Products.OffsMenu;
import View.Products.ProductsMenu;

import java.util.HashMap;
import java.util.regex.Matcher;

public class SellerMenu extends Menu {

    public SellerMenu(Menu parentMenu) {
        super("SellerMenu", parentMenu);
        subMenus = new HashMap<>();
        subMenus.put("^add\\s+product$",new AddProductMenu(this));
        subMenus.put("^view\\s+offs$",new ViewOffsMenu(this));
        subMenus.put("^view\\s+balance$",getViewBalanceMenu());
        subMenus.put("^view\\s+company\\s+information$",getViewCompanyInformationMenu());
        subMenus.put("^show\\s+categories$",getShowCategories());
        subMenus.put("^view\\s+sales\\s+history$",getViewSalesHistory());
        subMenus.put("^offs", new OffsMenu("off menu",this));
        subMenus.put("^products$",new ProductsMenu("ProductsMenu",this));
    }

    private Menu getViewBalanceMenu(){
        return new Menu("viewBalanceMenu",this) {
            @Override
            public void execute() {
                    System.out.println("The balance is:"+sellerController.getLoggedInSellerBalance());
            }

            @Override
            public void help() {
            }
        };
    }

    private Menu getViewCompanyInformationMenu(){
        return new Menu("view\\s+company\\s+information",this) {
            @Override
            public void execute() {
                    System.out.println(sellerController.getLoggedInSellerCompanyInformation());
            }
            @Override
            public void help() {}
        };
    }

    private Menu getShowCategories(){
        return new Menu("getShowCategories",this) {
            @Override
            public void help() { }

            @Override
            public void execute() {
                System.out.println("All Categories:");
                ManageCategoriesMenu.printCategoryTree(sellerController.getMainCategory());
            }
        };
    }

    private Menu getViewSalesHistory(){
        return new Menu("getViewSalesHistory",this) {
            @Override
            public void help() { }

            @Override
            public void execute() {
                int number = 1;
                for (SellerLog sellerLog : sellerController.getAllSellerLogs()) {
                    System.out.println(number+") \n"+sellerLog.getSellerLogInfo());
                    number+=1;
                }
            }
        };
    }

    @Override
    public void help() {
        String output = "view personal info" + "\n" + "view company information" + "\n" + "view sales history"
                + "\n" + "manage products" + "\n" + "add product" + "\n" + "remove product [productId]" + "\n"
                + "show categories" + "\n" + "view offs" + "\n" + "view balance";
        System.out.println(output);
    }

    @Override
    public void execute() {
        String input;
        while(!(input= scanner.nextLine().trim()).equalsIgnoreCase("back")){
            if(input.matches("logout"))
                logoutChangeMenu();
            else if(input.matches("help"))
                help();
            else {
                Matcher matcher;
                Menu chosenMenu=null;
                for (String regex : subMenus.keySet()) {
                    if((matcher = getMatcher(input,regex)).matches()){
                        chosenMenu = subMenus.get(regex);
                    }
                }
                if(chosenMenu==null)
                    System.err.println("Invalid command");
                else
                    chosenMenu.execute();
            }
        }
        this.parentMenu.getParentMenu().execute();
    }

}