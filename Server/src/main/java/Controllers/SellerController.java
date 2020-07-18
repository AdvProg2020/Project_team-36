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

public class SellerController extends UserController{

    private static HashMap<String, String> OffFieldsSetters = new HashMap<>();
    private static ArrayList<Product> productsToBeEditedForOff = new ArrayList<>();

    public SellerController(GlobalVariables userVariables) {
        super(userVariables);
        writeOffFieldsSetters();

    }

    public Seller getLoggedInSeller(){
        return (Seller)userVariables.getLoggedInUser();
    }

    public long getLoggedInSellerBalance() {
        return ((Seller)userVariables.getLoggedInUser()).getCredit();
    }

    public String getLoggedInSellerCompanyInformation(){
        String output = "";
        output += "company Name:" + ((Seller) userVariables.getLoggedInUser()).getCompanyName();
        if (!((Seller)userVariables.getLoggedInUser()).getCompanyInfo().equals("")){
            output += "\n" + "Info:" + ((Seller) userVariables.getLoggedInUser()).getCompanyInfo();
        }
        return output;
    }

    public void removeSellerProduct(int productId) throws NoProductForSeller {
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        if(!seller.isThereProduct(productId))
            throw new NoProductForSeller();
        Product.getProduct(productId).removeSeller(seller);
    }

    public Category getMainCategory(){
        return Category.getMainCategory();
    }

    public ArrayList<Product> getSellerProducts(){
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        return seller.getAllProducts();
    }

    public Product getSellerProductWithId(int id) throws NoProductForSeller {
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        if(seller.isThereProduct(id)){
            return Product.getProduct(id);
        } else {
            throw new NoProductForSeller();
        }
    }

    public ArrayList<Product> getAllProducts(){
        return Product.getAllProducts();
    }

    public Product getProductWithId(int id) throws InvalidProductIdException {
        if(Product.isThereProductWithId(id)){
            return Product.getProduct(id);
        } else {
            throw new InvalidProductIdException();
        }
    }

    public StringBuilder getSellerProductDetail(Product product){
        return product.printSellerProductDetails((Seller)userVariables.getLoggedInUser());
    }

    public HashSet<Customer> getAllBuyers(Product product){
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        return product.getProductFieldBySeller(seller).getAllBuyers();
    }

    public void sendAddSellerToProductRequest(long price, int supply, Product product){
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        new Request(new ProductField(price,seller,supply,product.getProductId()),Status.TO_BE_ADDED);
    }

    public ArrayList<Category> getAllCategories(){
        return Category.getAllCategories();
    }

    public ArrayList<SellerLog> getAllSellerLogs(){
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        return seller.getAllLogs();
    }

    public ArrayList<Sale> getAllSellerSales(){
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        return seller.getAllSales();
    }

    public Sale getSaleWithId(int id)throws InvalidOffIdException{
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        if(seller.sellerHasTheOff(id)){
            seller.getSaleWithId(id);
        }else{
            throw new InvalidOffIdException();
        }
        return null;
    }

    public Sale getOffCopy(Sale off){
        return new Sale(off);
    }

    public Method getOffFieldEditor(String chosenField,SellerController sellerController) throws NoSuchMethodException{
        for (String regex : OffFieldsSetters.keySet()) {
            if (chosenField.matches(regex)) {
                return sellerController.getClass().getMethod(OffFieldsSetters.get(regex),String.class,Sale.class);
            }
        }
        throw new NoSuchMethodException();
    }

    public void invokeOffEditor(String newValue,Sale off, Method editor) throws IllegalAccessException, InvocationTargetException {
        editor.invoke(this,newValue,off);
    }

    public void editOffStartDate(String newStartDate, Sale off) throws StartDateAfterEndDateException, InvalidDateFormatException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        Date startDate;
        try {
            startDate = dateFormat.parse(newStartDate);
        } catch (ParseException e) {
            throw new InvalidDateFormatException();
        }
        if (startDate.after(off.getEndTime())) {
            throw new StartDateAfterEndDateException();
        } else {
            off.setStartTime(startDate);
            off.setEditedField("startTime");
        }
    }

    public void editOffEndDate(String newEndDate, Sale off) throws EndDateBeforeStartDateException,InvalidDateFormatException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        Date endDate;
        try {
            endDate = dateFormat.parse(newEndDate);
        } catch (ParseException e) {
            throw new InvalidDateFormatException();
        }
        if (endDate.before(off.getStartTime())) {
            throw new EndDateBeforeStartDateException();
        } else {
            off.setEndTime(endDate);
            off.setEditedField("endTime");
        }
    }

    public void editOffPercent(String newPercentage, Sale off) throws NumberFormatException, InvalidRangeException{
        try {
            int percentage = Integer.parseInt(newPercentage);
            if (percentage < 100 && percentage > 0) {
                off.setSalePercent(percentage * 0.01);
                off.setEditedField("salePercent");
            } else {
                throw new InvalidRangeException();
            }
        } catch (InvalidRangeException e){
            throw new InvalidRangeException();
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
    }

    private boolean isThereProduct(int productId){
        for (Product product : productsToBeEditedForOff) {
            if(product.getProductId()==productId){
                return true;
            }
        }
        return false;
    }

    public void setProductsToBeRemovedFromOff(int productId,Sale off) throws InvalidProductIdException, ProductAlreadyAddedException {
        for (Product product : getProductsInOff(off)) {
            if(product.getProductId()==productId){
                if(isThereProduct(productId)){
                    throw new ProductAlreadyAddedException();
                } else {
                    productsToBeEditedForOff.add(Product.getProduct(productId));
                    return;
                }
            }
        }
        throw new InvalidProductIdException();
    }

    public void setProductsToBeAddedToOff(int productId,Sale off) throws InvalidProductIdException, ProductAlreadyAddedException {
        for (Product product : getProductsNotInOff(off)) {
            if(product.getProductId()==productId){
                if(isThereProduct(productId)){
                    throw new ProductAlreadyAddedException();
                } else {
                    productsToBeEditedForOff.add(Product.getProduct(productId));
                    return;
                }
            }
        }
        throw new InvalidProductIdException();
    }

    public ArrayList<Product> getProductsNotInOff(Sale off){
        productsToBeEditedForOff.clear();
        ArrayList<Product> availableProducts = new ArrayList<>();
        for (Product product : getSellerProducts()) {
            if(!off.isThereProduct(product)){
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    public ArrayList<Product> getProductsInOff(Sale off){
        productsToBeEditedForOff.clear();
        ArrayList<Product> availableProducts = new ArrayList<>();
        for (Product product : getSellerProducts()) {
            if(off.isThereProduct(product)){
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    public void addProductsToOff(Sale off){
        off.addProducts(productsToBeEditedForOff);
        off.setEditedField("productsInSale");
    }

    public void removeProductsFromOff(Sale off){
        off.removeProducts(productsToBeEditedForOff);
        off.setEditedField("productsInSale");
    }

    public void sendEditOffRequest(Sale off){
        new Request(off,Status.TO_BE_EDITED);
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

    public void removeSale(Sale sale){
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
            case "getSellerProductDetail" -> processGetSellerProductDetail(query);
            case "getAllBuyers" -> processGetAllBuyers(query);
            case "sendAddSellerToProductRequest" -> processSendAddSellerToProductRequest(query);
            case "getAllCategories" -> processGetAllCategories(query);
            case "getAllSellerLogs" -> processGetAllSellerLogs(query);
            case "getAllSellerSales" -> processGetAllSellerSales(query);
            case "getSaleWithId" -> processGetSaleWithId(query);
            case "getOffCopy" -> processGetOffCopy(query);
            case "getOffFieldEditor" -> processGetOffFieldEditor(query);
            case "invokeOffEditor" -> processInvokeOffEditor(query);
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
            default -> new Response("Error", "");
        };
    }


    private Response processSendEditOffRequest(Query query) {
        //TODO karaneh
    }

    private Response processRemoveProductsFromOff(Query query) {
        Sale off = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        removeProductsFromOff(off);
        return new Response("void","");
    }

    private Response processAddProductsToOff(Query query) {
        Sale off = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        addProductsToOff(off);
        return new Response("void","");
    }

    private Response processGetProductsInOff(Query query) {
        Sale off = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        List<SaveProduct> products = new ArrayList<>();
        getProductsInOff(off).forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>",gson.toJson(products));
    }

    private Response processGetProductsNotInOff(Query query) {
        Sale off = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        List<SaveProduct> products = new ArrayList<>();
        getProductsNotInOff(off).forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>",gson.toJson(products));
    }

    private Response processSetProductsToBeAddedToOff(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        Sale off = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        try {
            setProductsToBeAddedToOff(productId,off);
            return new Response("void","");
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
            return new Response("InvalidProductIdException","");
        } catch (ProductAlreadyAddedException e) {
            e.printStackTrace();
            return new Response("ProductAlreadyAddedException","");
        }
    }

    private Response processSetProductsToBeRemovedFromOff(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        Sale off = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        try {
            setProductsToBeRemovedFromOff(productId,off);
            return new Response("void","");
        } catch (InvalidProductIdException e) {
            return new Response("InvalidProductIdException","");
        } catch (ProductAlreadyAddedException e) {
            e.printStackTrace();
            return new Response("ProductAlreadyAddedException","");
        }
    }

    private Response processEditOffPercent(Query query) {
        String newPercentage = query.getMethodInputs().get("newPercentage");
        Sale off = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        try {
            editOffPercent(newPercentage,off);
            return new Response("void","");
        } catch (InvalidRangeException e) {
            return new Response("InvalidRangeException","");
        }
    }

    private Response processEditOffEndDate(Query query) {
        String newEndDate = query.getMethodInputs().get("newEndDate");
        Sale sale = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        try {
            editOffEndDate(newEndDate,sale);
            return new Response("void","");
        } catch (EndDateBeforeStartDateException e) {
            return new Response("EndDateBeforeStartDateException","");
        } catch (InvalidDateFormatException e) {
            return new Response("InvalidDateFormatException","");
        }
    }

    private Response processEditOffStartDate(Query query) {
        String newStartDate = query.getMethodInputs().get("newEndDate");
        Sale sale = Sale.getSaleWithId(Integer.parseInt(query.getMethodInputs().get("off")));
        try {
            editOffStartDate(newStartDate,sale);
            return new Response("void","");
        } catch (InvalidDateFormatException e) {
            return new Response("InvalidDateFormatException","");
        } catch (StartDateAfterEndDateException e) {
            return new Response("StartDateAfterEndDateException","");
        }
    }

    private Response processInvokeOffEditor(Query query) {
       //TODO karaneh
    }

    private Response processGetOffFieldEditor(Query query) {
        //TODO karaneh
    }

    private Response processGetOffCopy(Query query) {
        //TODO karaneh getCopy
    }

    private Response processGetSaleWithId(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            SaveSale saveSale = new SaveSale(getSaleWithId(id));
            Gson gson = new GsonBuilder().create();
            return new Response("Sale",gson.toJson(saveSale));
        } catch (InvalidOffIdException e) {
            return new Response("InvalidOffIdException","");
        }
    }

    private Response processGetAllSellerSales(Query query) {
        List<SaveSale> allSales = new ArrayList<>();
        getAllSellerSales().forEach(sale -> allSales.add(new SaveSale(sale)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Sale>",gson.toJson(allSales));
    }

    private Response processGetAllSellerLogs(Query query) {
        List<SaveSellerLog> logs = new ArrayList<>();
        getAllSellerLogs().forEach(sellerLog -> logs.add(new SaveSellerLog(sellerLog)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<SellerLog>",gson.toJson(logs));
    }

    private Response processGetAllCategories(Query query) {
        List<SaveCategory> allCategories = new ArrayList<>();
        getAllCategories().forEach(category -> allCategories.add(new SaveCategory(category)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Category>",gson.toJson(allCategories));
    }

    private Response processSendAddSellerToProductRequest(Query query) {
        //TODO ask karaneh
        return null;
    }

    private Response processGetAllBuyers(Query query) {
        int productId =Integer.parseInt (query.getMethodInputs().get("product"));
        Set<SaveCustomer> customers = new HashSet<>();
        getAllBuyers(Product.getProduct(productId)).forEach(customer -> customers.add(new SaveCustomer(customer)));
        Gson gson = new GsonBuilder().create();
        return new Response("Set<Customer>",gson.toJson(customers));
    }

    private Response processGetSellerProductDetail(Query query) {
        //TODO ask karaneh
        return null;
    }

    private Response processGetProductWithId(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            Product product = getProductWithId(id);
            SaveProduct saveProduct = new SaveProduct(product);
            Gson gson = new GsonBuilder().create();
            return new Response("Product",gson.toJson(saveProduct));
        } catch (InvalidProductIdException e) {
            return new Response("InvalidProductIdException","");
        }

    }

    private Response processGetAllProducts(Query query) {
        List<SaveProduct> products = new ArrayList<>();
        getAllProducts().forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>",gson.toJson(products));
    }

    private Response processGetSellerProductWithId(Query query) {
        int id =Integer.parseInt (query.getMethodInputs().get("id"));
        try {
            Product product = getSellerProductWithId(id);
            SaveProduct saveProduct = new SaveProduct(product);
            Gson gson = new GsonBuilder().create();
            return new Response("Product",gson.toJson(saveProduct));
        } catch (NoProductForSeller noProductForSeller) {
            return new Response("NoProductForSeller","");
        }
    }

    private Response processGetSellerProducts(Query query) {
        List<SaveProduct> products = new ArrayList<>();
        getSellerProducts().forEach(product -> products.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>",gson.toJson(products));
    }

    private Response processGetMainCategory(Query query) {
        SaveCategory saveCategory = new SaveCategory(getMainCategory());
        Gson gson = new GsonBuilder().create();
        return new Response("Category",gson.toJson(saveCategory));
    }

    private Response processRemoveSellerProduct(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            removeSellerProduct(productId);
            return new Response("void","");
        } catch (NoProductForSeller noProductForSeller) {
            return new Response("NoProductForSeller","");
        }
    }

    private Response processGetLoggedInSellerCompanyInformation(Query query) {
        return new Response("String",getLoggedInSellerCompanyInformation());
    }

    private Response processGetLoggedInSellerBalance(Query query) {
        long credit = getLoggedInSellerBalance();
        return new Response("long",Long.toString(credit));
    }

    private Response processGetLoggedInSeller(Query query) {
        Seller seller = getLoggedInSeller();
        SaveSeller saveSeller = new SaveSeller(seller);
        Gson gson = new GsonBuilder().create();
        String toBeReturned = gson.toJson(saveSeller);
        return new Response("Seller",toBeReturned);
    }

    private Response processGetOffToView(){
        SaveSale saveSale = new SaveSale(getOffToView());
        Gson gson = new GsonBuilder().create();
        String toBeReturned = gson.toJson(saveSale);
        return new Response("Sale",toBeReturned);
    }

    private Response processGetOffToEdit(){
        SaveSale saveSale = new SaveSale(getOffToEdit());
        Gson gson = new GsonBuilder().create();
        String toBeReturned = gson.toJson(saveSale);
        return new Response("Sale",toBeReturned);
    }

    private Response processSetOffToView(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Sale offToView = Sale.getSaleById(id);
        setOffToView(offToView);
        return new Response("void", "");
    }

    private Response processSetOffToEdit(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Sale offToEdit = Sale.getSaleById(id);
        setOffToEdit(offToEdit);
        return new Response("void", "");
    }

    private Response processRemoveSale(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Sale offToRemove = Sale.getSaleById(id);
        removeSale(offToRemove);
        return new Response("void", "");
    }

    public static class InvalidDateFormatException extends Exception{
    }

    public static class StartDateAfterEndDateException extends Exception{
    }

    public static class EndDateBeforeStartDateException extends Exception{
    }

    public static class InvalidRangeException extends Exception{
    }

    public static class InvalidProductIdException extends Exception{
    }

    public static class InvalidOffIdException extends Exception{
    }

    public static class NoProductForSeller extends Exception{
    }

    public static class ProductAlreadyAddedException extends Exception{
    }
}
