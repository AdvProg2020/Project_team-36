package Controllers;

import Exceptions.NoLoggedInSellerException;
import Exceptions.NoLoggedInUserException;
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

    public void removeSellerProduct(Product product) throws NoLoggedInUserException,NoLoggedInSellerException {
        User loggedInUser = userVariables.getLoggedInUser();
        if (!(loggedInUser instanceof Seller)) {
            throw new NoLoggedInSellerException("The logged in user is not seller");
        }
        Seller loggedInSeller = (Seller) loggedInUser;
        loggedInSeller.getAllProducts().remove(product);
    }

    public Seller getLoggedInSeller()throws NoLoggedInSellerException,NoLoggedInUserException{
        if (userVariables.getLoggedInUser() instanceof Seller){
            return (Seller) userVariables.getLoggedInUser();
        }
        throw new NoLoggedInSellerException("The logged in user is not seller");
    }
}

