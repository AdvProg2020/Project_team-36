package Controllers;

import Models.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OffController implements ObjectController {
    GlobalVariables userVariables;
    protected HashMap<String, Method> sortMethods;
    protected HashMap<String, Method> optionalFilterMethods;
    protected HashMap<String, Method> integerFilterMethods;

    public OffController(GlobalVariables userVariables) {
        this.userVariables = userVariables;
        this.sortMethods = new HashMap<>();
        this.integerFilterMethods = new HashMap<>();
        this.optionalFilterMethods = new HashMap<>();
        setSortMethodsProducts();
        setFilterMethods();
    }

    private void setSortMethodsProducts() {
        try {
            Method method = Product.class.getDeclaredMethod("getProductionDate");
            this.sortMethods.put("production time", method);
            method = Product.class.getDeclaredMethod("getSeenNumber");
            this.sortMethods.put("seen count", method);
            method = Product.class.getDeclaredMethod("getName");
            this.sortMethods.put("name", method);
            method = Product.class.getDeclaredMethod("getScore");
            this.sortMethods.put("score", method);
            method = Product.class.getDeclaredMethod("getHighestCurrentPrice");
            this.sortMethods.put("Maximum current price", method);
            method = Product.class.getDeclaredMethod("getLowestCurrentPrice");
            this.sortMethods.put("Minimum current price", method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void setFilterMethods() {
        try {
            Method method = Product.class.getDeclaredMethod("getName");
            this.optionalFilterMethods.put("name", method);
            method = Product.class.getDeclaredMethod("getLowestCurrentPrice");
            this.integerFilterMethods.put("lowest price", method);
            method = Product.class.getDeclaredMethod("getHighestCurrentPrice");
            this.integerFilterMethods.put("highest price", method);
            method = Product.class.getDeclaredMethod("getCompany");
            this.optionalFilterMethods.put("company", method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getProductCurrentSortName() {
        return userVariables.getSortOff();
    }

    @Override
    public String getSortProductType() {
        return userVariables.getSortOffType();
    }

    @Override
    public void removeSortProduct() {
        userVariables.removeSortOff();
    }

    @Override
    public void setSort(String name, String type) throws ProductsController.NoSortException {
        for (String field : sortMethods.keySet()) {
            if (field.equalsIgnoreCase(name)) {
                userVariables.setSortOff(name, type);
                return;
            }
        }
        throw new ProductsController.NoSortException();

    }

    @Override
    public Set<String> getAvailableFilters() {
        Set<String> result = new HashSet<>();
        result.addAll(integerFilterMethods.keySet());
        result.add("category");
        result.addAll(optionalFilterMethods.keySet());
        if (userVariables.getFilterOffsCategory() != null) {
            for (Field field : userVariables.getFilterOffsCategory().getAllFields()) {
                result.add(field.getName());
            }
        }
        return result;
    }

    @Override
    public ArrayList<Filter> getCurrentFilters() {
        return userVariables.getAllFiltersOffs();
    }

    @Override
    public Category getCurrentCategoryFilter() {
        return userVariables.getFilterOffsCategory();
    }

    @Override
    public void setNewFilter(String name) throws ProductsController.IntegerFieldException, ProductsController.OptionalFieldException, ProductsController.NoFilterWithNameException {
        for (String type : integerFilterMethods.keySet()) {
            if (type.equalsIgnoreCase(name)) {
                userVariables.setPendingFilter(new RangeFilter(integerFilterMethods.get(type), type));
                throw new ProductsController.IntegerFieldException();
            }
        }
        for (String type : optionalFilterMethods.keySet()) {
            if (type.equalsIgnoreCase(name)) {
                userVariables.setPendingFilter(new OptionalFilter(integerFilterMethods.get(type), type));
                throw new ProductsController.OptionalFieldException();
            }
        }
        if (userVariables.getFilterOffsCategory() != null)
            checkCategoryFieldsFilter(name);
        else
            throw new ProductsController.NoFilterWithNameException();
    }

    private void checkCategoryFieldsFilter(String name) throws ProductsController.IntegerFieldException, ProductsController.OptionalFieldException, ProductsController.NoFilterWithNameException {
        for (Field field : userVariables.getFilterOffsCategory().getAllFields()) {
            if (field.getName().equalsIgnoreCase(name)) {
                if (field instanceof IntegerField) {
                    userVariables.setPendingFilter(new RangeFilter(field.getName()));
                    throw new ProductsController.IntegerFieldException();
                } else if (field instanceof OptionalField) {
                    userVariables.setPendingFilter(new OptionalFilter(field.getName()));
                    throw new ProductsController.OptionalFieldException();
                }
            }
        }
        throw new ProductsController.NoFilterWithNameException();
    }

    @Override
    public void setFilterRange(String min, String max) {
        userVariables.setOffFilterRange(min, max);
    }

    @Override
    public void setFilterOptions(ArrayList<String> options) {
        userVariables.setOffFilterOptions(options);
    }

    @Override
    public void removeFilter(String name) throws ProductsController.NoFilterWithNameException {
        if (name.equalsIgnoreCase("category")) {
            if (userVariables.getFilterOffsCategory() != null) {
                removeCategoryRelatedFilters();
                userVariables.setFilterOffsCategory(null);
            } else
                throw new ProductsController.NoFilterWithNameException();
        } else {
            for (Filter filter : userVariables.getAllFiltersOffs()) {
                if (filter.getName().equalsIgnoreCase(name)) {
                    userVariables.getAllFiltersOffs().remove(filter);
                    return;
                }

            }
            throw new ProductsController.NoFilterWithNameException();
        }

    }

    private void removeCategoryRelatedFilters() {
        HashSet<String> generalFilters = new HashSet<>();
        HashSet<Filter> temp = new HashSet<>();
        generalFilters.addAll(integerFilterMethods.keySet());
        generalFilters.addAll(optionalFilterMethods.keySet());
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (generalFilters.contains(filter.getName()))
                temp.add(filter);
        }
        userVariables.getAllFiltersProducts().removeAll(temp);
    }

    public HashSet<String> getCompanyNamesForFilter(){
        HashSet<String> names= new HashSet<>();
        for (Product product : Product.getAllProducts()) {
            names.add(product.getCompany());
        }
        return names;
    }

    public ArrayList<Product> getAllInSaleProducts(){
        ArrayList<Product> result = new ArrayList<>();
        if(userVariables.getFilterOffsCategory()!=null){
            getInSaleCategories(result,userVariables.getFilterOffsCategory());
        }else{
            result.addAll(Product.getAllInSaleProducts());
        }
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            result = filter.filter(result);
        }
        boolean isAscending = false;
        if(userVariables.getSortOffType().equalsIgnoreCase("ascending"))
            isAscending = true;
        for (String type : sortMethods.keySet()) {
            if(type.equalsIgnoreCase(userVariables.getSortOff()))
                new Sort().sort(result,sortMethods.get(type),isAscending);
        }
        return result;
    }
@Override
    public void setCategoryFilter(String name) throws ProductsController.NoCategoryWithName {

        for (Category category : Category.getAllCategories()) {
            if(category.getName().equalsIgnoreCase(name)){
                userVariables.setFilterOffsCategory(category);}
        }
        throw new ProductsController.NoCategoryWithName();
    }

    @Override
    public Set<String> getAvailableSorts() {
        return sortMethods.keySet();
    }

    private void getInSaleCategories(ArrayList<Product> products,Category category){
        products.addAll(category.getAllSubProducts());
        ArrayList<Product> temp = new ArrayList<>();
        for (Product product : products) {
            if(!product.isProductInSale()){
                temp.add(product);
            }
        }
        products.removeAll(temp);
    }



}
