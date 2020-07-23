package Controllers;

import GUI.Constants;
import Models.*;
import Network.Client;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class OffController implements ObjectController {
    private final String controllerName = "OffController";


    @Override
    public void removeSortProduct() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeSortProduct");
        Client.process(query);
    }

    @Override
    public void setSort(String name, String type) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setSort");
        query.getMethodInputs().put("name",name);
        query.getMethodInputs().put("type",type);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoSortException"))
            throw new ProductsController.NoSortException();
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

    @Override
    public void setNewFilter(String name) throws ProductsController.IntegerFieldException, ProductsController.OptionalFieldException, ProductsController.NoFilterWithNameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setNewFilter");
        query.getMethodInputs().put("name",name);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("IntegerFieldException"))
            throw new ProductsController.IntegerFieldException();
        else if(response.getReturnType().equalsIgnoreCase("OptionalFieldException"))
            throw new ProductsController.OptionalFieldException();
        else if(response.getReturnType().equalsIgnoreCase("NoFilterWithNameException"))
            throw new ProductsController.NoFilterWithNameException();
    }

    @Override
    public void setFilterRange(String min, String max) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFilterRange");
        query.getMethodInputs().put("min",min);
        query.getMethodInputs().put("max",max);
        Client.process(query);
    }

    @Override
    public void setFilterOptions(ArrayList<String> options) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFilterOptions");
        //TODO check kon ino nazanin
        List<String> temp = new ArrayList<>(options);
        Gson gson =  new GsonBuilder().create();
        String optionz = gson.toJson(temp);
        query.getMethodInputs().put("options",optionz);
        Client.process(query);
    }

    @Override
    public void removeFilter(String name) throws ProductsController.NoFilterWithNameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeFilter");
        query.getMethodInputs().put("name",name);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoFilterWithNameException")){
            throw new ProductsController.NoFilterWithNameException();
        }
    }
    @Override
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

    @Override
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

    @Override
    public void setCategoryFilter(String name) throws ProductsController.NoCategoryWithName {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCategoryFilter");
        query.getMethodInputs().put("name",name);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoCategoryWithName"))
            throw new ProductsController.NoCategoryWithName();
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

    public HashMap<String, HashSet<String>> getAllOptionalChoices() {
       //TODO nazanin inam mese producte chjuri load konam?
        return null;
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







}
