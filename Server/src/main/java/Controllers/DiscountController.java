package Controllers;

import Models.Customer;
import Models.Discount;
import Models.Query;
import Models.Response;
import Repository.SaveCustomer;
import Repository.SaveDiscount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscountController {
    private Discount discount;
    private ArrayList<Customer> customersForDiscountCode;
    private Date startTime;
    private Date endTime;
    private double discountPercent;//bar hasbe darsad nist
    private long discountLimit;
    private int repetitionForEachUser;

    public DiscountController() {
        this.customersForDiscountCode = new ArrayList<>();
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) throws EndDateBeforeStartDateException, EndDatePassedException {
        Date now = new Date();
        if (endTime.before(now)) {
            throw new EndDatePassedException();
        } else if (endTime.before(startTime)) {
            throw new EndDateBeforeStartDateException();
        } else {
            this.endTime = endTime;
        }
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setDiscountLimit(long discountLimit) {
        this.discountLimit = discountLimit;
    }

    public void setRepetitionForEachUser(int repetitionForEachUser) {
        this.repetitionForEachUser = repetitionForEachUser;
    }

    public ArrayList<Customer> getAllCustomers() {
        return Customer.getAllCustomers();
    }

    public void setCustomersForDiscountCode(String username) throws InvalidUsernameException, CustomerAlreadyAddedException {
        if (!Customer.isThereCustomerWithUsername(username)) {
            throw new InvalidUsernameException();
        } else if (isThereCustomerWithUsername(username)) {
            throw new CustomerAlreadyAddedException();
        } else {
            customersForDiscountCode.add((Customer) Customer.getUserByUsername(username));
        }
    }

    public boolean isThereCustomerWithUsername(String username) {
        for (Customer customer : customersForDiscountCode) {
            if (customer.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void finalizeTheNewDiscountCode() {
        discount = new Discount(startTime, endTime, discountPercent, discountLimit, repetitionForEachUser, customersForDiscountCode);
        giveDiscountCodeToCustomers(discount);
    }

    private void giveDiscountCodeToCustomers(Discount discount) {
        for (Customer customer : customersForDiscountCode) {
            customer.setDiscountForCustomer(discount);
        }
    }

    public Discount getDiscount() {
        return discount;
    }

    public Response processQuery(Query query) {
        switch (query.getMethodName()) {
            case "setStartTime":
                return processSetStartTime(query);

            case "setEndTime":
                return processSetEndTime(query);

            case "setDiscountPercent":
                return processSetDiscountPercent(query);

            case "setDiscountLimit":
                return processSetDiscountLimit(query);

            case "setRepetitionForEachUser":
                return processSetRepetitionForEachUser(query);

            case "getAllCustomers":
                return processGetAllCustomers();

            case "setCustomersForDiscountCode":
                return processSetCustomersForDiscountCode(query);


            case "finalizeTheNewDiscountCode":
                return processFinalizeTheNewDiscountCode();

            case "getDiscount":
                return processGetDiscount();
            case "isThereCustomerWithUsername":
                return processIsThereCustomerWithUsername(query);
            default:
                return new Response("Error", "");
        }
    }

    private Response processSetStartTime(Query query) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date startTime = dateFormat.parse(query.getMethodInputs().get("startTime"));
            setStartTime(startTime);
            return new Response("void", "");
        } catch (ParseException e) {
            return new Response("ParseException", "");
        }
    }

    private Response processSetEndTime(Query query) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date endTime = dateFormat.parse(query.getMethodInputs().get("endTime"));
            setEndTime(endTime);
            return new Response("void", "");
        } catch (ParseException e) {
            return new Response("ParseException", "");
        } catch (EndDateBeforeStartDateException e) {
            return new Response("EndDateBeforeStartDateException", "");
        } catch (EndDatePassedException e) {
            return new Response("EndDatePassedException", "");
        }
    }

    private Response processSetDiscountPercent(Query query){
        double percent = Double.parseDouble(query.getMethodInputs().get("discountPercent"));
        setDiscountPercent(percent);
        return new Response("void", "");
    }

    private Response processSetDiscountLimit(Query query){
        long discountLimit = Long.parseLong(query.getMethodInputs().get("discountLimit"));
        setDiscountLimit(discountLimit);
        return new Response("void", "");
    }

    private Response processSetRepetitionForEachUser(Query query){
        int repetitionForEachUser = Integer.parseInt(query.getMethodInputs().get("repetitionForEachUser"));
        setRepetitionForEachUser(repetitionForEachUser);
        return new Response("void", "");
    }

    private Response processGetAllCustomers(){
        List<SaveCustomer> allSaveCustomers = new ArrayList<>();
        getAllCustomers().forEach(c -> allSaveCustomers.add(new SaveCustomer(c)));
        Gson gson = new GsonBuilder().create();
        String saveCustomerListGson = gson.toJson(allSaveCustomers);
        return new Response("List<Customer>", saveCustomerListGson);
    }

    private Response processSetCustomersForDiscountCode(Query query){
        try {
            setCustomersForDiscountCode(query.getMethodInputs().get("username"));
            return new Response("void", "");
        } catch (InvalidUsernameException e) {
            return new Response("InvalidUsernameException", "");
        } catch (CustomerAlreadyAddedException e) {
            return new Response("CustomerAlreadyAddedException", "");
        }
    }

    private Response processFinalizeTheNewDiscountCode(){
        finalizeTheNewDiscountCode();
        return new Response("void", "");
    }

    private Response processGetDiscount(){
        SaveDiscount saveDiscount = new SaveDiscount(getDiscount());
        Gson gson = new GsonBuilder().create();
        String saveDiscountGson = gson.toJson(saveDiscount);
        return new Response("Discount", saveDiscountGson);
    }

    private Response processIsThereCustomerWithUsername(Query query){
        boolean thereIsCustomer = isThereCustomerWithUsername(query.getMethodInputs().get("username"));
        Gson gson = new GsonBuilder().create();
        String thereIsCustomerGson = gson.toJson(thereIsCustomer);
        return new Response("Boolean", thereIsCustomerGson);
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
