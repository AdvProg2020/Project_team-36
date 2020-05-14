package Controllers;

import Exceptions.NoLoggedInUserException;
import Models.*;
import View.*;

public class EntryController extends UserController  {
    public EntryController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public void setPasswordLogin(String password, UserAreaMenu userAreaMenu) throws WrongPasswordException , NoLoggedInUserException {
        if(!password.equals(userVariables.getLoggedInUser().getPassword())){
            throw new WrongPasswordException("Wrong password!");
        }else{
            userAreaMenu.newUserMenu(userVariables.getLoggedInUser().getType());
        }
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

    public void setCompany(String name){
        ((Seller)userVariables.getLoggedInUser()).setCompanyName(name);
    }

    public void setCompanyInfo(String info){
        ((Seller) userVariables.getLoggedInUser()).setCompanyInfo(info);
    }

    public void register(){
        User user = userVariables.getLoggedInUser();
        if(user instanceof Seller){
            new Request((Seller) user);
            return;
        }
        User.addNewUser(user);
        if(user instanceof Manager)
            Manager.addNewManager((Manager) user);
        else if(user instanceof Customer)
            Customer.addNewCustomer((Customer)user);
    }

    public void logout() {
        this.userVariables.setLoggedInUser(null);
    }
    public void register() throws NoLoggedInUserException{
        User.addNewUser(userVariables.getLoggedInUser());
    }

    private void createNewAccount(String username, String type) throws ManagerExistsException {
        User newUser;
        if (type.matches("customer")) {
            newUser = new Customer(username);
        } else if (type.matches("seller")) {
            newUser = new Seller(username);
        } else {
            if (!Manager.canManagerRegister()) {
                throw new ManagerExistsException("There is a manager!You cannot register");
            } else {
                newUser = new Manager(username);
            }
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

    public static class WrongPasswordException extends Exception{
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    //-..-
}