package Client.Controllers;

import Client.GUI.Constants;
import Client.Models.*;

import Client.Network.Client;
import Models.Query;
import Models.Response;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ManagerController extends UserController {
    private final String controllerName = "ManagerController";


    public ArrayList<User> getAllUsers() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllUsers");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<User> allUsers = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveUser>>() {
        }.getType();
        List<SaveUser> allSaveUsers = gson.fromJson(response.getData(), type);
        allSaveUsers.forEach(saveUser -> allUsers.add(User.generateUser(saveUser)));
        return allUsers;
    }

    public void setSortDiscountMethods() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setSortDiscountMethods");
        Client.process(query);
    }

    public void setSortRequestsMethods() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setSortRequestsMethods");
        Client.process(query);
    }

    public ArrayList<Discount> getAllDiscountCodes() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllDiscountCodes");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Discount> allDiscounts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveDiscount>>() {
        }.getType();
        List<SaveDiscount> allSaveDiscounts = gson.fromJson(response.getData(), type);
        allSaveDiscounts.forEach(saveDiscount -> allDiscounts.add(new Discount(saveDiscount)));
        return allDiscounts;
    }

    public Discount getDiscountWithId(String id) throws InvalidDiscountIdException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getDiscountWithId");
        query.getMethodInputs().put("id", id);
        Response response = Client.process(query);
        if (response.getReturnType().equals("Discount")) {
            Gson gson = new Gson();
            SaveDiscount saveDiscount = gson.fromJson(response.getData(), SaveDiscount.class);
            return new Discount(saveDiscount);
        } else if (response.getReturnType().equals("InvalidDiscountIdException")) {
            throw new InvalidDiscountIdException();
        } else {
            return null;
        }
    }

    public void removeDiscount(String id) throws InvalidDiscountIdException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeDiscount");
        query.getMethodInputs().put("id", id);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidDiscountIdException")) {
            throw new InvalidDiscountIdException();
        }
    }

    public void editDiscountStartTime(String newStartDate, Discount discount) throws InvalidDateException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editDiscountStartTime");
        query.getMethodInputs().put("id", Integer.toString(discount.getId()));
        query.getMethodInputs().put("newStartDate", newStartDate);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidDateException")) {
            throw new InvalidDateException();
        }
    }

    public void editDiscountEndTime(String newEndDate, Discount discount) throws InvalidDateException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editDiscountEndTime");
        query.getMethodInputs().put("id", Integer.toString(discount.getId()));
        query.getMethodInputs().put("newEndDate", newEndDate);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidDateException")) {
            throw new InvalidDateException();
        }
    }

    public void editDiscountPercent(String newPercentage, Discount discount) throws InvalidDiscountIdException, InvalidRangeException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editDiscountPercent");
        query.getMethodInputs().put("id", Integer.toString(discount.getId()));
        query.getMethodInputs().put("newPercentage", newPercentage);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidDiscountIdException")) {
            throw new InvalidDiscountIdException();
        } else if (response.getReturnType().equals("InvalidRangeException")) {
            throw new InvalidRangeException();
        }
    }

    public void editDiscountLimit(String newLimit, Discount discount) throws NumberFormatException, InvalidDiscountIdException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editDiscountLimit");
        query.getMethodInputs().put("id", Integer.toString(discount.getId()));
        query.getMethodInputs().put("newLimit", newLimit);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidDiscountIdException")) {
            throw new InvalidDiscountIdException();
        }
    }

    public void editDiscountRepetitionForEachUser(String newRepetitionNumber, Discount discount) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editDiscountRepetitionForEachUser");
        query.getMethodInputs().put("id", Integer.toString(discount.getId()));
        query.getMethodInputs().put("newRepetitionNumber", newRepetitionNumber);
        Client.process(query);
    }

    public ArrayList<Customer> getCustomersWithoutThisCode(int id) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCustomersWithoutThisCode");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Customer> allCustomers = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveCustomer>>() {
        }.getType();
        List<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(), type);
        allSaveCustomers.forEach(saveCustomer -> allCustomers.add(new Customer(saveCustomer)));
        return allCustomers;
    }

    public void giveCodeToSelectedCustomers(Discount discount) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "giveCodeToSelectedCustomers");
        query.getMethodInputs().put("id", Integer.toString(discount.getId()));
        Client.process(query);
    }

    public void removeCodeFromSelectedCustomers(Discount discount) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeCodeFromSelectedCustomers");
        query.getMethodInputs().put("id", Integer.toString(discount.getId()));
        Client.process(query);
    }

    public void setCustomersIncludedForDiscount(Customer customer, int id){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCustomersIncludedForDiscount");
        query.getMethodInputs().put("id", Integer.toString(id));
        query.getMethodInputs().put("username", customer.getUsername());
        Client.process(query);
    }

    public void removeCustomersIncludedForDiscount(Customer customer, int id){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeCustomersIncludedForDiscount");
        query.getMethodInputs().put("id", Integer.toString(id));
        query.getMethodInputs().put("username", customer.getUsername());
        Client.process(query);
    }

    public ArrayList<Customer> getCustomersWithThisCode(int id) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCustomersWithThisCode");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Customer> allCustomers = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveCustomer>>() {
        }.getType();
        List<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(), type);
        allSaveCustomers.forEach(saveCustomer -> allCustomers.add(new Customer(saveCustomer)));
        return allCustomers;
    }

    public void setCustomersForAddingDiscountCode(String username, int id) throws InvalidUsernameException, CustomerAlreadyAddedException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCustomersForAddingDiscountCode");
        query.getMethodInputs().put("id", Integer.toString(id));
        query.getMethodInputs().put("username", username);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidUsernameException")) {
            throw new InvalidUsernameException();
        } else if (response.getReturnType().equals("CustomerAlreadyAddedException")) {
            throw new CustomerAlreadyAddedException();
        }
    }

    public void setCustomersForRemovingDiscountCode(String username, int id) throws InvalidUsernameException, CustomerAlreadyAddedException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCustomersForRemovingDiscountCode");
        query.getMethodInputs().put("id", Integer.toString(id));
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

    public boolean canManagerRegister(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "canManagerRegister");
        Response response = Client.process(query);
        Gson gson = new Gson();
        return gson.fromJson(response.getData(), Boolean.class);
    }

    public User getUserWithUsername(String username) throws InvalidUsernameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getUserWithUsername");
        query.getMethodInputs().put("username", username);
        Response response = Client.process(query);
        if (response.getReturnType().equals("User")) {
            Gson gson = new Gson();
            SaveUser saveUser = gson.fromJson(response.getData(), SaveUser.class);
            return User.generateUser(saveUser);
        } else if (response.getReturnType().equals("InvalidUsernameException")) {
            throw new InvalidUsernameException();
        } else {
            return null;
        }
    }

    public void deleteUser(User user) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "deleteUser");
        query.getMethodInputs().put("id", Integer.toString(user.getUserId()));
        Client.process(query);
    }

    public ArrayList<Request> getAllRequests() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllRequests");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Request> allRequests = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveRequest>>() {
        }.getType();
        List<SaveRequest> allSaveRequests = gson.fromJson(response.getData(), type);
        allSaveRequests.forEach(saveRequest -> allRequests.add(new Request(saveRequest)));
        return allRequests;
    }

    public Request getRequestWithId(int id) throws InvalidRequestIdException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getRequestWithId");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        if (response.getReturnType().equals("Request")) {
            Gson gson = new Gson();
            SaveRequest saveRequest = gson.fromJson(response.getData(), SaveRequest.class);
            return new Request(saveRequest);
        } else if (response.getReturnType().equals("InvalidRequestIdException")) {
            throw new InvalidRequestIdException();
        } else {
            return null;
        }
    }

    public void declineRequest(int id) throws InvalidRequestIdException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "declineRequest");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidRequestIdException")) {
            throw new InvalidRequestIdException();
        }
    }

    public void acceptRequest(int id) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "acceptRequest");
        query.getMethodInputs().put("id", Integer.toString(id));
        Client.process(query);
    }

    public ArrayList<Product> getAllProducts() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllProducts");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Product> allProducts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveProduct>>() {
        }.getType();
        List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(), type);
        allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
        return allProducts;
    }

    public Product getProductWithId(int id) throws InvalidProductIdException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProductWithId");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        if (response.getReturnType().equals("Product")) {
            Gson gson = new Gson();
            SaveProduct saveProduct = gson.fromJson(response.getData(), SaveProduct.class);
            return new Product(saveProduct);
        } else if (response.getReturnType().equals("InvalidProductIdException")) {
            throw new InvalidProductIdException();
        } else {
            return null;
        }
    }

    public void removeProduct(Product product) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeProduct");
        query.getMethodInputs().put("id", Integer.toString(product.getProductId()));
        Client.process(query);
    }

    public ArrayList<User> sortUsers(String field, String typeString) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sortUsers");
        query.getMethodInputs().put("field", field);
        query.getMethodInputs().put("type", typeString);
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<User>")) {
            Gson gson = new Gson();
            ArrayList<User> allUsers = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveUser>>() {
            }.getType();
            List<SaveUser> allSaveUsers = gson.fromJson(response.getData(), type);
            allSaveUsers.forEach(saveUser -> allUsers.add(User.generateUser(saveUser)));
            return allUsers;
        } else if (response.getReturnType().equals("ProductsController.NoSortException")) {
            throw new ProductsController.NoSortException();
        } else {
            return null;
        }
    }

    public ArrayList<Discount> sortDiscountCodes(String field, String typeString) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sortDiscountCodes");
        query.getMethodInputs().put("field", field);
        query.getMethodInputs().put("type", typeString);
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Discount>")) {
            Gson gson = new Gson();
            ArrayList<Discount> allDiscounts = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveDiscount>>() {
            }.getType();
            List<SaveDiscount> allSaveDiscounts = gson.fromJson(response.getData(), type);
            allSaveDiscounts.forEach(saveDiscount -> allDiscounts.add(new Discount(saveDiscount)));
            return allDiscounts;
        } else if (response.getReturnType().equals("ProductsController.NoSortException")) {
            throw new ProductsController.NoSortException();
        } else {
            return null;
        }
    }

    public ArrayList<Request> filterRequests(String input) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "filterRequests");
        query.getMethodInputs().put("input", input);
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Request>")) {
            Gson gson = new Gson();
            ArrayList<Request> allRequests = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveRequest>>() {
            }.getType();
            List<SaveRequest> allSaveRequests = gson.fromJson(response.getData(), type);
            allSaveRequests.forEach(saveRequest -> allRequests.add(new Request(saveRequest)));
            return allRequests;
        } else {
            return null;
        }
    }

    public ArrayList<Request> sortRequests(String field, String typeString) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sortRequests");
        query.getMethodInputs().put("field", field);
        query.getMethodInputs().put("type", typeString);
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Request>")) {
            Gson gson = new Gson();
            ArrayList<Request> allRequests = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveRequest>>() {
            }.getType();
            List<SaveRequest> allSaveRequests = gson.fromJson(response.getData(), type);
            allSaveRequests.forEach(saveRequest -> allRequests.add(new Request(saveRequest)));
            return allRequests;
        } else if (response.getReturnType().equals("ProductsController.NoSortException")) {
            throw new ProductsController.NoSortException();
        } else {
            return null;
        }
    }

    public ArrayList<Product> sortProducts(String field, String typeString) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sortProducts");
        query.getMethodInputs().put("field", field);
        query.getMethodInputs().put("type", typeString);
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Product>")) {
            Gson gson = new Gson();
            ArrayList<Product> allProducts = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveProduct>>() {
            }.getType();
            List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(), type);
            allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
            return allProducts;
        } else if (response.getReturnType().equals("ProductsController.NoSortException")) {
            throw new ProductsController.NoSortException();
        } else {
            return null;
        }
    }

    public Discount getDiscountToView() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getDiscountToView");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Discount")) {
            Gson gson = new Gson();
            SaveDiscount saveDiscount = gson.fromJson(response.getData(), SaveDiscount.class);
            return new Discount(saveDiscount);
        } else {
            return null;
        }    }

    public Discount getDiscountToEdit() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getDiscountToEdit");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Discount")) {
            Gson gson = new Gson();
            SaveDiscount saveDiscount = gson.fromJson(response.getData(), SaveDiscount.class);
            return new Discount(saveDiscount);
        } else {
            return null;
        }      }

    public void setDiscountToView(Discount discountToView) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setDiscountToView");
        query.getMethodInputs().put("id", Integer.toString(discountToView.getId()));
        Client.process(query);
    }

    public void setDiscountToEdit(Discount discountToEdit) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setDiscountToEdit");
        query.getMethodInputs().put("id", Integer.toString(discountToEdit.getId()));
        Client.process(query);
    }

    public ArrayList<CustomerLog> getAllCustomerLogs(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllCustomerLogs");
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<CustomerLog>")) {
            Gson gson = new Gson();
            ArrayList<CustomerLog> allLogs = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveCustomerLog>>() {
            }.getType();
            List<SaveCustomerLog> allSaveLogs = gson.fromJson(response.getData(), type);
            allSaveLogs.forEach(saveLog -> allLogs.add(new CustomerLog(saveLog)));
            return allLogs;
        } else{
            System.out.println(response.getData());
            return null;
        }
    }

    public void setLogSent(int logId) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setLogSent");
        query.getMethodInputs().put("logId",Integer.toString(logId));
        Client.process(query);
    }

    public static class InvalidDiscountIdException extends Exception {
        public InvalidDiscountIdException() {
            super("there is no discount with this id");
        }
    }


    public static class InvalidDateException extends Exception {

    }

    public static class InvalidUsernameException extends Exception {
    }

    public static class InvalidRangeException extends Exception {

    }

    public static class InvalidRequestIdException extends Exception {
    }

    public static class InvalidProductIdException extends Exception {
    }

    public static class CustomerAlreadyAddedException extends Exception {
    }

}
