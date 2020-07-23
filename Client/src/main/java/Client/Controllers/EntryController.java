package Client.Controllers;

import Client.GUI.Constants;
import Client.Models.User;
import Client.Network.Client;
import Models.*;
import Repository.SaveUser;
import com.google.gson.Gson;

public class EntryController {
    private final String controllerName = "EntryController";

    public void setPasswordLogin(String password) throws WrongPasswordException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPasswordLogin");
        query.getMethodInputs().put("password",password);
        Response response = Client.process(query);
        Constants.globalVariables.setLoggedInUser(getLoggedInUser());
        if(response.getReturnType().equalsIgnoreCase("WrongPasswordException"))
            throw new WrongPasswordException("Wrong password!");
    }

    public User getLoggedInUser() {
        Query query = new Query(Constants.globalVariables.getToken(), "UserController", "getLoggedInUser");
        Response response = Client.process(query);
        if (response.getReturnType().equals("User")) {
            Gson gson = new Gson();
            SaveUser saveUser = gson.fromJson(response.getData(), SaveUser.class);
            if(saveUser==null) {
                return null;
            }
            return User.generateUser(saveUser);
        } else {
            return null;
        }
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

    public void setEmail(String email){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setEmail");
        query.getMethodInputs().put("email",email);
        Client.process(query);
    }

    public void setLastname(String lastname){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setLastname");
        query.getMethodInputs().put("lastname",lastname);
        Client.process(query);
    }

    public void setFirstname(String firstname){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFirstname");
        query.getMethodInputs().put("firstname",firstname);
        Client.process(query);
    }

    public void setPhoneNumber(String phoneNumber){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPhoneNumber");
        query.getMethodInputs().put("phoneNumber",phoneNumber);
        Client.process(query);
    }

    public void setPassword(String password){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPassword");
        query.getMethodInputs().put("password",password);
        Client.process(query);
    }

    public void register() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "register");
        Client.process(query);
    }

    public void setLoggedInUser(int userId){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setLoggedInUser");
      query.getMethodInputs().put("userId",Integer.toString(userId));
        Client.process(query);
    }

    public void logout() throws NotLoggedInException {
        Constants.globalVariables.setLoggedInUser(null);
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
