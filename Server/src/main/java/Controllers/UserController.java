package Controllers;

import Models.Query;
import Models.Response;
import Models.Seller;
import Models.User;
import Repository.SaveUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class  UserController {
    private HashMap<String, Method> generalInfoMethods;
    private HashMap<String, Method> sellerInfoMethods;
   protected GlobalVariables userVariables;
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

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "getLoggedInUser" -> processGetLoggedInUser(query);
            case "editInfo" -> processEditInfo(query);
            case "setPassword" -> processSetPassword(query);
            case "setEmail" -> processSetEmail(query);
            case "setFirstname" -> processSetFirstname(query);
            case "setLastname" -> processSetLastname(query);
            case "setPhoneNumber" -> processSetPhoneNumber(query);
            default -> new Response("Error", "");
        };
    }

    private Response processSetPhoneNumber(Query query) {
        String phoneNumber = query.getMethodInputs().get("phoneNumber");
        setPhoneNumber(phoneNumber);
        return new Response("void","");
    }

    private Response processSetLastname(Query query) {
        String lastname = query.getMethodInputs().get("lastname");
        setLastname(lastname);
        return new Response("void","");
    }

    private Response processSetFirstname(Query query) {
        String firstname = query.getMethodInputs().get("firstname");
        setFirstname(firstname);
        return new Response("void","");
    }

    private Response processSetEmail(Query query) {
        String email = query.getMethodInputs().get("email");
        setEmail(email);
        return new Response("void","");
    }

    private Response processSetPassword(Query query) {
        String password = query.getMethodInputs().get("password");
        setPassword(password);
        return new Response("void","");
    }

    private Response processEditInfo(Query query) {
        String type = query.getMethodInputs().get("type");
        String newQuality = query.getMethodInputs().get("newQuality");
        try {
            editInfo(type,newQuality);
            return new Response("void","");
        } catch (NoFieldWithThisType noFieldWithThisType) {
            return new Response("NoFieldWithThisType","");
        }
    }

    private Response processGetLoggedInUser(Query query) {
        SaveUser saveUser = new SaveUser(getLoggedInUser());
        Gson gson = new GsonBuilder().create();
        String saveSaleGson = gson.toJson(saveUser);
        return new Response("User", saveSaleGson);
    }


    public static class NoFieldWithThisType extends Exception{}

}
