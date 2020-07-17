package Controllers;

import Models.*;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ManagerController extends UserController {
    GlobalVariables userVariables;
    private HashMap<Integer, String> giftEvents;
    private static HashMap<String, String> discountFieldsSetters = new HashMap<>();
    private static ArrayList<Customer> customersToBeEditedForDiscountCode = new ArrayList<>();
    private HashMap<String, Method> sortUsersMethods;
    private HashMap<String, Method> sortDiscountMethods;
    private HashMap<String, Method> sortRequestMethods;
    private HashMap<String, Method> sortProductsMethods;

    public ManagerController(GlobalVariables userVariables) {
        super(userVariables);
        writeDiscountFieldsSetters();
        giftEvents = new HashMap<>();
        giftEvents.put(1, "first buy gift");
        giftEvents.put(2, "high log price gift");
        giftEvents.put(3, "periodic gift");
        setSortUsersMethods();
        setSortDiscountMethods();
        setSortRequestsMethods();
        setSortMethodsProducts();
    }

    public ArrayList<User> getAllUsers() {
        return User.getAllUsers();
    }

    public void setSortDiscountMethods() {
        this.sortDiscountMethods = new HashMap<>();
        try {
            Method method = Discount.class.getDeclaredMethod("getEndTime");
            sortDiscountMethods.put("end time", method);
            method = Discount.class.getDeclaredMethod("getStartTime");
            sortDiscountMethods.put("start time", method);
            method = Discount.class.getDeclaredMethod("getDiscountPercent");
            sortDiscountMethods.put("percent", method);
            method = Discount.class.getDeclaredMethod("getDiscountLimit");
            sortDiscountMethods.put("limit", method);
        } catch (NoSuchMethodException e) {

        }
    }

    private void setSortMethodsProducts() {
        try {
            sortProductsMethods = new HashMap<>();
            Method method = Product.class.getDeclaredMethod("getProductionDate");
            this.sortProductsMethods.put("production date", method);
            method = Product.class.getDeclaredMethod("getSeenNumber");
            this.sortProductsMethods.put("seen", method);
            method = Product.class.getDeclaredMethod("getName");
            this.sortProductsMethods.put("name", method);
            method = Product.class.getDeclaredMethod("getScore");
            this.sortProductsMethods.put("score", method);
            method = Product.class.getDeclaredMethod("getHighestCurrentPrice");
            this.sortProductsMethods.put("maximum price of all", method);
            method = Product.class.getDeclaredMethod("getLowestCurrentPrice");
            this.sortProductsMethods.put("minimum price of all", method);
            method = Product.class.getDeclaredMethod("getTotalSupply");
            this.sortProductsMethods.put("supply", method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setSortRequestsMethods() {
        this.sortRequestMethods = new HashMap<>();
        try {
            Method method = Request.class.getDeclaredMethod("getType");
            sortRequestMethods.put("type", method);
            method = Request.class.getDeclaredMethod("getRequestId");
            sortRequestMethods.put("request id", method);
        } catch (NoSuchMethodException e) {

        }
    }

    private void setSortUsersMethods() {
        sortUsersMethods = new HashMap<>();
        try {
            Method method = User.class.getDeclaredMethod("getUserId");
            sortUsersMethods.put("username", method);
            method = User.class.getDeclaredMethod("getLastname");
            sortUsersMethods.put("lastname", method);
            method = User.class.getDeclaredMethod("getFirstname");
            sortUsersMethods.put("firstname", method);
            method = User.class.getDeclaredMethod("getType");
            sortUsersMethods.put("type", method);
        } catch (NoSuchMethodException e) {
        }
    }

    public ArrayList<Discount> getAllDiscountCodes() {
        return Discount.getAllDiscounts();
    }

    public Discount getDiscountWithId(String id) throws InvalidDiscountIdException {
        int intId = Integer.parseInt(id);
        if (Discount.isThereDiscountWithId(intId)) {
            return Discount.getDiscountWithId(intId);
        } else {
            throw new ManagerController.InvalidDiscountIdException();
        }
    }

    public void removeDiscount(String id) throws InvalidDiscountIdException {
        int intId = Integer.parseInt(id);
        if (Discount.isThereDiscountWithId(intId)) {
            Discount.getDiscountWithId(intId).removeDiscount();
        } else {
            throw new ManagerController.InvalidDiscountIdException();
        }
    }

    //todo nazashtamesh (bedune estefade)
    public Method getFieldEditor(String chosenField) throws NoSuchMethodException {
        for (String regex : discountFieldsSetters.keySet()) {
            if (chosenField.matches(regex)) {
                return this.getClass().getMethod(discountFieldsSetters.get(regex), String.class, Discount.class);
            }
        }
        throw new NoSuchMethodException("you can only edit the fields above, and also please enter the required command.");
    }

    //todo nazashtamesh (bedune estefade)
    public void invokeEditor(String newValue, Discount discount, Method editor) throws IllegalAccessException, InvocationTargetException {

        editor.invoke(this, newValue, discount);
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
            discount.setEndTime(endDate);
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
        } catch (InvalidRangeException e) {
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

    public void editDiscountRepetitionForEachUser(String newRepetitionNumber, Discount discount) {
        try {
            int repetitionNumber = Integer.parseInt(newRepetitionNumber);
            int difference = Math.abs(repetitionNumber - discount.getRepetitionForEachUser());
            if (repetitionNumber < discount.getRepetitionForEachUser()) {
                for (Customer customer : discount.getCustomersIncluded()) {
                    customer.decreaseDiscountCode(discount, difference);
                }
            } else {
                for (Customer customer : discount.getCustomersIncluded()) {
                    customer.increaseDiscountCode(discount, difference);
                }
            }
            discount.setRepetitionForEachUser(repetitionNumber);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("you can't enter anything but number");
        }
    }

    public ArrayList<Customer> getCustomersWithoutThisCode(int id) {
        customersToBeEditedForDiscountCode.clear();
        ArrayList<Customer> availableCustomers = new ArrayList<>();
        for (Customer customer : Customer.getAllCustomers()) {
            if (!customer.isThereDiscountCode(id)) {
                availableCustomers.add(customer);
            }
        }
        return availableCustomers;
    }

    public void giveCodeToSelectedCustomers(Discount discount) {
        discount.setCustomersIncluded(customersToBeEditedForDiscountCode);
        for (Customer customer : customersToBeEditedForDiscountCode) {
            customer.setDiscountForCustomer(discount);
        }
    }

    public void removeCodeFromSelectedCustomers(Discount discount) {
        discount.removeCustomersIncluded(customersToBeEditedForDiscountCode);
        for (Customer customer : customersToBeEditedForDiscountCode) {
            customer.removeDiscount(discount);
        }
    }

    public ArrayList<Customer> getCustomersWithThisCode(int id) {
        customersToBeEditedForDiscountCode.clear();
        ArrayList<Customer> availableCustomers = new ArrayList<>();
        for (Customer customer : Customer.getAllCustomers()) {
            if (customer.isThereDiscountCode(id)) {
                availableCustomers.add(customer);
            }
        }
        return availableCustomers;
    }

    public void setCustomersForAddingDiscountCode(String username, int id) throws InvalidUsernameException, CustomerAlreadyAddedException {
        for (Customer customer : getCustomersWithoutThisCode(id)) {
            if (customer.getUsername().equals(username)) {
                if (isThereCustomerWithUsername(username)) {
                    throw new CustomerAlreadyAddedException();
                } else {
                    customersToBeEditedForDiscountCode.add((Customer) Customer.getUserByUsername(username));
                    return;
                }
            }
        }
        throw new InvalidUsernameException("There is no available customer with this username");
    }

    public void setCustomersForRemovingDiscountCode(String username, int id) throws InvalidUsernameException, CustomerAlreadyAddedException {
        for (Customer customer : getCustomersWithThisCode(id)) {
            if (customer.getUsername().equals(username)) {
                if (isThereCustomerWithUsername(username)) {
                    throw new CustomerAlreadyAddedException();
                } else {
                    customersToBeEditedForDiscountCode.add((Customer) Customer.getUserByUsername(username));
                    return;
                }
            }
        }
        throw new InvalidUsernameException("There is no available customer with this username");
    }

    public boolean isThereCustomerWithUsername(String username) {
        for (Customer customer : customersToBeEditedForDiscountCode) {
            if (customer.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public User getUserWithUsername(String username) throws InvalidUsernameException {
        if (!User.isThereUsername(username)) {
            throw new InvalidUsernameException("there's no user with this username");
        } else {
            return User.getUserByUsername(username);
        }
    }

    public void deleteUser(User user) {
        user.setUserDeleted();
        User.removeUsername(user.getUsername());
    }

    public ArrayList<Request> getAllRequests() {
        return Request.getAllRequests();
    }

    public Request getRequestWithId(int id) throws InvalidRequestIdException {
        if (Request.isThereRequestWithId(id)) {
            return Request.getRequestWithId(id);
        } else {
            throw new InvalidRequestIdException("there's no request with this id");
        }
    }

    public void declineRequest(int id) throws InvalidRequestIdException {
        if (Request.isThereRequestWithId(id)) {
            Request.denyRequest(id);
        } else {
            throw new InvalidRequestIdException("there's no request with this id");
        }
    }

    public ArrayList<Product> getAllProducts() {
        return Product.getAllProducts();
    }

    public Product getProductWithId(int id) throws InvalidProductIdException {
        if (Product.isThereProductWithId(id)) {
            return Product.getProduct(id);
        } else {
            throw new InvalidProductIdException("there's no product with these id");
        }
    }

    public void removeProduct(Product product) {
        Product.removeProduct(product);
    }

    private void writeDiscountFieldsSetters() {
        discountFieldsSetters.put("start\\s+time", "editDiscountStartTime");
        discountFieldsSetters.put("termination\\s+time", "editDiscountEndTime");
        discountFieldsSetters.put("discount\\s+percent", "editDiscountPercent");
        discountFieldsSetters.put("discount\\s+limit", "editDiscountLimit");
        discountFieldsSetters.put("usage\\s+frequency", "editDiscountRepetitionForEachUser");
        discountFieldsSetters.put("customers\\s+included", "editDiscountCustomersIncluded");
    }

    public ArrayList<User> sortUsers(String field, String type) throws ProductsController.NoSortException {
        ArrayList<User> toBeReturned = new ArrayList<>();
        toBeReturned.addAll(getAllUsers());
        for (String regex : sortUsersMethods.keySet()) {
            if (regex.equalsIgnoreCase(field)) {
                new Sort().sort(toBeReturned, sortUsersMethods.get(regex), type.equalsIgnoreCase("ascending"));
                return toBeReturned;
            }
        }
        throw new ProductsController.NoSortException();
    }

    public ArrayList<Discount> sortDiscountCodes(String field, String type) throws ProductsController.NoSortException {
        ArrayList<Discount> toBeReturned = new ArrayList<>();
        toBeReturned.addAll(getAllDiscountCodes());
        for (String regex : sortDiscountMethods.keySet()) {
            if (regex.equalsIgnoreCase(field)) {
                new Sort().sort(toBeReturned, sortDiscountMethods.get(regex), type.equalsIgnoreCase("ascending"));
                return toBeReturned;
            }
        }
        throw new ProductsController.NoSortException();

    }

    public ArrayList<Request> filterRequests(String input) {
        ArrayList<Request> requests = new ArrayList<>();
        requests.addAll(Request.getAllRequests());
        ArrayList<Request> toBeReturned = new ArrayList<>();
        if (input.matches("seller")) {
            for (Request request : requests) {
                if (request.getPendableRequest() instanceof Seller)
                    toBeReturned.add(request);
            }
        } else if (input.matches("product")) {
            for (Request request : requests) {
                if (request.getPendableRequest() instanceof Product)
                    toBeReturned.add(request);
            }
        } else if (input.matches("seller for product")) {
            for (Request request : requests) {
                if (request.getPendableRequest() instanceof ProductField)
                    toBeReturned.add(request);
            }
        } else if (input.matches("comment")) {
            for (Request request : requests) {
                if (request.getPendableRequest() instanceof Comment)
                    toBeReturned.add(request);
            }
        }
        return toBeReturned;

    }

    public ArrayList<Request> sortRequests(String field, String type) throws ProductsController.NoSortException {
        ArrayList<Request> toBeReturned = new ArrayList<>();
        toBeReturned.addAll(getAllRequests());
        for (String regex : sortRequestMethods.keySet()) {
            if (regex.equalsIgnoreCase(field)) {
                new Sort().sort(toBeReturned, sortRequestMethods.get(regex), type.equalsIgnoreCase("ascending"));
                return toBeReturned;
            }
        }
        throw new ProductsController.NoSortException();
    }

    public ArrayList<Product> sortProducts(String field, String type) throws ProductsController.NoSortException {
        ArrayList<Product> toBeReturned = new ArrayList<>();
        toBeReturned.addAll(getAllProducts());
        for (String regex : sortProductsMethods.keySet()) {
            if (regex.equalsIgnoreCase(field)) {
                new Sort().sort(toBeReturned, sortProductsMethods.get(regex), type.equalsIgnoreCase("ascending"));
                return toBeReturned;
            }
        }
        throw new ProductsController.NoSortException();
    }

    //todo: fekr naknm bekhaim bezanimesh
    public HashMap<Integer, String> getGiftEventsName() {
        return this.giftEvents;
    }

    public Response processQuery(Query query) {
        switch (query.getMethodName()) {
            case "getAllUsers":
                return processGetAllUsers();

            case "setSortDiscountMethods":
                return processSetSortDiscountMethods();

            case "setSortMethodsProducts":
                return processSetSortMethodsProducts();

            case "setSortRequestsMethods":
                return processSetSortRequestsMethods();

            case "setSortUsersMethods":
                return processSetSortUsersMethods();

            case "getAllDiscountCodes":
                return processGetAllDiscountCodes();

            case "getDiscountWithId":
                return processGetDiscountWithId(query);

            case "removeDiscount":
                return processRemoveDiscount(query);

            case "editDiscountStartTime":
                return processEditDiscountStartTime(query);

            case "editDiscountEndTime":
                return processEditDiscountEndTime(query);

            case "editDiscountPercent":
                return processEditDiscountPercent(query);

            case "editDiscountLimit":
                return processEditDiscountLimit(query);

            case "editDiscountRepetitionForEachUser":
                return processEditDiscountRepetitionForEachUser(query);

            case "getCustomersWithoutThisCode":
                return processGetCustomersWithoutThisCode(query);

            case "giveCodeToSelectedCustomers":
                return processGiveCodeToSelectedCustomers(query);

            case "removeCodeFromSelectedCustomers":
                return processRemoveCodeFromSelectedCustomers(query);

            case "getCustomersWithThisCode":
                return processGetCustomersWithThisCode(query);

            case "setCustomersForAddingDiscountCode":
                return processSetCustomersForAddingDiscountCode(query);

            case "setCustomersForRemovingDiscountCode":
                return processSetCustomersForRemovingDiscountCode(query);

            case "isThereCustomerWithUsername":
                return processIsThereCustomerWithUsername(query);

            case "getUserWithUsername":
                return processGetUserWithUsername(query);

            case "deleteUser":
                return processDeleteUser(query);

            case "getAllRequests":
                return processGetAllRequests();

            case "getRequestWithId":
                return processGetRequestWithId(query);

            case "declineRequest":
                return processDeclineRequest(query);

            case "getAllProducts":
                return processGetAllProducts();

            case "getProductWithId":
                return processGetProductWithId(query);

            case "removeProduct":
                return processRemoveProduct(query);

            case "sortUsers":
                return processSortUsers(query);

            case "sortDiscountCodes":
                return processSortDiscountCodes(query);

            case "filterRequests":
                return processFilterRequests(query);

            case "sortRequests":
                return processSortRequests(query);

            case "sortProducts":
                return processSortProducts(query);

            default:
                return new Response("Error", "");
        }
    }

    private Response processGetAllUsers() {
        List<SaveUser> allSaveUsers = new ArrayList<>();
        getAllUsers().forEach(c -> allSaveUsers.add(new SaveUser(c)));
        Gson gson = new GsonBuilder().create();
        String saveUserListGson = gson.toJson(allSaveUsers);
        return new Response("List<User>", saveUserListGson);
    }

    private Response processSetSortDiscountMethods() {
        setSortDiscountMethods();
        return new Response("void", "");
    }

    private Response processSetSortMethodsProducts() {
        setSortMethodsProducts();
        return new Response("void", "");
    }

    private Response processSetSortRequestsMethods() {
        setSortRequestsMethods();
        return new Response("void", "");
    }

    private Response processSetSortUsersMethods() {
        setSortUsersMethods();
        return new Response("void", "");
    }

    private Response processGetAllDiscountCodes() {
        List<SaveDiscount> allSaveDiscounts = new ArrayList<>();
        getAllDiscountCodes().forEach(c -> allSaveDiscounts.add(new SaveDiscount(c)));
        Gson gson = new GsonBuilder().create();
        String saveDiscountListGson = gson.toJson(allSaveDiscounts);
        return new Response("List<Discount>", saveDiscountListGson);
    }

    private Response processGetDiscountWithId(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            Gson gson = new GsonBuilder().create();
            String saveDiscountGson = gson.toJson(new SaveDiscount(discount));
            return new Response("Discount", saveDiscountGson);
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        }
    }

    private Response processRemoveDiscount(Query query) {
        try {
            removeDiscount(query.getMethodInputs().get("id"));
            return new Response("void", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        }
    }

    private Response processEditDiscountStartTime(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            editDiscountStartTime(query.getMethodInputs().get("newStartDate"), discount);
            return new Response("void", "");
        } catch (InvalidDateException e) {
            return new Response("InvalidDateException", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        }
    }

    private Response processEditDiscountEndTime(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            editDiscountEndTime(query.getMethodInputs().get("newEndDate"), discount);
            return new Response("void", "");
        } catch (InvalidDateException e) {
            return new Response("InvalidDateException", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        }
    }

    private Response processEditDiscountPercent(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            editDiscountPercent(query.getMethodInputs().get("newPercentage"), discount);
            return new Response("void", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        } catch (InvalidRangeException e) {
            return new Response("InvalidRangeException", "");
        } catch (NumberFormatException e) {
            return new Response("NumberFormatException", "");
        }
    }

    private Response processEditDiscountLimit(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            editDiscountLimit(query.getMethodInputs().get("newLimit"), discount);
            return new Response("void", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        } catch (NumberFormatException e) {
            return new Response("NumberFormatException", "");
        }
    }

    private Response processEditDiscountRepetitionForEachUser(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            editDiscountRepetitionForEachUser(query.getMethodInputs().get("newRepetitionNumber"), discount);
            return new Response("void", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        } catch (NumberFormatException e) {
            return new Response("NumberFormatException", "");
        }
    }

    private Response processGetCustomersWithoutThisCode(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        List<SaveCustomer> allSaveCustomers = new ArrayList<>();
        getCustomersWithoutThisCode(id).forEach(c -> allSaveCustomers.add(new SaveCustomer(c)));
        Gson gson = new GsonBuilder().create();
        String saveCustomerListGson = gson.toJson(allSaveCustomers);
        return new Response("List<Customer>", saveCustomerListGson);
    }

    private Response processGiveCodeToSelectedCustomers(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            giveCodeToSelectedCustomers(discount);
            return new Response("void", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        }
    }

    private Response processRemoveCodeFromSelectedCustomers(Query query) {
        try {
            Discount discount = getDiscountWithId(query.getMethodInputs().get("id"));
            removeCodeFromSelectedCustomers(discount);
            return new Response("void", "");
        } catch (InvalidDiscountIdException e) {
            return new Response("InvalidDiscountIdException", "");
        }
    }

    private Response processGetCustomersWithThisCode(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        List<SaveCustomer> allSaveCustomers = new ArrayList<>();
        getCustomersWithThisCode(id).forEach(c -> allSaveCustomers.add(new SaveCustomer(c)));
        Gson gson = new GsonBuilder().create();
        String saveCustomerListGson = gson.toJson(allSaveCustomers);
        return new Response("List<Customer>", saveCustomerListGson);
    }

    private Response processSetCustomersForAddingDiscountCode(Query query) {
        try {
            int id = Integer.parseInt(query.getMethodInputs().get("id"));
            setCustomersForAddingDiscountCode(query.getMethodInputs().get("username"), id);
            return new Response("void", "");
        } catch (NumberFormatException e) {
            return new Response("NumberFormatException", "");
        } catch (InvalidUsernameException e) {
            return new Response("InvalidUsernameException", "");
        } catch (CustomerAlreadyAddedException e) {
            return new Response("CustomerAlreadyAddedException", "");
        }
    }

    private Response processSetCustomersForRemovingDiscountCode(Query query) {
        try {
            int id = Integer.parseInt(query.getMethodInputs().get("id"));
            setCustomersForRemovingDiscountCode(query.getMethodInputs().get("username"), id);
            return new Response("void", "");
        } catch (NumberFormatException e) {
            return new Response("NumberFormatException", "");
        } catch (InvalidUsernameException e) {
            return new Response("InvalidUsernameException", "");
        } catch (CustomerAlreadyAddedException e) {
            return new Response("CustomerAlreadyAddedException", "");
        }
    }

    private Response processIsThereCustomerWithUsername(Query query) {
        boolean thereIsCustomer = isThereCustomerWithUsername(query.getMethodInputs().get("username"));
        Gson gson = new GsonBuilder().create();
        String thereIsCustomerGson = gson.toJson(thereIsCustomer);
        return new Response("Boolean", thereIsCustomerGson);
    }

    private Response processGetUserWithUsername(Query query) {
        try {
            SaveUser saveUser = new SaveUser(getUserWithUsername(query.getMethodInputs().get("username")));
            Gson gson = new GsonBuilder().create();
            String saveUserGson = gson.toJson(saveUser);
            return new Response("User", saveUserGson);
        } catch (InvalidUsernameException e) {
            return new Response("InvalidUsernameException", "");
        }

    }

    private Response processDeleteUser(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        User user = User.getUserById(id);
        deleteUser(user);
        return new Response("void", "");
    }

    private Response processGetAllRequests() {
        List<SaveRequest> allSaveRequest = new ArrayList<>();
        getAllRequests().forEach(c -> allSaveRequest.add(new SaveRequest(c)));
        Gson gson = new GsonBuilder().create();
        String saveRequestListGson = gson.toJson(allSaveRequest);
        return new Response("List<Request>", saveRequestListGson);
    }

    private Response processGetRequestWithId(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            SaveRequest saveRequest = new SaveRequest(getRequestWithId(id));
            Gson gson = new GsonBuilder().create();
            String saveRequestGson = gson.toJson(saveRequest);
            return new Response("Request", saveRequestGson);
        } catch (InvalidRequestIdException e) {
            return new Response("InvalidRequestIdException", "");
        }
    }

    private Response processRemoveProduct(Query query) {
        try {
            int id = Integer.parseInt(query.getMethodInputs().get("id"));
            Product product = getProductWithId(id);
            removeProduct(product);
            return new Response("void", "");
        } catch (InvalidProductIdException e) {
            return new Response("InvalidProductIdException", "");
        }
    }

    private Response processDeclineRequest(Query query) {
        try {
            int id = Integer.parseInt(query.getMethodInputs().get("id"));
            declineRequest(id);
            return new Response("void", "");
        } catch (InvalidRequestIdException e) {
            return new Response("InvalidRequestIdException", "");
        }
    }

    private Response processGetAllProducts() {
        List<SaveProduct> allSaveProducts = new ArrayList<>();
        getAllProducts().forEach(c -> allSaveProducts.add(new SaveProduct(c)));
        Gson gson = new GsonBuilder().create();
        String saveProductListGson = gson.toJson(allSaveProducts);
        return new Response("List<Product>", saveProductListGson);
    }

    private Response processGetProductWithId(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            SaveProduct saveProduct = new SaveProduct(getProductWithId(id));
            Gson gson = new GsonBuilder().create();
            String saveProductGson = gson.toJson(saveProduct);
            return new Response("Product", saveProductGson);
        } catch (InvalidProductIdException e) {
            return new Response("InvalidProductIdException", "");
        }
    }

    private Response processSortUsers(Query query) {
        try {
            List<SaveUser> allSaveUsers = new ArrayList<>();
            sortUsers(query.getMethodInputs().get("field"), query.getMethodInputs().get("type")).forEach(c -> allSaveUsers.add(new SaveUser(c)));
            Gson gson = new GsonBuilder().create();
            String saveUserListGson = gson.toJson(allSaveUsers);
            return new Response("List<User>", saveUserListGson);
        } catch (ProductsController.NoSortException e) {
            return new Response("ProductsController.NoSortException", "");
        }
    }

    private Response processSortDiscountCodes(Query query) {
        try {
            List<SaveDiscount> allSaveDiscounts = new ArrayList<>();
            sortDiscountCodes(query.getMethodInputs().get("field"), query.getMethodInputs().get("type")).forEach(c -> allSaveDiscounts.add(new SaveDiscount(c)));
            Gson gson = new GsonBuilder().create();
            String saveDiscountListGson = gson.toJson(allSaveDiscounts);
            return new Response("List<Discount>", saveDiscountListGson);
        } catch (ProductsController.NoSortException e) {
            return new Response("ProductsController.NoSortException", "");
        }
    }

    private Response processFilterRequests(Query query) {
        List<SaveRequest> allSaveRequest = new ArrayList<>();
        filterRequests(query.getMethodInputs().get("input")).forEach(c -> allSaveRequest.add(new SaveRequest(c)));
        Gson gson = new GsonBuilder().create();
        String saveRequestListGson = gson.toJson(allSaveRequest);
        return new Response("List<Request>", saveRequestListGson);
    }

    private Response processSortRequests(Query query){
        try {
            List<SaveRequest> allSaveRequest = new ArrayList<>();
            sortRequests(query.getMethodInputs().get("field"), query.getMethodInputs().get("type")).forEach(c -> allSaveRequest.add(new SaveRequest(c)));
            Gson gson = new GsonBuilder().create();
            String saveRequestListGson = gson.toJson(allSaveRequest);
            return new Response("List<Request>", saveRequestListGson);
        } catch (ProductsController.NoSortException e) {
            return new Response("ProductsController.NoSortException", "");
        }
    }

    private Response processSortProducts(Query query){
        try {
            List<SaveProduct> allSaveProducts = new ArrayList<>();
            sortProducts(query.getMethodInputs().get("field"), query.getMethodInputs().get("type")).forEach(c -> allSaveProducts.add(new SaveProduct(c)));
            Gson gson = new GsonBuilder().create();
            String saveProductListGson = gson.toJson(allSaveProducts);
            return new Response("List<Product>", saveProductListGson);
        } catch (ProductsController.NoSortException e) {
            return new Response("ProductsController.NoSortException", "");
        }
    }

    public static class InvalidDiscountIdException extends Exception {
        public InvalidDiscountIdException() {
            super("there is no discount with this id");
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

    public static class InvalidProductIdException extends Exception {
        public InvalidProductIdException(String message) {
            super(message);
        }
    }

    public static class CustomerAlreadyAddedException extends Exception {
    }

}
