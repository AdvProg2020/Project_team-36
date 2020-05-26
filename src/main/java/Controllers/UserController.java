package Controllers;
import Models.Customer;
import Models.Seller;
import Models.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
public abstract class UserController {

    private HashMap<String, Method> generalInfoMethods;
    private HashMap<String, Method> sellerInfoMethods;
    protected GlobalVariables userVariables;
    GlobalVariables userVariables;
    public UserController(GlobalVariables userVariables) {
        this.userVariables = userVariables;
        this.generalInfoMethods = new HashMap<>();
        this.sellerInfoMethods = new HashMap<>();
        setHashmaps();
    }

    private void setHashmaps(){
        try {
            Method method = User.class.getDeclaredMethod("setFirstname", String.class);
            this.generalInfoMethods.put("firstname", method);
            method = User.class.getDeclaredMethod("setLastname", String.class);
            this.generalInfoMethods.put("lastname", method);
            method = User.class.getDeclaredMethod("setEmail", String.class);
            this.generalInfoMethods.put("email", method);
            method = User.class.getDeclaredMethod("setPassword", String.class);
            this.generalInfoMethods.put("password", method);
            method = User.class.getDeclaredMethod("setPhoneNumber", String.class);
            this.generalInfoMethods.put("phone", method);
            method = Seller.class.getDeclaredMethod("setCompanyName", String.class);
            this.sellerInfoMethods.put("company name", method);
            method = Seller.class.getDeclaredMethod("setCompanyInfo", String.class);
            this.sellerInfoMethods.put("company info", method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public User getLoggedInUser(){
        return userVariables.getLoggedInUser();
    }

    public void editInfo(String type,String newQuality) throws NoFieldWithThisType {
        if(userVariables.getLoggedInUser() instanceof Seller){
            for (String name : sellerInfoMethods.keySet()) {
                if(name.equalsIgnoreCase(type)){
                    try {
                        sellerInfoMethods.get(name).invoke(userVariables.getLoggedInUser(),newQuality);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new NoFieldWithThisType();
                    }
                }
            }
        }
        for (String name : generalInfoMethods.keySet()) {
            if(name.equalsIgnoreCase(type)){
                try {
                    generalInfoMethods.get(name).invoke(userVariables.getLoggedInUser(),newQuality);
                    return;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new NoFieldWithThisType();

                }
            }

        }
        throw new NoFieldWithThisType();
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


    public static class NoFieldWithThisType extends Exception{}

}
