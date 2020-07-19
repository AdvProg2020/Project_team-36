package Controllers;

import GUI.Constants;
import Models.*;
import Network.Client;
import Repository.SaveCategory;
import Repository.SaveCustomer;
import Repository.SaveDiscount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscountController {
    private String controllerName = "DiscountController";

    public void setStartTime(Date startTime) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setStartTime");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String startTimeString = dateFormat.format(startTime);
        query.getMethodInputs().put("startTime", startTimeString);
        Client.process(query);
    }

    public void setEndTime(Date endTime) throws EndDateBeforeStartDateException, EndDatePassedException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setEndTime");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String endTimeString = dateFormat.format(endTime);
        query.getMethodInputs().put("endTime", endTimeString);
        Response response = Client.process(query);
        if (response.getReturnType().equals("EndDateBeforeStartDateException")) {
            throw new EndDateBeforeStartDateException();
        } else if (response.getReturnType().equals("EndDatePassedException")) {
            throw new EndDatePassedException();
        }
    }

    public void setDiscountPercent(double discountPercent) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setDiscountPercent");
        String discountPercentString = Double.toString(discountPercent);
        query.getMethodInputs().put("discountPercent", discountPercentString);
        Client.process(query);
    }

    public void setDiscountLimit(long discountLimit) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setDiscountLimit");
        String discountLimitString = Long.toString(discountLimit);
        query.getMethodInputs().put("discountLimit", discountLimitString);
        Client.process(query);
    }

    public void setRepetitionForEachUser(int repetitionForEachUser) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setRepetitionForEachUser");
        String repetitionForEachUserString = Integer.toString(repetitionForEachUser);
        query.getMethodInputs().put("repetitionForEachUser", repetitionForEachUserString);
        Client.process(query);
    }

    public ArrayList<Customer> getAllCustomers() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllCustomers");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Customer> allCustomers = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveCustomer>>() {
        }.getType();
        List<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(), type);
        allSaveCustomers.forEach(saveCustomer -> allCustomers.add(new Customer(saveCustomer)));
        return allCustomers;
    }

    public void setCustomersForDiscountCode(String username) throws InvalidUsernameException, CustomerAlreadyAddedException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCustomersForDiscountCode");
        query.getMethodInputs().put("username", username);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidUsernameException")) {
            throw new InvalidUsernameException();
        } else if (response.getReturnType().equals("CustomerAlreadyAddedException")) {
            throw new CustomerAlreadyAddedException();
        }
    }

    public boolean isThereCustomerWithUsername(String username) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "isThereCustomerWithUsername");
        query.getMethodInputs().put("username", username);
        Response response = Client.process(query);
        Gson gson = new Gson();
        return gson.fromJson(response.getData(), Boolean.class);
    }

    public void finalizeTheNewDiscountCode() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "finalizeTheNewDiscountCode");
        Client.process(query);
    }

    public Discount getDiscount() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getDiscount");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Discount")) {
            Gson gson = new Gson();
            SaveDiscount saveDiscount = gson.fromJson(response.getData(), SaveDiscount.class);
            return new Discount(saveDiscount);
        } else {
            return null;
        }
    }

    public static class InvalidUsernameException extends Exception {
    }

    public static class CustomerAlreadyAddedException extends Exception {
    }

    public static class EndDateBeforeStartDateException extends Exception {

    }

    public static class EndDatePassedException extends Exception {

    }
}
