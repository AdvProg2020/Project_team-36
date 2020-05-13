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

    public long getLoggedInSellerBalance() throws NoLoggedInSellerException, NoLoggedInUserException {
        User loggedInUser = userVariables.getLoggedInUser();
        if (!(loggedInUser instanceof Seller)) {
            throw new NoLoggedInSellerException("The logged in user is not seller");
        }
        return ((Seller) loggedInUser).getCredit();
    }

    public String getLoggedInSellerCompanyInformation() throws NoLoggedInSellerException, NoLoggedInUserException{
        User loggedInUser = userVariables.getLoggedInUser();
        String output = "";
        if (!(loggedInUser instanceof Seller)) {
            throw new NoLoggedInSellerException("The logged in user is not seller");
        }
        output += "company Name:" + ((Seller) loggedInUser).getCompanyName();
        if (!((Seller) loggedInUser).getCompanyInfo().equals("")){
            output += "\n" + "Info:" + ((Seller) loggedInUser).getCompanyInfo();
        }
        return output;
    }

    public void removeSellerProduct(Product product) throws NoLoggedInUserException,NoLoggedInSellerException,NoProductForThisSellerException {
        Seller loggedInSeller = getLoggedInSeller();
        if(!loggedInSeller.getAllProducts().contains(product)){
            throw new NoProductForThisSellerException("This seller does not sell this product!");
        }
        loggedInSeller.getAllProducts().remove(product);
    }

    public Seller getLoggedInSeller()throws NoLoggedInSellerException,NoLoggedInUserException{
        if (userVariables.getLoggedInUser() instanceof Seller){
            return (Seller) userVariables.getLoggedInUser();
        }
        throw new NoLoggedInSellerException("The logged in user is not seller");
    }

    public void setCredit(long credit) throws NoLoggedInUserException{
        ((Seller)userVariables.getLoggedInUser()).setCredit(credit);
    }

    public void setCompanyName(String companyName)throws NoLoggedInUserException{
        ((Seller)userVariables.getLoggedInUser()).setCompanyName(companyName);
    }

    public void setCompanyInfo(String companyInfo)throws NoLoggedInUserException{
        ((Seller)userVariables.getLoggedInUser()).setCompanyInfo(companyInfo);
    }
}

