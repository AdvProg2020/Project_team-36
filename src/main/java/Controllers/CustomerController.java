package Controllers;

import Exceptions.NoLoggedInUserException;
import Models.Customer;

public class CustomerController extends UserController {
    public CustomerController(GlobalVariables userVariables) {
        super(userVariables);
    }
    public void setCredit(long credit)throws NoLoggedInUserException {
        ((Customer)userVariables.getLoggedInUser()).setCredit(credit);
    }
}
