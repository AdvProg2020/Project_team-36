package Controllers;

import Exceptions.NoLoggedInSellerException;
import Exceptions.NoLoggedInUserException;
import Exceptions.NoProductForThisSellerException;
import Exceptions.NoProductWithThisIdException;
import Models.Product;
import Models.Seller;
import Models.User;

public class SellerController extends UserController {

    public SellerController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public long getLoggedInSellerBalance() {
        return ((Seller)userVariables.getLoggedInUser()).getCredit();
    }

    public String getLoggedInSellerCompanyInformation(){
        String output = "";
        output += "company Name:" + ((Seller) userVariables.getLoggedInUser()).getCompanyName();
        if (!((Seller)userVariables.getLoggedInUser()).getCompanyInfo().equals("")){
            output += "\n" + "Info:" + ((Seller) userVariables.getLoggedInUser()).getCompanyInfo();
        }
        return output;
    }

    public void removeSellerProduct(int productId) throws NoProductForSeller {
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        if(!seller.isThereProduct(productId))
            throw new NoProductForSeller();
        Product.getProduct(productId).removeSeller(seller);
    }

    public void setCompanyInfo(String companyInfo)throws NoLoggedInUserException{
        ((Seller)userVariables.getLoggedInUser()).setCompanyInfo(companyInfo);
    }


    public static class NoProductForSeller extends Exception{

    }
}
