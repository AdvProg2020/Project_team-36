package Controllers;

import Models.Manager;
import Models.Query;
import Models.Response;
import Models.User;

public class NewManagerController {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String password;

    public void setUsername(String username) throws InvalidInputException {
         if (User.isThereUsername(username))
            throw new InvalidInputException("there is a user with this username");
        else {
            this.username = username;
        }
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) throws InvalidInputException {
        if (email.matches("\\S+@\\S+\\.\\S+")) {
            this.email = email;
        } else {
            throw new InvalidInputException("invalid form of email!\n Correct format: abcd@abc.abc");
        }
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidInputException {
        if (phoneNumber.matches("\\d+")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new InvalidInputException("invalid phone number! Try again");
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void finalizeMakingNewManagerProfile(){
        Manager newManager = new Manager(username);
        newManager.setFirstname(firstname);
        newManager.setLastname(lastname);
        newManager.setPhoneNumber(phoneNumber);
        newManager.setPassword(password);
        newManager.setEmail(email);
        Manager.addNewManager(newManager);
        User.addNewUser(newManager);
    }

    public Response processQuery(Query query) {
        switch (query.getMethodName()) {
            case "setUsername":
                return processSetUsername(query);

            case "setFirstname":
                return processSetFirstname(query);

            case "setLastname":
                return processSetLastname(query);

            case "setEmail":
                return processSetEmail(query);

            case "setPhoneNumber":
                return processSetPhoneNumber(query);

            case "setPassword":
                return processSetPassword(query);

            case "finalizeMakingNewManagerProfile":
                return processFinalizeMakingNewManagerProfile();

            default:
                return new Response("Error", "");
        }
    }

    private Response processSetUsername(Query query){
        try {
            setUsername(query.getMethodInputs().get("username"));
            return new Response("void", "");
        } catch (InvalidInputException e) {
            return new Response("InvalidInputException", "");
        }
    }

    private Response processSetFirstname(Query query){
        setFirstname(query.getMethodInputs().get("firstname"));
        return new Response("void", "");
    }

    private Response processSetLastname(Query query){
        setLastname(query.getMethodInputs().get("lastname"));
        return new Response("void", "");
    }

    private Response processSetPassword(Query query){
        setPassword(query.getMethodInputs().get("password"));
        return new Response("void", "");
    }

    private Response processSetEmail(Query query){
        try {
            setEmail(query.getMethodInputs().get("email"));
            return new Response("void", "");
        } catch (InvalidInputException e) {
            return new Response("InvalidInputException", "");
        }
    }

    private Response processSetPhoneNumber(Query query){
        try {
            setPhoneNumber(query.getMethodInputs().get("phoneNumber"));
            return new Response("void", "");
        } catch (InvalidInputException e) {
            return new Response("InvalidInputException", "");
        }
    }

    private Response processFinalizeMakingNewManagerProfile(){
        finalizeMakingNewManagerProfile();
        return new Response("void", "");
    }

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}
