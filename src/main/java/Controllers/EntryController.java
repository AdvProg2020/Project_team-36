package Controllers;

import Models.Customer;
import Models.Manager;
import Models.Seller;
import Models.User;

public class EntryController extends Controller {

    public EntryController(GlobalVariables globalVariable) {
        super(globalVariable);
    }

    public void setPasswordLogin(String password) {

    }

    public void setUserNameLogin(String username) {

    }

    public void setUsernameRegister(String username, String type) throws InvalidTypeException, InvalidUsernameException, ManagerExistsException {
        if (!type.matches("([manager|seller|customer])"))
            throw new InvalidTypeException("there is no user with this type");
        else if (User.isThereUsername(username))
            throw new InvalidUsernameException("there is a user with this username");
        else {
            createNewAccount(username, type);
        }
    }

    public void setPasswordRegister(String password){
        userVariables.getLoggedInUser().setPassword(password);
    }

    private void createNewAccount(String username, String type) throws ManagerExistsException {
        User newUser;
        if (type.matches("customer")) {
            newUser = new Customer(username);
            userVariables.setLoggedInUser(newUser);
            User.addNewUser(newUser);
        } else if (type.matches("seller")) {
            newUser = new Seller(username);
        } else {
            if (!Manager.canManagerRegister())
                throw new ManagerExistsException("There is a manager!You cannot register");
            else
                newUser = new Manager(username);
        }
        userVariables.setLoggedInUser(newUser);
    }


    public static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }

    public static class InvalidTypeException extends Exception {
        public InvalidTypeException(String message) {
            super(message);
        }
    }

    public static class ManagerExistsException extends Exception {
        public ManagerExistsException(String message) {
            super(message);
        }
    }

}