package Controllers;

import Models.Customer;
import Models.Manager;
import Models.Seller;
import Models.User;
import View.*;

public class EntryController extends UserController  {
    public EntryController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public void setPasswordLogin(String password, UserAreaMenu userAreaMenu) throws WrongPasswordException{
        if(!password.equals(userVariables.getLoggedInUser().getPassword())){
            throw new WrongPasswordException("Wrong password!");
        }else{
            userAreaMenu.newUserMenu(userVariables.getLoggedInUser().getType());
        }
    }

    public void logout(){
        this.userVariables.setLoggedInUser(null);
    }

    public void setUserNameLogin(String username) throws InvalidUsernameException{
        if(!User.isThereUsername(username)){
            throw new InvalidUsernameException("There is no user with this username");
        }
        else{
            userVariables.setLoggedInUser(User.getUserByUsername(username));
        }
    }

    public void setUsernameRegister(String type, String username) throws InvalidTypeException, InvalidUsernameException, ManagerExistsException {
        if (!type.matches("customer|manager|seller"))
            throw new InvalidTypeException("there is no user with this type");
        else if (User.isThereUsername(username))
            throw new InvalidUsernameException("there is a user with this username");
        else {
            createNewAccount(username, type);
        }
    }

    public void register(){
        User.addNewUser(userVariables.getLoggedInUser());
    }

    private void createNewAccount(String username, String type) throws ManagerExistsException {
        User newUser;
        if (type.matches("customer")) {
            newUser = new Customer(username);
            userVariables.setLoggedInUser(newUser);
        } else if (type.matches("seller")) {
            newUser = new Seller(username);
        } else {
            if (!Manager.canManagerRegister()) {
                throw new ManagerExistsException("There is a manager!You cannot register");
            } else {
                newUser = new Manager(username);
                Manager.setMainManager((Manager) newUser);
                Manager.addNewManager((Manager) newUser);
            }
            userVariables.setLoggedInUser(newUser);
        }
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

    public static class WrongPasswordException extends Exception{
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    //-..-
}