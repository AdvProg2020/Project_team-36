package Controllers;

import Models.*;

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
        new Request(new ProductField(price,seller,supply,product.getProductId()));
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
            off.setStartTime(endDate);
        }
    }

    public void editOffPercent(String newPercentage, Sale off) throws NumberFormatException, InvalidRangeException{
        try {
            int percentage = Integer.parseInt(newPercentage);
            if (percentage < 100 && percentage > 0) {
                off.setSalePercent(percentage * 0.01);
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
    }

    public void removeProductsFromOff(Sale off){
        off.removeProducts(productsToBeEditedForOff);
    }

    public void sendEditOffRequest(Sale off){
        new Request(off);
    }

    private void writeOffFieldsSetters() {
        OffFieldsSetters.put("start\\s+date", "editOffStartDate");
        OffFieldsSetters.put("end\\s+date", "editOffEndDate");
        OffFieldsSetters.put("off\\s+percent", "editOffPercent");
        OffFieldsSetters.put("products\\s+included", "editOffProductsIncluded");
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
