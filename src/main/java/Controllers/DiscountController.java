package Controllers;

import Models.Customer;
import Models.Discount;

import java.util.ArrayList;
import java.util.Date;

public class DiscountController {

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

    public void setEndTime(Date endTime) throws InvalidDateException {
        Date now = new Date();
        if(endTime.before(now)){
            throw new DiscountController.InvalidDateException("we are already past this date");
        }else if(endTime.before(startTime)){
            throw new DiscountController.InvalidDateException("termination time must be after start time");
        }
        else {
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

    public ArrayList<Customer> getAllCustomers(){
        return Customer.getAllCustomers();
    }

    public void setCustomersForDiscountCode(String username) throws InvalidUsernameException{
        if(!Customer.isThereCustomerWithUsername(username)){
            throw new DiscountController.InvalidUsernameException("There is no customer with this username");
        }
        else {
            customersForDiscountCode.add((Customer)Customer.getUserByUsername(username));
        }
    }

    public void finalizeTheNewDiscountCode(){
        Discount discount = new Discount(startTime,endTime,discountPercent,discountLimit,repetitionForEachUser,customersForDiscountCode);
        giveDiscountCodeToCustomers(discount);
    }

    private void giveDiscountCodeToCustomers(Discount discount){
        for (Customer customer : customersForDiscountCode) {
            customer.setDiscountForCustomer(discount);
        }
    }

    public static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }

    public static class InvalidDateException extends Exception {
        public InvalidDateException(String message) {
            super(message);
        }
    }
}
