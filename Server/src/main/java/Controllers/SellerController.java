package Controllers;

import Models.*;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SellerController extends UserController {

    private static HashMap<String, String> OffFieldsSetters = new HashMap<>();
    private static ArrayList<Product> productsToBeEditedForOff = new ArrayList<>();
    private static ArrayList<Product> productsToBeInOff = new ArrayList<>();
    private Sale editingOff;

    public SellerController(GlobalVariables userVariables) {
        super(userVariables);
        writeOffFieldsSetters();

    }

    public Seller getLoggedInSeller() {
        return (Seller) userVariables.getLoggedInUser();
    }

    public long getLoggedInSellerBalance() {
        return ((Seller) userVariables.getLoggedInUser()).getCredit();
    }

    public String getLoggedInSellerCompanyInformation() {
        String output = "";
        output += "company Name:" + ((Seller) userVariables.getLoggedInUser()).getCompanyName();
        if (!((Seller) userVariables.getLoggedInUser()).getCompanyInfo().equals("")) {
            output += "\n" + "Info:" + ((Seller) userVariables.getLoggedInUser()).getCompanyInfo();
        }
        return output;
    }

    public void removeSellerProduct(int productId) throws NoProductForSeller {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        if (!seller.isThereProduct(productId))
            throw new NoProductForSeller();
        Product.getProduct(productId).removeSeller(seller);
    }

    public Category getMainCategory() {
        return Category.getMainCategory();
    }

    public ArrayList<Product> getSellerProducts() {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        return seller.getAllProducts();
    }

    public Product getSellerProductWithId(int id) throws NoProductForSeller {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        if (seller.isThereProduct(id)) {
            return Product.getProduct(id);
        } else {
            throw new NoProductForSeller();
        }
    }

    public long getMoneyInWallet(int sellerId){
        return Seller.getSellerById(sellerId).getWallet().getTotalMoney();
    }

    public boolean isThereEnoughAvailable(long money, int sellerId){
        return Seller.getSellerById(sellerId).getWallet().isThereEnoughMoneyAvailable(money);
    }

    public void chargeWallet(long money, int sellerId){
        Seller.getSellerById(sellerId).getWallet().chargeWallet(money);
    }

    public void withdrawFromWallet(long money, int sellerId){
        Seller.getSellerById(sellerId).getWallet().withdrawMoney(money);
    }

    public ArrayList<Product> getAllProducts() {
        return Product.getAllProducts();
    }

    public Product getProductWithId(int id) throws InvalidProductIdException {
        if (Product.isThereProductWithId(id)) {
            return Product.getProduct(id);
        } else {
            throw new InvalidProductIdException();
        }
    }

    public HashSet<Customer> getAllBuyers(Product product) {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        return product.getProductFieldBySeller(seller).getAllBuyers();
    }

    public void sendAddSellerToProductRequest(long price, int supply, Product product) {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        new Request(new ProductField(price, seller, supply, product.getProductId()), Status.TO_BE_ADDED);
    }

    public ArrayList<Category> getAllCategories() {
        return Category.getAllCategories();
    }

    public ArrayList<SellerLog> getAllSellerLogs() {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        return seller.getAllLogs();
    }

    public ArrayList<Sale> getAllSellerSales() {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        return seller.getAllSales();
    }

    public Sale getSaleWithId(int id) throws InvalidOffIdException {
        Seller seller = ((Seller) userVariables.getLoggedInUser());
        if (seller.sellerHasTheOff(id)) {
            seller.getSaleWithId(id);
        } else {
            throw new InvalidOffIdException();
        }
        return null;
    }

    public Sale getOffCopy(Sale off) {
        editingOff = new Sale(off);
        productsToBeInOff.clear();
        return editingOff;
    }

    public Method getOffFieldEditor(String chosenField, SellerController sellerController) throws NoSuchMethodException {
        for (String regex : OffFieldsSetters.keySet()) {
            if (chosenField.matches(regex)) {
                return sellerController.getClass().getMethod(OffFieldsSetters.get(regex), String.class, Sale.class);
            }
        }
        throw new NoSuchMethodException();
    }

    public void invokeOffEditor(String newValue, Sale off, Method editor) throws IllegalAccessException, InvocationTargetException {
        editor.invoke(this, newValue, off);
    }

    public void editOffStartDate(String newStartDate) throws StartDateAfterEndDateException, InvalidDateFormatException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        Date startDate;
        try {
            startDate = dateFormat.parse(newStartDate);
        } catch (ParseException e) {
            throw new InvalidDateFormatException();
        }
        if (startDate.after(editingOff.getEndTime())) {
            throw new StartDateAfterEndDateException();
        } else {
            editingOff.setStartTime(startDate);
            editingOff.setEditedField("startTime");
        }
    }

    public void editOffEndDate(String newEndDate) throws EndDateBeforeStartDateException, InvalidDateFormatException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        Date endDate;
        try {
            endDate = dateFormat.parse(newEndDate);
        } catch (ParseException e) {
            throw new InvalidDateFormatException();
        }
        if (endDate.before(editingOff.getStartTime())) {
            throw new EndDateBeforeStartDateException();
        } else {
            editingOff.setEndTime(endDate);
            editingOff.setEditedField("endTime");
        }
    }

    public void editOffPercent(String newPercentage) throws NumberFormatException, InvalidRangeException {
        try {
            int percentage = Integer.parseInt(newPercentage);
            if (percentage < 100 && percentage > 0) {
                editingOff.setSalePercent(percentage * 0.01);
                editingOff.setEditedField("salePercent");
            } else {
                throw new InvalidRangeException();
            }
        } catch (InvalidRangeException e) {
            throw new InvalidRangeException();
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
    }

    private boolean isThereProduct(int productId) {
        for (Product product : productsToBeEditedForOff) {
            if (product.getProductId() == productId) {
                return true;
            }
        }
        return false;
    }

    public void setProductsToBeRemovedFromOff(int productId) throws InvalidProductIdException, ProductAlreadyAddedException {
        for (Product product : getProductsInOff()) {
            if (product.getProductId() == productId) {
                if (isThereProduct(productId)) {
                    throw new ProductAlreadyAddedException();
                } else {
                    productsToBeEditedForOff.add(Product.getProduct(productId));
                    return;
                }
            }
        }
        throw new InvalidProductIdException();
    }

    public void setProductsToBeAddedToOff(int productId) throws InvalidProductIdException, ProductAlreadyAddedException {
        for (Product product : getProductsNotInOff()) {
            if (product.getProductId() == productId) {
                if (isThereProduct(productId)) {
                    throw new ProductAlreadyAddedException();
                } else {
                    productsToBeEditedForOff.add(Product.getProduct(productId));
                    return;
                }
            }
        }
        throw new InvalidProductIdException();
    }

    public ArrayList<Product> getProductsNotInOff() {
        productsToBeEditedForOff.clear();
        ArrayList<Product> availableProducts = new ArrayList<>();
        for (Product product : getSellerProducts()) {
            if (!editingOff.isThereProduct(product)) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    public ArrayList<Product> getProductsInOff() {
        productsToBeEditedForOff.clear();
        ArrayList<Product> availableProducts = new ArrayList<>();
        for (Product product : getSellerProducts()) {
            if (editingOff.isThereProduct(product)) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    public void addProductsToOff() {
        editingOff.addProducts(productsToBeEditedForOff);
        editingOff.setEditedField("productsInSale");
    }

    public void addProductToOff(Product product){
        productsToBeInOff.add(product);
    }

    public void finalizeAddingProducts(){
        editingOff.setProductsInSale(productsToBeInOff);
    }

    public void removeProductsFromOff() {
        editingOff.removeProducts(productsToBeEditedForOff);
        editingOff.setEditedField("productsInSale");
    }

    public void sendEditOffRequest() {
        new Request(editingOff, Status.TO_BE_EDITED);
    }

    private void writeOffFieldsSetters() {
        OffFieldsSetters.put("start\\s+date", "editOffStartDate");
        OffFieldsSetters.put("end\\s+date", "editOffEndDate");
        OffFieldsSetters.put("off\\s+percent", "editOffPercent");
        OffFieldsSetters.put("products\\s+included", "editOffProductsIncluded");
    }

    public Sale getOffToView() {
        return Sale.getOffToView();
    }

    public Sale getOffToEdit() {
        return Sale.getOffToEdit();
    }

    public void setOffToView(Sale offToView) {
        Sale.setOffToView(offToView);
    }

    public void setOffToEdit(Sale offToEdit) {
        Sale.setOffToEdit(offToEdit);
    }

    public void removeSale(Sale sale) {
        Sale.removeSale(sale);
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "getLoggedInSeller" -> processGetLoggedInSeller(query);
            case "getLoggedInSellerBalance" -> processGetLoggedInSellerBalance(query);
            case "getLoggedInSellerCompanyInformation" -> processGetLoggedInSellerCompanyInformation(query);
            case "removeSellerProduct" -> processRemoveSellerProduct(query);
            case "getMainCategory" -> processGetMainCategory(query);
            case "getSellerProducts" -> processGetSellerProducts(query);
            case "getSellerProductWithId" -> processGetSellerProductWithId(query);
            case "getAllProducts" -> processGetAllProducts(query);
            case "getProductWithId" -> processGetProductWithId(query);
            case "getAllBuyers" -> processGetAllBuyers(query);
            case "sendAddSellerToProductRequest" -> processSendAddSellerToProductRequest(query);
            case "getAllCategories" -> processGetAllCategories(query);
            case "getAllSellerLogs" -> processGetAllSellerLogs(query);
            case "getAllSellerSales" -> processGetAllSellerSales(query);
            case "getSaleWithId" -> processGetSaleWithId(query);
            case "getOffCopy" -> processGetOffCopy(query);
            case "editOffStartDate" -> processEditOffStartDate(query);
            case "editOffEndDate" -> processEditOffEndDate(query);
            case "editOffPercent" -> processEditOffPercent(query);
            case "setProductsToBeRemovedFromOff" -> processSetProductsToBeRemovedFromOff(query);
            case "setProductsToBeAddedToOff" -> processSetProductsToBeAddedToOff(query);
            case "getProductsNotInOff" -> processGetProductsNotInOff(query);
            case "getProductsInOff" -> processGetProductsInOff(query);
            case "addProductsToOff" -> processAddProductsToOff(query);
            case "removeProductsFromOff" -> processRemoveProductsFromOff(query);
            case "sendEditOffRequest" -> processSendEditOffRequest(query);
            case "getOffToView" -> processGetOffToView();
            case "getOffToEdit" -> processGetOffToEdit();
            case "setOffToView" -> processSetOffToView(query);
            case "setOffToEdit" -> processSetOffToEdit(query);
            case "removeSale" -> processRemoveSale(query);
            case "addProductToOff" -> processAddProductToOff(query);
            case "finalizeAddingProducts" -> processFinalizeAddingProducts();
            case "getMoneyInWallet" -> processGetMoneyInWallet(query);
            case "chargeWallet" -> processChargeWallet(query);
            case "withdrawFromWallet" -> processWithdrawFromWallet(query);
            case "isThereEnoughAvailable" -> processIsThereEnoughAvailable(query);
            default -> new Response("Error", "");
        };
    }


    private Response processSendEditOffRequest(Query query) {
        sendEditOffRequest();
        return new Response("void", "");
    }

    private Response processRemoveProductsFromOff(Query query) {
        removeProductsFromOff();
        return new Response("void", "");
    }

    private Response processAddProductsToOff(Query query) {
        addProductsToOff();
        return new Response("void", "");
    }

    private Response processGetProductsInOff(Query query) {
        List<SaveProduct> products = new ArrayList<>();
        getProductsInOff().forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>", gson.toJson(products));
    }

    private Response processGetProductsNotInOff(Query query) {
        List<SaveProduct> products = new ArrayList<>();
        getProductsNotInOff().forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>", gson.toJson(products));
    }

    private Response processSetProductsToBeAddedToOff(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            setProductsToBeAddedToOff(productId);
            return new Response("void", "");
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
            return new Response("InvalidProductIdException", "");
        } catch (ProductAlreadyAddedException e) {
            e.printStackTrace();
            return new Response("ProductAlreadyAddedException", "");
        }
    }

    private Response processSetProductsToBeRemovedFromOff(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            setProductsToBeRemovedFromOff(productId);
            return new Response("void", "");
        } catch (InvalidProductIdException e) {
            return new Response("InvalidProductIdException", "");
        } catch (ProductAlreadyAddedException e) {
            e.printStackTrace();
            return new Response("ProductAlreadyAddedException", "");
        }
    }

    private Response processEditOffPercent(Query query) {
        String newPercentage = query.getMethodInputs().get("newPercentage");
        try {
            editOffPercent(newPercentage);
            return new Response("void", "");
        } catch (InvalidRangeException e) {
            return new Response("InvalidRangeException", "");
        }
    }

    private Response processEditOffEndDate(Query query) {
        String newEndDate = query.getMethodInputs().get("newEndDate");
        try {
            editOffEndDate(newEndDate);
            return new Response("void", "");
        } catch (EndDateBeforeStartDateException e) {
            return new Response("EndDateBeforeStartDateException", "");
        } catch (InvalidDateFormatException e) {
            return new Response("InvalidDateFormatException", "");
        }
    }

    private Response processEditOffStartDate(Query query) {
        String newStartDate = query.getMethodInputs().get("newStartDate");
        try {
            editOffStartDate(newStartDate);
            return new Response("void", "");
        } catch (InvalidDateFormatException e) {
            return new Response("InvalidDateFormatException", "");
        } catch (StartDateAfterEndDateException e) {
            return new Response("StartDateAfterEndDateException", "");
        }
    }


    private Response processGetOffCopy(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        SaveSale saveSale = new SaveSale(getOffCopy(Sale.getSaleById(id)));
        Gson gson = new GsonBuilder().create();
        String saveSaleGson = gson.toJson(saveSale);
        return new Response("Sale", saveSaleGson);
    }

    private Response processGetSaleWithId(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            SaveSale saveSale = new SaveSale(getSaleWithId(id));
            Gson gson = new GsonBuilder().create();
            return new Response("Sale", gson.toJson(saveSale));
        } catch (InvalidOffIdException e) {
            return new Response("InvalidOffIdException", "");
        }
    }

    private Response processGetAllSellerSales(Query query) {
        List<SaveSale> allSales = new ArrayList<>();
        getAllSellerSales().forEach(sale -> allSales.add(new SaveSale(sale)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Sale>", gson.toJson(allSales));
    }

    private Response processGetAllSellerLogs(Query query) {
        List<SaveSellerLog> logs = new ArrayList<>();
        getAllSellerLogs().forEach(sellerLog -> logs.add(new SaveSellerLog(sellerLog)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<SellerLog>", gson.toJson(logs));
    }

    private Response processGetAllCategories(Query query) {
        List<SaveCategory> allCategories = new ArrayList<>();
        getAllCategories().forEach(category -> allCategories.add(new SaveCategory(category)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Category>", gson.toJson(allCategories));
    }

    private Response processSendAddSellerToProductRequest(Query query) {
        Product product = Product.getProductById(Integer.parseInt(query.getMethodInputs().get("id")));
        int supply = Integer.parseInt(query.getMethodInputs().get("supply"));
        long price = Long.parseLong(query.getMethodInputs().get("price"));
        sendAddSellerToProductRequest(price, supply, product);
        return new Response("void", "");
    }

    private Response processGetAllBuyers(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("id"));
        Set<SaveCustomer> customers = new HashSet<>();
        getAllBuyers(Product.getProduct(productId)).forEach(customer -> customers.add(new SaveCustomer(customer)));
        Gson gson = new GsonBuilder().create();
        return new Response("Set<Customer>", gson.toJson(customers));
    }

//    private Response processGetSellerProductDetail(Query query) {
//        return null;
//    }

    private Response processGetProductWithId(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            Product product = getProductWithId(id);
            SaveProduct saveProduct = new SaveProduct(product);
            Gson gson = new GsonBuilder().create();
            return new Response("Product", gson.toJson(saveProduct));
        } catch (InvalidProductIdException e) {
            return new Response("InvalidProductIdException", "");
        }

    }

    private Response processGetAllProducts(Query query) {
        List<SaveProduct> products = new ArrayList<>();
        getAllProducts().forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>", gson.toJson(products));
    }

    private Response processGetSellerProductWithId(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            Product product = getSellerProductWithId(id);
            SaveProduct saveProduct = new SaveProduct(product);
            Gson gson = new GsonBuilder().create();
            return new Response("Product", gson.toJson(saveProduct));
        } catch (NoProductForSeller noProductForSeller) {
            return new Response("NoProductForSeller", "");
        }
    }

    private Response processGetSellerProducts(Query query) {
        List<SaveProduct> products = new ArrayList<>();
        getSellerProducts().forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>", gson.toJson(products));
    }

    private Response processGetMainCategory(Query query) {
        SaveCategory saveCategory = new SaveCategory(getMainCategory());
        Gson gson = new GsonBuilder().create();
        return new Response("Category", gson.toJson(saveCategory));
    }

    private Response processRemoveSellerProduct(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            removeSellerProduct(productId);
            return new Response("void", "");
        } catch (NoProductForSeller noProductForSeller) {
            return new Response("NoProductForSeller", "");
        }
    }

    private Response processGetLoggedInSellerCompanyInformation(Query query) {
        return new Response("String", getLoggedInSellerCompanyInformation());
    }

    private Response processGetLoggedInSellerBalance(Query query) {
        long credit = getLoggedInSellerBalance();
        return new Response("long", Long.toString(credit));
    }

    private Response processGetLoggedInSeller(Query query) {
        Seller seller = getLoggedInSeller();
        SaveSeller saveSeller = new SaveSeller(seller);
        Gson gson = new GsonBuilder().create();
        String toBeReturned = gson.toJson(saveSeller);
        return new Response("Seller", toBeReturned);
    }

    private Response processGetOffToView() {
        SaveSale saveSale = new SaveSale(getOffToView());
        Gson gson = new GsonBuilder().create();
        String toBeReturned = gson.toJson(saveSale);
        return new Response("Sale", toBeReturned);
    }

    private Response processGetOffToEdit() {
        SaveSale saveSale = new SaveSale(getOffToEdit());
        Gson gson = new GsonBuilder().create();
        String toBeReturned = gson.toJson(saveSale);
        return new Response("Sale", toBeReturned);
    }

    private Response processSetOffToView(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Sale offToView = Sale.getSaleById(id);
        setOffToView(offToView);
        return new Response("void", "");
    }

    private Response processSetOffToEdit(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Sale offToEdit = Sale.getSaleById(id);
        setOffToEdit(offToEdit);
        return new Response("void", "");
    }

    private Response processRemoveSale(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Sale offToRemove = Sale.getSaleById(id);
        removeSale(offToRemove);
        return new Response("void", "");
    }

    private Response processAddProductToOff(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Product product = Product.getProductById(id);
        addProductToOff(product);
        return new Response("void", "");
    }

    private Response processGetMoneyInWallet(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("sellerId"));
        long money = getMoneyInWallet(id);
        return new Response("long", Long.toString(money));
    }

    private Response processChargeWallet(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("sellerId"));
        long money = Long.parseLong(query.getMethodInputs().get("money"));
        chargeWallet(money, id);
        return new Response("void", "");
    }

    private Response processWithdrawFromWallet(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("sellerId"));
        long money = Long.parseLong(query.getMethodInputs().get("money"));
        withdrawFromWallet(money, id);
        return new Response("void", "");
    }

    private Response processIsThereEnoughAvailable(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("sellerId"));
        long money = Long.parseLong(query.getMethodInputs().get("money"));
        boolean available = isThereEnoughAvailable(money, id);
        return new Response("boolean", Boolean.toString(available));
    }

    private Response processFinalizeAddingProducts(){
        finalizeAddingProducts();
        return new Response("void", "");
    }

    public static class InvalidDateFormatException extends Exception {
    }

    public static class StartDateAfterEndDateException extends Exception {
    }

    public static class EndDateBeforeStartDateException extends Exception {
    }

    public static class InvalidRangeException extends Exception {
    }

    public static class InvalidProductIdException extends Exception {
    }

    public static class InvalidOffIdException extends Exception {
    }

    public static class NoProductForSeller extends Exception {
    }

    public static class ProductAlreadyAddedException extends Exception {
    }
}
