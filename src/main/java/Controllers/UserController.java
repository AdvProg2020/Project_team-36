package Controllers;

import Exceptions.NoLoggedInUserException;
import Models.Manager;
import Models.Seller;
import Models.User;

public abstract class UserController {
    GlobalVariables userVariables;
    public UserController(GlobalVariables userVariables) {
        this.userVariables = userVariables;
    }

    public void setPassword(String password)throws NoLoggedInUserException{
        this.userVariables.getLoggedInUser().setPassword(password);
    }

    public void setEmail(String email)throws NoLoggedInUserException{
        this.userVariables.getLoggedInUser().setEmail(email);
    }

    public void setFirstname(String firstname)throws NoLoggedInUserException{
        this.userVariables.getLoggedInUser().setFirstname(firstname);
    }

    public void setLastname(String lastname)throws NoLoggedInUserException{
        this.userVariables.getLoggedInUser().setLastname(lastname);
    }

    public void setPhoneNumber(String phoneNumber)throws NoLoggedInUserException{
        this.userVariables.getLoggedInUser().setPhoneNumber(phoneNumber);
    }

    public String getType() throws NoLoggedInUserException {
        if (userVariables.getLoggedInUser() instanceof Seller){
            return "seller";
        }
        if (userVariables.getLoggedInUser() instanceof Manager){
            return "manager";
        }
        return "customer";
    }

    public String getPersonalInfo() throws NoLoggedInUserException{
        return userVariables.getLoggedInUser().toString();
    }
}
