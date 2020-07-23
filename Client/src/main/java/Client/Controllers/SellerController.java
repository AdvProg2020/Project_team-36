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
import java.util.*;

public class SellerController extends UserController {
    private final String controllerName = "SellerController";


    public Seller getLoggedInSeller() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getLoggedInSeller");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Seller")) {
            Gson gson = new Gson();
            SaveSeller saveSeller = gson.fromJson(response.getData(), SaveSeller.class);
            return new Seller(saveSeller);
        } else {
            return null;
        }
    }

    public long getLoggedInSellerBalance() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getLoggedInSellerBalance");
        Response response = Client.process(query);
        if (response.getReturnType().equals("long")) {
            Gson gson = new Gson();
            return gson.fromJson(response.getData(), Long.class);
        } else {
            return -1;
        }
    }

    public String getLoggedInSellerCompanyInformation() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getLoggedInSellerCompanyInformation");
        Response response = Client.process(query);
        Gson gson = new Gson();
        return gson.fromJson(response.getData(), String.class);
    }

    public void removeSellerProduct(int productId) throws NoProductForSeller {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeSellerProduct");
        query.getMethodInputs().put("productId", Integer.toString(productId));
        Response response = Client.process(query);
        if (response.getReturnType().equals("NoProductForSeller")) {
            throw new NoProductForSeller();
        }
    }

    public Category getMainCategory() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getMainCategory");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Category")) {
            Gson gson = new Gson();
            SaveCategory saveCategory = gson.fromJson(response.getData(), SaveCategory.class);
            return new Category(saveCategory);
        } else {
            return null;
        }
    }

    public ArrayList<Product> getSellerProducts() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getSellerProducts");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Product> allProducts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveProduct>>() {
        }.getType();
        List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(), type);
        allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
        return allProducts;
    }

    public Product getSellerProductWithId(int id) throws NoProductForSeller {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getSellerProductWithId");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        if (response.getReturnType().equals("Product")) {
            Gson gson = new Gson();
            SaveProduct saveProduct = gson.fromJson(response.getData(), SaveProduct.class);
            return new Product(saveProduct);
        } else if (response.getReturnType().equals("NoProductForSeller")) {
            throw new NoProductForSeller();
        } else {
            return null;
        }
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

    public HashSet<Customer> getAllBuyers(Product product) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllBuyers");
        query.getMethodInputs().put("id", Integer.toString(product.getProductId()));
        Response response = Client.process(query);
        Gson gson = new Gson();
        HashSet<Customer> allBuyers = new HashSet<>();
        Type type = new TypeToken<ArrayList<SaveCustomer>>() {
        }.getType();
        Set<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(), type);
        allSaveCustomers.forEach(saveCustomer -> allBuyers.add(new Customer(saveCustomer)));
        return allBuyers;
    }

    public void sendAddSellerToProductRequest(long price, int supply, Product product) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllBuyers");
        query.getMethodInputs().put("id", Integer.toString(product.getProductId()));
        query.getMethodInputs().put("price", Long.toString(price));
        query.getMethodInputs().put("supply", Integer.toString(supply));
        Client.process(query);
    }

    public ArrayList<Category> getAllCategories() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllCategories");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Category> allCategories = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveCategory>>() {
        }.getType();
        List<SaveCategory> allSaveCategories = gson.fromJson(response.getData(), type);
        allSaveCategories.forEach(saveCategory -> allCategories.add(new Category(saveCategory)));
        return allCategories;
    }

    public ArrayList<SellerLog> getAllSellerLogs() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllSellerLogs ");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<SellerLog> allSellerLogs = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveSellerLog>>() {
        }.getType();
        List<SaveSellerLog> allSaveSellerLogs = gson.fromJson(response.getData(), type);
        allSaveSellerLogs.forEach(saveSellerLog -> allSellerLogs.add(new SellerLog(saveSellerLog)));
        return allSellerLogs;
    }

    public ArrayList<Sale> getAllSellerSales() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllSellerSales ");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Sale> allSales = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveSale>>() {
        }.getType();
        List<SaveSale> allSaveSales = gson.fromJson(response.getData(), type);
        allSaveSales.forEach(saveSale -> allSales.add(new Sale(saveSale)));
        return allSales;
    }

    public Sale getSaleWithId(int id) throws InvalidOffIdException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getSaleWithId");
        query.getMethodInputs().put("id", Integer.toString(id));
        Response response = Client.process(query);
        if (response.getReturnType().equals("Sale")) {
            Gson gson = new Gson();
            SaveSale saveSale = gson.fromJson(response.getData(), SaveSale.class);
            return new Sale(saveSale);
        } else if (response.getReturnType().equals("InvalidOffIdException")) {
            throw new InvalidOffIdException();
        } else {
            return null;
        }
    }

    public Sale getOffCopy(Sale off) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getOffCopy");
        query.getMethodInputs().put("id", Integer.toString(off.getOffId()));
        Response response = Client.process(query);
        Gson gson = new Gson();
        SaveSale saveSale = gson.fromJson(response.getData(), SaveSale.class);
        return new Sale(saveSale);
    }

    public void addProductToOff(Product product){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addProductToOff");
        query.getMethodInputs().put("id", Integer.toString(product.getProductId()));
        Client.process(query);
    }

    public void finalizeAddingProducts(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "finalizeAddingProducts");
        Client.process(query);
    }

    public void editOffStartDate(String newStartDate) throws StartDateAfterEndDateException, InvalidDateFormatException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editOffStartDate");
        query.getMethodInputs().put("newStartDate", newStartDate);
        Response response = Client.process(query);
        if (response.getReturnType().equals("StartDateAfterEndDateException")) {
            throw new StartDateAfterEndDateException();
        } else if (response.getReturnType().equals("InvalidDateFormatException")) {
            throw new InvalidDateFormatException();
        }
    }

    public void editOffEndDate(String newEndDate) throws EndDateBeforeStartDateException, InvalidDateFormatException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editOffEndDate");
        query.getMethodInputs().put("newEndDate", newEndDate);
        Response response = Client.process(query);
        if (response.getReturnType().equals("EndDateBeforeStartDateException")) {
            throw new EndDateBeforeStartDateException();
        } else if (response.getReturnType().equals("InvalidDateFormatException")) {
            throw new InvalidDateFormatException();
        }
    }

    public void editOffPercent(String newPercentage) throws NumberFormatException, InvalidRangeException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editOffPercent");
        query.getMethodInputs().put("newPercentage", newPercentage);
        Response response = Client.process(query);
        if (response.getReturnType().equals("NumberFormatException")) {
            throw new NumberFormatException();
        } else if (response.getReturnType().equals("InvalidRangeException")) {
            throw new InvalidRangeException();
        }
    }

    public void setProductsToBeRemovedFromOff(int productId) throws InvalidProductIdException, ProductAlreadyAddedException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setProductsToBeRemovedFromOff");
        query.getMethodInputs().put("productId", Integer.toString(productId));
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidProductIdException")) {
            throw new InvalidProductIdException();
        } else if (response.getReturnType().equals("ProductAlreadyAddedException")) {
            throw new ProductAlreadyAddedException();
        }
    }

    public void setProductsToBeAddedToOff(int productId) throws InvalidProductIdException, ProductAlreadyAddedException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setProductsToBeAddedToOff");
        query.getMethodInputs().put("productId", Integer.toString(productId));
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidProductIdException")) {
            throw new InvalidProductIdException();
        } else if (response.getReturnType().equals("ProductAlreadyAddedException")) {
            throw new ProductAlreadyAddedException();
        }
    }

    public ArrayList<Product> getProductsNotInOff() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProductsNotInOff");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Product> allProducts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveProduct>>() {
        }.getType();
        List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(), type);
        allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
        return allProducts;
    }

    public ArrayList<Product> getProductsInOff() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProductsInOff");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Product> allProducts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveProduct>>() {
        }.getType();
        List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(), type);
        allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
        return allProducts;
    }

    public void addProductsToOff() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addProductsToOff");
        Client.process(query);
    }

    public void removeProductsFromOff() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeProductsFromOff");
        Client.process(query);
    }

    public void sendEditOffRequest() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sendEditOffRequest");
        Client.process(query);
    }


    public Sale getOffToView() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getOffToView");
        Response response = Client.process(query);
        Gson gson = new Gson();
        SaveSale saveSale = gson.fromJson(response.getData(), SaveSale.class);
        return new Sale(saveSale);
    }

    public Sale getOffToEdit() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getOffToEdit");
        Response response = Client.process(query);
        Gson gson = new Gson();
        SaveSale saveSale = gson.fromJson(response.getData(), SaveSale.class);
        return new Sale(saveSale);
    }

    public void setOffToView(Sale offToView) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setOffToView");
        query.getMethodInputs().put("id", Integer.toString(offToView.getOffId()));
        Client.process(query);
    }

    public void setOffToEdit(Sale offToEdit) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setOffToEdit");
        query.getMethodInputs().put("id", Integer.toString(offToEdit.getOffId()));
        Client.process(query);
    }

    public void removeSale(Sale sale) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeSale");
        query.getMethodInputs().put("id", Integer.toString(sale.getOffId()));
        Client.process(query);
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
