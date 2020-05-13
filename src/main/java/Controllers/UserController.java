package Controllers;

public abstract class UserController {
    GlobalVariables userVariables;
    public UserController(GlobalVariables userVariables) {
        this.userVariables = userVariables;
    }

    public void setPassword(String password){
        this.userVariables.getLoggedInUser().setPassword(password);
    }

    public void setEmail(String email){
        this.userVariables.getLoggedInUser().setEmail(email);
    }

    public void setFirstname(String firstname){
        this.userVariables.getLoggedInUser().setFirstname(firstname);
    }

    public void setLastname(String lastname){
        this.userVariables.getLoggedInUser().setLastname(lastname);
    }

    public void setPhoneNumber(String phoneNumber){
        this.userVariables.getLoggedInUser().setPhoneNumber(phoneNumber);
    }

    //-..-
}
