package Controllers;

import GUI.Constants;
import Models.Manager;
import Models.Query;
import Models.Response;
import Models.User;
import Network.Client;

public class NewManagerController {
    private String controllerName = "NewManagerController";


    public void setUsername(String username) throws InvalidInputException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setUsername");
        query.getMethodInputs().put("username", username);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidInputException")) {
            throw new InvalidInputException();
        }
    }

    public void setFirstname(String firstname) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFirstname");
        query.getMethodInputs().put("firstname", firstname);
        Client.process(query);
    }

    public void setLastname(String lastname) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setLastnamel");
        query.getMethodInputs().put("lastname", lastname);
        Client.process(query);
    }

    public void setEmail(String email) throws InvalidInputException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setEmail");
        query.getMethodInputs().put("email", email);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidInputException")) {
            throw new InvalidInputException();
        }
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidInputException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPhoneNumber");
        query.getMethodInputs().put("phoneNumber", phoneNumber);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidInputException")) {
            throw new InvalidInputException();
        }
    }

    public void setPassword(String password) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPassword");
        query.getMethodInputs().put("password", password);
        Client.process(query);    }

    public void finalizeMakingNewManagerProfile() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "finalizeMakingNewManagerProfile");
        Client.process(query);
    }

    public static class InvalidInputException extends Exception {

    }
}
