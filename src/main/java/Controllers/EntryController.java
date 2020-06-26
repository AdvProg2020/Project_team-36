package Controllers;

import Models.*;
import View.*;

public class EntryController extends UserController  {
    UserAreaMenu userAreaMenu;
    public EntryController(GlobalVariables userVariables) {
        super(userVariables);

    }

    public void setUserAreaMenu(UserAreaMenu userAreaMenu) {
        this.userAreaMenu = userAreaMenu;
    }

    public void setPasswordLogin(String password) throws WrongPasswordException{
        if(!password.equals(userVariables.getLoggedInUser().getPassword())){
            throw new WrongPasswordException("Wrong password!");
        }
            //  else{
     //       //TODO change for consule
            //userAreaMenu.newUserMenu(userVariables.getLoggedInUser().getType());
        //}
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
        User.addUsername(user.getUsername());
        if(user instanceof Seller){
            new Request((Seller) user,Status.TO_BE_ADDED);
            return;
        }
        User.addNewUser(user);
        if(user instanceof Manager)
            Manager.addNewManager((Manager) user);
        else if(user instanceof Customer)
            Customer.addNewCustomer((Customer)user);
    }

    public void logout() throws NotLoggedInException {
        if(this.userVariables.getLoggedInUser()!= null)
        this.userVariables.setLoggedInUser(null);
        else
            throw new  NotLoggedInException();

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

    public boolean isUserLoggedIn(){
        if(userVariables.getLoggedInUser()!= null)
            return true;
        return false;
    }

    public void setImage(String path){
        userVariables.getLoggedInUser().setImageURL(path);
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

    public static class NotLoggedInException extends Exception{}

    //-..-
}