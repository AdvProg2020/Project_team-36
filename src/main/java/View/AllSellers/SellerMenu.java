package View.AllSellers;

import Controllers.SellerController;
import Models.Seller;
import View.Menu;
import View.Products.OffsMenu;
import View.Products.ProductsMenu;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.HashMap;
import java.util.regex.Matcher;

public class SellerMenu extends Menu {
    private int productId;

    public SellerMenu(Menu parentMenu) {
        super("SellerMenu", parentMenu);
        subMenus = new HashMap<>();
        subMenus.put("view\\s+balance",getViewBalanceMenu());
        subMenus.put("view\\s+company\\s+information",getViewCompanyInformationMenu());
        subMenus.put("remove\\s+product\\s+(\\d+)",getRemoveProductMenu());
        subMenus.put("offs", new OffsMenu("off menu",this));
        subMenus.put("products",new ProductsMenu("ProductsMenu",this));
    }

    public Menu getRemoveProductMenu(){
        return new Menu("remove\\s+product\\s+(\\d+)",this) {
            @Override
            public void execute() {
                try {
                    sellerController.removeSellerProduct(productId);
                    System.out.println("Product removed!");
                } catch (SellerController.NoProductForSeller noProductForSeller) {
                    System.err.println("There is no product with this id in your products!");
                }
            }

            @Override
            public void help() {
            }
        };
    }


    public Menu getViewBalanceMenu(){
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

    public Menu getViewCompanyInformationMenu(){
        return new Menu("view\\s+company\\s+information",this) {
            @Override
            public void execute() {
                    System.out.println(sellerController.getLoggedInSellerCompanyInformation());
            }
            @Override
            public void help() {}
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
                        try{
                            productId = Integer.parseInt(matcher.group(1));
                        }catch (Exception e){
                            //DO NOTHING
                        }
                    }
                }
                if(chosenMenu==null)
                    System.err.println("Invalid command");
                else
                    chosenMenu.execute();
            }
        }
    }
}