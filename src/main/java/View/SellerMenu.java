package View;

import Controllers.SellerController;
import Exceptions.NoLoggedInSellerException;
import Exceptions.NoLoggedInUserException;
import Exceptions.NoProductForThisSellerException;
import Exceptions.NoProductWithThisIdException;
import Models.Product;
import Models.Seller;

import java.util.HashMap;
import java.util.regex.Matcher;

public class SellerMenu extends Menu{

    public SellerMenu(Menu parentMenu) {
        super("SellerMenu", parentMenu);
        subMenus = new HashMap<>();
        subMenus.put("view\\s+balance",getViewBalanceMenu());
        subMenus.put("view\\s+company\\s+information",getViewCompanyInformationMenu());
        subMenus.put("remove\\s+product\\s+(\\d+)",getRemoveProductMenu());
        subMenus.put("view\\s+personal\\s+info",new PersonalInfoMenu(this));
    }

    public Menu getViewBalanceMenu(){
        return new Menu("view\\s+balance",this) {
            @Override
            public void execute() {
                try{
                    System.out.println(sellerController.getLoggedInSellerBalance());
                }catch (NoLoggedInSellerException|NoLoggedInUserException e ){
                    System.err.println(e.getMessage());
                }
            }

            @Override
            public void help() {}
        };
    }

    public Menu getViewCompanyInformationMenu(){
        return new Menu("view\\s+company\\s+information",this) {
            @Override
            public void execute() {
                try {
                    System.out.println(sellerController.getLoggedInSellerCompanyInformation());
                }catch (NoLoggedInSellerException|NoLoggedInUserException e ){
                    System.err.println(e.getMessage());
                }
            }

            @Override
            public void help() {}
        };
    }

    public Menu getRemoveProductMenu(){
        return new Menu("remove\\s+product\\s+(\\d+)",this) {
            Matcher matcher = getMatcher(this.getName(),"remove\\s+product\\s+(\\d+)");
            int productId = Integer.parseInt(matcher.group(1));

            @Override
            public void execute() {
                try {
                    Product productToBeRemoved = productController.getProductById(productId);
                    Seller loggedInSeller = sellerController.getLoggedInSeller();
                    sellerController.removeSellerProduct(productToBeRemoved);
                    productController.removeSellerFromProduct(productToBeRemoved,loggedInSeller);
                }catch (NoLoggedInUserException|NoLoggedInSellerException|NoProductWithThisIdException|NoProductForThisSellerException e ){
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
