package Controllers;

public interface UsersController {
    default public void setPassword(String password){
    }
    //NOTE:   haminjuri ye alame set bara password, email,phone number,first name,lastname....
}
