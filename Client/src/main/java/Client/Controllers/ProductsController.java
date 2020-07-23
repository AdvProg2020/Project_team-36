package Client.Controllers;

import Client.GUI.Constants;
import Client.Models.*;
import Client.Network.Client;
import Models.Query;
import Models.Response;
import Repository.SaveCategory;
import Repository.SaveComment;
import Repository.SaveProduct;
import Repository.SaveProductField;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class ProductsController implements ObjectController {
    private final String controllerName;
    private final GlobalVariables userVariables;


    public ProductsController(GlobalVariables userVariables) {
        this.controllerName = "ProductsController";
        this.userVariables = userVariables;
    }



    public Product getProduct(int id) throws NoProductWithId {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProduct");
        query.getMethodInputs().put("id",Integer.toString(id));
        Response response = Client.process(query);
        if (response.getReturnType().equals("Product")) {
            Gson gson = new Gson();
            SaveProduct saveProduct = gson.fromJson(response.getData(), SaveProduct.class);
            return new Product(saveProduct);
        }  if(response.getReturnType().equals("NoProductWithId")) {
            throw new NoProductWithId();
        } else{
            System.out.println(response.getData());
            System.exit(1);
            return null;
        }
    }

    public Category getMainCategory() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getMainCategory");
        Response response = Client.process(query);
            Gson gson = new Gson();
            return new Category(gson.fromJson(response.getData(),SaveCategory.class));
    }

    public void setSort(String name, String type) throws NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setSort");
        query.getMethodInputs().put("name",name);
        query.getMethodInputs().put("type",type);
        Response response = Client.process(query);
        if(response.getReturnType().equals("NoSortException"))
          throw new NoSortException();
    }

    @Override
    public Set<String> getAvailableFilters() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAvailableFilters");
        Response response = Client.process(query);
        Gson gson = new Gson();
        Type type = new TypeToken<HashSet<String>>() {
        }.getType();
        return gson.fromJson(response.getData(), type);
    }

    public HashSet<String> getCompanyNamesForFilter() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCompanyNamesForFilter");
        Response response = Client.process(query);
        Gson gson = new Gson();
        Type type = new TypeToken<HashSet<String>>() {
        }.getType();
        Set<String> filters;
         filters = gson.fromJson(response.getData(), type);
        return new HashSet<>(filters);

    }

    public void removeSortProduct() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeSortProduct");
        Client.process(query);
    }

    @Override
    public void removeFilter(String name) throws NoFilterWithNameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeFilter");
        query.getMethodInputs().put("name",name);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoFilterWithNameException")){
            throw new NoFilterWithNameException();
        }
    }
    @Override
    public void setNewFilter(String name) throws IntegerFieldException, OptionalFieldException, NoFilterWithNameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setNewFilter");
        query.getMethodInputs().put("name",name);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("IntegerFieldException"))
            throw new IntegerFieldException();
        else if(response.getReturnType().equalsIgnoreCase("OptionalFieldException"))
            throw new OptionalFieldException();
        else if(response.getReturnType().equalsIgnoreCase("NoFilterWithNameException"))
            throw new NoFilterWithNameException();
    }

    public void setCategoryFilter(String name) throws NoCategoryWithName {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCategoryFilter");
        query.getMethodInputs().put("name",name);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoCategoryWithName"))
            throw new NoCategoryWithName();
    }

    public void setFilterOptions(ArrayList<String> options) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFilterOptions");
        //TODO check kon ino nazanin
        List<String> temp = new ArrayList<>(options);
        Gson gson =  new GsonBuilder().create();
        String optionz = gson.toJson(temp);
        query.getMethodInputs().put("options",optionz);
        Client.process(query);
    }

    public void setFilterRange(String min, String max) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFilterRange");
        query.getMethodInputs().put("min",min);
        query.getMethodInputs().put("max",max);
        Client.process(query);
    }

    public ArrayList<Product> getFinalProductsList() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getFinalProductsList");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Product> allProducts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveProduct>>() {
        }.getType();
        List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(), type);
        allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
        return allProducts;
    }

    public void setChosenProduct(int productId) throws NoProductWithId {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setChosenProduct");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoProductWithId"))
            throw new NoProductWithId();
    }

    public Product getChosenProduct() throws NoProductWithId {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getChosenProduct");
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("Product")){
            Gson gson = new Gson();
            return new Product(gson.fromJson(response.getData(),SaveProduct.class));
        }else{
            throw new NoProductWithId();
        }
    }

    public void resetDigest() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "resetDigest");
        Client.process(query);
    }

    public void addSellerForBuy(String username) throws NotEnoughSupply, NoSellerWithUsername {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addSellerForBuy");
        query.getMethodInputs().put("username",username);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NotEnoughSupply"))
            throw new NotEnoughSupply();
        else if(response.getReturnType().equalsIgnoreCase("NoSellerWithUsername"))
            throw new NoSellerWithUsername();
    }

    public void addToCart() throws NoSellerIsChosen, EntryController.NotLoggedInException, UserCantBuy {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addToCart");
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoSellerIsChosen"))
            throw new NoSellerIsChosen();
        else if(response.getReturnType().equalsIgnoreCase("UserCantBuy"))
            throw new UserCantBuy();
        else if(response.getReturnType().equalsIgnoreCase("NotLoggedInException"))
            throw new EntryController.NotLoggedInException();
    }

    public Product compare(int productId) throws NoProductWithId, NotInTheSameCategory {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "compare");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("Product")){
            Gson gson = new Gson();
            return new Product(gson.fromJson(response.getData(),SaveProduct.class));
        }else if(response.getReturnType().equalsIgnoreCase("NoProductWithId")){
            throw new NoProductWithId();
        }else
            throw new NotInTheSameCategory();
    }

    public ArrayList<Comment> getProductComments() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProductComments");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Comment> allComments = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveComment>>() {
        }.getType();
        List<SaveComment> allSaveComments = gson.fromJson(response.getData(), type);
        allSaveComments.forEach(saveComment -> allComments.add(new Comment(saveComment)));
        return allComments;
    }

    public void addComment(String title, String content) throws EntryController.NotLoggedInException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addComment");
        query.getMethodInputs().put("title",title);
        query.getMethodInputs().put("content",content);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NotLoggedInException"))
            throw new EntryController.NotLoggedInException();
    }

    public void setCompanyFilter(ArrayList<String> options) {
        List<String> temp = new ArrayList<>(options);
        Gson gson = new GsonBuilder().create();
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCompanyFilter");
        query.getMethodInputs().put("options",gson.toJson(temp));
        Client.process(query);

    }

    public void addNameFilter(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addNameFilter");
        query.getMethodInputs().put("name",name);
        Client.process(query);
    }

    public void removeNameFilter(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeNameFilter");
        query.getMethodInputs().put("name",name);
        Client.process(query);
    }

    public void availabilityFilter() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "availabilityFilter");
        Client.process(query);
    }

    public void removeAvailabilityFilter() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeAvailabilityFilter");
        Client.process(query);
    }

    @Override
    public void addSellerFilter(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addSellerFilter");
        query.getMethodInputs().put("name",name);
        Client.process(query);
    }

    @Override
    public void removeSellerFilter(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeSellerFilter");
        query.getMethodInputs().put("name",name);
        Client.process(query);
    }

    @Override
    public ArrayList<String> getCategoryNames() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCategoryNames");
        Response response = Client.process(query);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> names = gson.fromJson(response.getData(), type);
        return new ArrayList<>(names);
    }

    @Override
    public ArrayList<String> getSpecialIntegerFilter() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getSpecialIntegerFilter");
        Response response = Client.process(query);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> names = gson.fromJson(response.getData(), type);
        return new ArrayList<>(names);
    }

    public HashMap<String, HashSet<String>> getAllOptionalChoices() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllOptionalChoices");
        Response response = Client.process(query);
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, HashSet<String>>>(){}.getType();
        HashMap<String, HashSet<String>> output = gson.fromJson(response.getData(),type);
        return output;
    }

    public void addOptionalFilter(String filterName, String option) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addOptionalFilter");
        query.getMethodInputs().put("filterName",filterName);
        query.getMethodInputs().put("option",option);
        Client.process(query);
    }

    public void removeOptionalFilter(String filterName, String option) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeOptionalFilter");
        query.getMethodInputs().put("filterName",filterName);
        query.getMethodInputs().put("option",option);
        Client.process(query);
    }

    public boolean canRate(Product product, User user) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "canRate");
        query.getMethodInputs().put("product",Integer.toString(product.getProductId()));
        query.getMethodInputs().put("user",Integer.toString(user.getUserId()));
        Response response = Client.process(query);
        return Boolean.getBoolean(response.getData());
    }

    public Product getProductToEdit() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProductToEdit");
        Response response = Client.process(query);
        Gson gson = new Gson();
        return new Product(gson.fromJson(response.getData(),SaveProduct.class));
    }

    public Product getProductToView() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProductToView");
        Response response = Client.process(query);
        Gson gson = new Gson();
        return new Product(gson.fromJson(response.getData(),SaveProduct.class));
    }

    public void setProductToEdit(Product productToEdit) {
        String productId = Integer.toString(productToEdit.getProductId());
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setProductToEdit");
        query.getMethodInputs().put("productToEdit",productId);
        Client.process(query);
    }

    public void setProductToView(Product productToView) {
        String productId = Integer.toString(productToView.getProductId());
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setProductToView");
        query.getMethodInputs().put("productToView",productId);
        Client.process(query);
    }

    public void seenProduct(int productId){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "seenProduct");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        Client.process(query);
    }

    public ProductField getBestSale(int productId) throws NoSaleForProduct {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getBestSale");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        Response response =Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoSaleForProduct"))
            throw new NoSaleForProduct();
        Gson gson = new Gson();
        return new ProductField(gson.fromJson(response.getData(),SaveProductField.class));
    }


    public static class NoSaleForProduct extends Exception{}

    public static class NoProductWithId extends Exception {
    }

    public static class NotInTheSameCategory extends Exception {
    }

    public static class NoSortException extends Exception {
    }

    public static class NoFilterWithNameException extends Exception {
    }

    public static class IntegerFieldException extends Exception {
    }

    public static class OptionalFieldException extends Exception {
    }

    public static class NoCategoryWithName extends Exception {
    }

    public static class NotEnoughSupply extends Exception {
    }

    public static class NoSellerWithUsername extends Exception {
    }

    public static class NoSellerIsChosen extends Exception {
    }

    public static class UserCantBuy extends Exception {

    }
}
