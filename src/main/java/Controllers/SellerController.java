package Controllers;

import Models.Seller;
import Models.User;

public class SellerController extends UserController {

    public SellerController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public long getLoggedInSellerBalance() throws NoLoggedInSellerException {
        User loggedInUser = userVariables.getLoggedInUser();
        if (loggedInUser == null) {
            throw new NoLoggedInSellerException("No user has logged in");
        }
        if (loggedInUser instanceof Seller) {
            throw new NoLoggedInSellerException("The logged in user is not seller");
        }

        return ((Seller) loggedInUser).getCredit();



    }

    public class NoLoggedInSellerException extends Exception {
        public NoLoggedInSellerException(String message) {
            super(message);
        }
    }
}

