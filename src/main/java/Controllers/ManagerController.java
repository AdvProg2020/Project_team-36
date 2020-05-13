package Controllers;

import Models.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ManagerController extends UserController {
    GlobalVariables userVariables;
    private static HashMap<String, String> discountFieldsSetters = new HashMap<>();
    private static ArrayList<Customer> customersToBeEditedForDiscountCode = new ArrayList<>();

    public ManagerController(GlobalVariables userVariables) {
        super(userVariables);
        writeDiscountFieldsSetters();
    }

    public boolean loggedInUserIsNotMainManager(){
        return !Manager.isMainManager(userVariables.getLoggedInUser().getUsername());
    }

    public boolean usersTypeIsTheSame(String newType,User user){
        return user.getType().equals(newType);
    }

    public ArrayList<User> getAllUsers() {
        return User.getAllUsers();
    }

    public ArrayList<Discount> getAllDiscountCodes() {
        return Discount.getAllDiscounts();
    }

    public Discount getDiscountWithId(int id) throws InvalidDiscountIdException {
        if (Discount.isThereDiscountWithId(id)) {
            return Discount.getDiscountWithId(id);
        } else {
            throw new ManagerController.InvalidDiscountIdException("there is no discount with this id");
        }
    }

    public void removeDiscount(int id) throws InvalidDiscountIdException {
        if (Discount.isThereDiscountWithId(id)) {
            Discount.getDiscountWithId(id).removeDiscount();
        } else {
            throw new ManagerController.InvalidDiscountIdException("there is no discount with this id");
        }
    }

    public Method getFieldEditor(String chosenField, ManagerController managerController) throws NoSuchMethodException{
        for (String regex : discountFieldsSetters.keySet()) {
            if (chosenField.matches(regex)) {
                return managerController.getClass().getMethod(discountFieldsSetters.get(regex),String.class,Discount.class);
            }
        }
        throw new NoSuchMethodException("you can only edit the fields above, and also please enter the required command.");
    }

    public void invokeEditor(String newValue,Discount discount, Method editor) throws IllegalAccessException, InvocationTargetException {
        editor.invoke(this,newValue,discount);
    }

    public void editDiscountStartTime(String newStartDate, Discount discount) throws InvalidDateException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        Date startDate;
        try {
            startDate = dateFormat.parse(newStartDate);
        } catch (ParseException e) {
            throw new InvalidDateException("the date must be in yyyy/MM/dd format");
        }
        if (startDate.after(discount.getEndTime())) {
            throw new InvalidDateException("start date must be before termination date");
        } else {
            discount.setStartTime(startDate);
        }
    }

    public void editDiscountEndTime(String newEndDate, Discount discount) throws InvalidDateException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        Date endDate;
        try {
            endDate = dateFormat.parse(newEndDate);
        } catch (ParseException e) {
            throw new InvalidDateException("the date must be in yyyy/MM/dd format");
        }
        if (endDate.before(discount.getStartTime())) {
            throw new InvalidDateException("termination date must be after start date");
        } else {
            discount.setStartTime(endDate);
        }
    }

    public void editDiscountPercent(String newPercentage, Discount discount) throws NumberFormatException, InvalidRangeException {
        try {
            int percentage = Integer.parseInt(newPercentage);
            if (percentage < 100 && percentage > 0) {
                discount.setDiscountPercent(percentage * 0.01);
            } else {
                throw new InvalidRangeException("number not in the desired range");
            }
        } catch (InvalidRangeException e){
            throw new InvalidRangeException("number not in the desired range");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("you can't enter anything but number");
        }
    }

    public void editDiscountLimit(String newLimit, Discount discount) throws NumberFormatException {
        try {
            int limit = Integer.parseInt(newLimit);
            discount.setDiscountLimit(limit);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("you can't enter anything but number");
        }
    }

    public void editDiscountRepetitionForEachUser(String newRepetitionNumber,Discount discount) {
        try {
            int repetitionNumber = Integer.parseInt(newRepetitionNumber);
            int difference = Math.abs(repetitionNumber-discount.getRepetitionForEachUser());
            if(repetitionNumber<discount.getRepetitionForEachUser()){
                for (Customer customer : discount.getCustomersIncluded()) {
                    customer.decreaseDiscountCode(discount,difference);
                }
            } else {
                for (Customer customer : discount.getCustomersIncluded()) {
                    customer.increaseDiscountCode(discount,difference);
                }
            }
            discount.setRepetitionForEachUser(repetitionNumber);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("you can't enter anything but number");
        }
    }

    public ArrayList<Customer> getCustomersWithoutThisCode(int id){
        customersToBeEditedForDiscountCode.clear();
        ArrayList<Customer> availableCustomers = new ArrayList<>();
        for (Customer customer : Customer.getAllCustomers()) {
            if(!customer.isThereDiscountCode(id)){
                availableCustomers.add(customer);
            }
        }
        return availableCustomers;
    }

    public void giveCodeToSelectedCustomers(Discount discount){
        discount.setCustomersIncluded(customersToBeEditedForDiscountCode);
        for (Customer customer : customersToBeEditedForDiscountCode) {
            customer.setDiscountForCustomer(discount);
        }
    }

    public void removeCodeFromSelectedCustomers(Discount discount){
        discount.removeCustomersIncluded(customersToBeEditedForDiscountCode);
        for (Customer customer : customersToBeEditedForDiscountCode) {
            customer.removeDiscount(discount);
        }
    }

    public ArrayList<Customer> getCustomersWithThisCode(int id){
        customersToBeEditedForDiscountCode.clear();
        ArrayList<Customer> availableCustomers = new ArrayList<>();
        for (Customer customer : Customer.getAllCustomers()) {
            if(customer.isThereDiscountCode(id)){
                availableCustomers.add(customer);
            }
        }
        return availableCustomers;
    }

    public void setCustomersForEditingDiscountCode(String username) throws InvalidUsernameException {
        if(!Customer.isThereCustomerWithUsername(username)){
            throw new InvalidUsernameException("There is no available customer with this username");
        }
        else {
            customersToBeEditedForDiscountCode.add((Customer)Customer.getUserByUsername(username));
        }
    }

    public User getUserWithUsername(String username) throws InvalidUsernameException {
        if(!User.isThereUsername(username)){
            throw new InvalidUsernameException("there's no user with this username");
        } else {
            return User.getUserByUsername(username);
        }
    }

//    public void changeTypeToManager(User user){
//        if(Manager.isMainManager(userVariables.getLoggedInUser().getUsername())){
//           // User.getAllUsers().remove(user);
//            NewManagerController newManager
//        }
//    }

    public ArrayList<Request> getAllRequests(){
        return Request.getAllRequests();
    }

    public Request getRequestWithId(int id) throws InvalidRequestIdException{
        if (Request.isThereRequestWithId(id)) {
            return Request.getRequestWithId(id);
        } else {
            throw new InvalidRequestIdException("there's no request with this id");
        }
    }

    public void declineRequest(int id) throws InvalidRequestIdException{
        if(Request.isThereRequestWithId(id)){
            Request.denyRequest(id);
        } else {
            throw new InvalidRequestIdException("there's no request with this id");
        }
    }

    public ArrayList<Product> getAllProducts(){
        return Product.getAllProducts();
    }

    private void writeDiscountFieldsSetters() {
        discountFieldsSetters.put("start\\s+time", "editDiscountStartTime");
        discountFieldsSetters.put("termination\\s+time", "editDiscountEndTime");
        discountFieldsSetters.put("discount\\s+percent", "editDiscountPercent");
        discountFieldsSetters.put("discount\\s+limit", "editDiscountLimit");
        discountFieldsSetters.put("usage\\s+frequency", "editDiscountRepetitionForEachUser");
        discountFieldsSetters.put("customers\\s+included", "editDiscountCustomersIncluded");
    }

    public static class InvalidDiscountIdException extends Exception {
        public InvalidDiscountIdException(String message) {
            super(message);
        }
    }

    public static class InvalidDateException extends Exception {
        public InvalidDateException(String message) {
            super(message);
        }
    }

    public static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }

    public static class InvalidRangeException extends Exception {
        public InvalidRangeException(String message) {
            super(message);
        }
    }

    public static class InvalidRequestIdException extends Exception {
        public InvalidRequestIdException(String message) {
            super(message);
        }
    }

    public static class NotTheMainManagerException extends Exception {
        public NotTheMainManagerException(String message) {
            super(message);
        }
    }

}
