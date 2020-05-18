package Controllers;

import Models.Customer;
import Models.Discount;

import java.util.ArrayList;
import java.util.Date;

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

    public void setEndTime(Date endTime) throws EndDateBeforeStartDateException,EndDatePassedException {
        Date now = new Date();
        if(endTime.before(now)){
            throw new EndDatePassedException();
        }else if(endTime.before(startTime)){
            throw new EndDateBeforeStartDateException();
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

    public void setCustomersForDiscountCode(String username) throws InvalidUsernameException,CustomerAlreadyAddedException{
        if(!Customer.isThereCustomerWithUsername(username)){
            throw new InvalidUsernameException();
        }else if(isThereCustomerWithUsername(username)){
            throw new CustomerAlreadyAddedException();
        } else {
            customersForDiscountCode.add((Customer)Customer.getUserByUsername(username));
        }
    }

    public boolean isThereCustomerWithUsername(String username){
        for (Customer customer : customersForDiscountCode ) {
            if (customer.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public void finalizeTheNewDiscountCode(){
        discount = new Discount(startTime,endTime,discountPercent,discountLimit,repetitionForEachUser,customersForDiscountCode);
        giveDiscountCodeToCustomers(discount);
    }

    private void giveDiscountCodeToCustomers(Discount discount){
        for (Customer customer : customersForDiscountCode) {
            customer.setDiscountForCustomer(discount);
        }
    }

    public Discount getDiscount(){
        return discount;
    }

    public static class InvalidUsernameException extends Exception {
    }

    public static class CustomerAlreadyAddedException extends Exception {
    }

    public static class EndDateBeforeStartDateException extends Exception {

    }

    public static class EndDatePassedException extends Exception {

    }

    //-..-
}
