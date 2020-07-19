package Controllers;

import GUI.Constants;
import Models.*;
import Network.Client;

public class EntryController {
    private final String controllerName = "EntryController";

    public void setPasswordLogin(String password) throws WrongPasswordException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPasswordLogin");
        query.getMethodInputs().put("password",password);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("WrongPasswordException"))
            throw new WrongPasswordException("Wrong password!");
    }

    public void setUserNameLogin(String username) throws InvalidUsernameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setUserNameLogin");
        query.getMethodInputs().put("username",username);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("InvalidUsernameException"))
            throw new InvalidUsernameException("No user!");
    }

    public void setUsernameRegister(String type, String username) throws InvalidTypeException, InvalidUsernameException, ManagerExistsException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setUsernameRegister");
        query.getMethodInputs().put("type",type);
        query.getMethodInputs().put("username",username);
        Response response = Client.process(query);

        if (response.getReturnType().equalsIgnoreCase("InvalidTypeException"))
            throw new InvalidTypeException("there is no user with this type");
        else if (response.getReturnType().equalsIgnoreCase("InvalidUsernameException"))
            throw new InvalidUsernameException("there is a user with this username");
        else if(response.getReturnType().equalsIgnoreCase("ManagerExistsException"))
            throw new ManagerExistsException("there is a Manager");
    }

    public void setCompany(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCompany");
        query.getMethodInputs().put("name",name);
        Client.process(query);
    }

    public void setCompanyInfo(String info) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCompanyInfo");
        query.getMethodInputs().put("info",info);
        Client.process(query);
    }

    public void register() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "register");
        Client.process(query);
    }

    public void logout() throws NotLoggedInException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "logout");
        if(Client.process(query).getReturnType().equalsIgnoreCase("NotLoggedInException"))
            throw new NotLoggedInException();
    }

    public boolean isUserLoggedIn() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "isUserLoggedIn");
        Response response =Client.process(query);
        return Boolean.getBoolean(response.getData());
    }

    public void setImage(String path) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setImage");
        query.getMethodInputs().put("path",path);
        Response response =Client.process(query);
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

    public static class WrongPasswordException extends Exception {
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    public static class NotLoggedInException extends Exception {
    }
}
