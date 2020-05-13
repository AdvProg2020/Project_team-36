package Controllers;

import Models.Manager;
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

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}
