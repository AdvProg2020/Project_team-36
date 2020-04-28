package View;

import Controllers.SellerController;

import java.util.HashMap;
import java.util.regex.Matcher;

public class SellerMenu extends Menu{

    public SellerMenu(Menu parentMenu) {
        super("SellerMenu", parentMenu);
        subMenus = new HashMap<>();
        subMenus.put("view\\s+balance",getViewBalanceMenu());
    }

    public Menu getViewBalanceMenu(){
        return new Menu("view\\s+balance",this) {
            @Override
            public void execute() {
                try{
                    System.out.println(sellerController.getLoggedInSellerBalance());
                }catch (SellerController.NoLoggedInSellerException e){
                    System.err.println(e.getMessage());
                }
            }

            @Override
            public void help() {
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
}
