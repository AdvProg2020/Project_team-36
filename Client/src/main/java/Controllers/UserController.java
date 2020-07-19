package Controllers;

import GUI.Constants;
import Models.*;
import Network.Client;
import Repository.SaveUser;
import com.google.gson.Gson;

public class UserController {
    private String controllerName = "UserController";

    public User getLoggedInUser(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getLoggedInUser");
        Response response = Client.process(query);
        if (response.getReturnType().equals("User")) {
            Gson gson = new Gson();
            SaveUser saveUser = gson.fromJson(response.getData(), SaveUser.class);
            return User.generateUser(saveUser);
        } else {
            return null;
        }
    }

    public void editInfo(String type,String newQuality) throws NoFieldWithThisType {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editInfo");
        query.getMethodInputs().put("type", type);
        query.getMethodInputs().put("newQuality", newQuality);
        Response response = Client.process(query);
        if (response.getReturnType().equals("NoFieldWithThisType")) {
            throw new NoFieldWithThisType();
        }
    }

    public void setPassword(String password){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPassword");
        query.getMethodInputs().put("password", password);
        Client.process(query);
    }

    public void setEmail(String email) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setEmail");
        query.getMethodInputs().put("email", email);
        Client.process(query);
    }

    public void setFirstname(String firstname){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFirstname");
        query.getMethodInputs().put("firstname", firstname);
        Client.process(query);    }

    public void setLastname(String lastname){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setLastnamel");
        query.getMethodInputs().put("lastname", lastname);
        Client.process(query);    }

    public void setPhoneNumber(String phoneNumber){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPhoneNumber");
        query.getMethodInputs().put("phoneNumber", phoneNumber);
        Client.process(query);
    }

    public static class NoFieldWithThisType extends Exception{}

}
