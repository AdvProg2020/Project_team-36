package Controllers;

import Models.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OffController extends ProductsController {
    GlobalVariables userVariables;

    public OffController(GlobalVariables userVariables) {
        super(userVariables);
        this.userVariables = userVariables;
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
    public void setSort(String name, String type) throws NoSortException {
        for (String field : sortMethods.keySet()) {
            if (field.equalsIgnoreCase(name)) {
                userVariables.setSortOff(name, type);
                return;
            }
        }
        throw new NoSortException();

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
    public void setNewFilter(String name) throws IntegerFieldException, OptionalFieldException, NoFilterWithNameException {
        for (String type : integerFilterMethods.keySet()) {
            if (type.equalsIgnoreCase(name)) {
                userVariables.setPendingFilter(new RangeFilter(integerFilterMethods.get(type), type));
                throw new IntegerFieldException();
            }
        }
        for (String type : optionalFilterMethods.keySet()) {
            if (type.equalsIgnoreCase(name)) {
                userVariables.setPendingFilter(new OptionalFilter(integerFilterMethods.get(type), type));
                throw new OptionalFieldException();
            }
        }
        if (userVariables.getFilterOffsCategory() != null)
            checkCategoryFieldsFilter(name);
        else
            throw new NoFilterWithNameException();
    }

    private void checkCategoryFieldsFilter(String name) throws IntegerFieldException, OptionalFieldException, NoFilterWithNameException {
        for (Field field : userVariables.getFilterOffsCategory().getAllFields()) {
            if (field.getName().equalsIgnoreCase(name)) {
                if (field instanceof IntegerField) {
                    userVariables.setPendingFilter(new RangeFilter(field.getName()));
                    throw new IntegerFieldException();
                } else if (field instanceof OptionalField) {
                    userVariables.setPendingFilter(new OptionalFilter(field.getName()));
                    throw new OptionalFieldException();
                }
            }
        }
        throw new NoFilterWithNameException();
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
    public void removeFilter(String name) throws NoFilterWithNameException {
        if (name.equalsIgnoreCase("category")) {
            if (userVariables.getFilterOffsCategory() != null) {
                removeCategoryRelatedFilters();
                userVariables.setFilterOffsCategory(null);
            } else
                throw new NoFilterWithNameException();
        } else {
            for (Filter filter : userVariables.getAllFiltersOffs()) {
                if (filter.getName().equalsIgnoreCase(name)) {
                    userVariables.getAllFiltersOffs().remove(filter);
                    return;
                }

            }
            throw new NoFilterWithNameException();
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
    public void setCategoryFilter(String name) throws NoCategoryWithName {

        for (Category category : Category.getAllCategories()) {
            if(category.getName().equalsIgnoreCase(name)){
                userVariables.setFilterOffsCategory(category);}
        }
        throw new NoCategoryWithName();
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
