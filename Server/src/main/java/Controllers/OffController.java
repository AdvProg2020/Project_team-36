package Controllers;

import Models.*;

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
            method = Product.class.getDeclaredMethod("isThereSeller", String.class);
            this.optionalFilterMethods.put("seller", method);
            method = Product.class.getDeclaredMethod("getLowestPrice");
            this.integerFilterMethods.put("price", method);
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
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter.getName().equalsIgnoreCase(name)) {
                userVariables.getAllFiltersProducts().remove(filter);
                break;
            }
        }
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
            if (!generalFilters.contains(filter.getName()))
                temp.add(filter);
        }
        userVariables.getAllFiltersProducts().removeAll(temp);
    }

    public HashSet<String> getCompanyNamesForFilter() {
        HashSet<String> names = new HashSet<>();
        for (Product product : Product.getAllProducts()) {
            names.add(product.getCompany());
        }
        return names;
    }

    @Override
    public ArrayList<String> getSpecialIntegerFilter() {
        ArrayList<String> toBeReturned = new ArrayList<>();
        for (Field field : userVariables.getFilterOffsCategory().getAllFields()) {
            if (field instanceof IntegerField)
                toBeReturned.add(field.getName());
        }
        return toBeReturned;
    }

    @Override
    public ArrayList<Product> getFinalProductsList() {
        ArrayList<Product> result = new ArrayList<>();
        if (userVariables.getFilterOffsCategory() != null) {
            getInSaleCategories(result, userVariables.getFilterOffsCategory());
        } else {
            result.addAll(Product.getAllInSaleProducts());
        }
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            result = filter.filter(result);
        }
        boolean isAscending = false;
        if (userVariables.getSortOffType().equalsIgnoreCase("ascending"))
            isAscending = true;
        for (String type : sortMethods.keySet()) {
            if (type.equalsIgnoreCase(userVariables.getSortOff()))
                new Sort().sort(result, sortMethods.get(type), isAscending);
        }
        return result;
    }

    @Override
    public void setCategoryFilter(String name) throws ProductsController.NoCategoryWithName {

        for (Category category : Category.getAllCategories()) {
            if (category.getName().equalsIgnoreCase(name)) {
                userVariables.setFilterOffsCategory(category);
                return;
            }
        }
        throw new ProductsController.NoCategoryWithName();
    }

    @Override
    public Set<String> getAvailableSorts() {
        return sortMethods.keySet();
    }

    private void getInSaleCategories(ArrayList<Product> products, Category category) {
        products.addAll(category.getAllSubProducts());
        ArrayList<Product> temp = new ArrayList<>();
        for (Product product : products) {
            if (!product.isProductInSale()) {
                temp.add(product);
            }
        }
        products.removeAll(temp);
    }

    public void setCompanyFilter(ArrayList<String> options) {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter.getName().equals("company")) {
                userVariables.getAllFiltersOffs().remove(filter);
                break;
            }
        }
        if (options.isEmpty())
            return;
        OptionalFilter optionalFilter = new OptionalFilter(optionalFilterMethods.get("company"), "company");
        optionalFilter.setOptions(options);
        userVariables.addFilterOffs(optionalFilter);
    }

    public void addNameFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter.getName().equals("name")) {
                ((OptionalFilter) filter).addOption(name);
                return;
            }
        }
        OptionalFilter optionalFilter = new OptionalFilter(optionalFilterMethods.get("name"), "name");
        optionalFilter.addOption(name);
        userVariables.addFilterOffs(optionalFilter);
    }

    public void removeNameFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter.getName().equals("name")&& filter instanceof OptionalFilter) {
                ((OptionalFilter) filter).removeOption(name);
                if (((OptionalFilter) filter).getOptions().isEmpty()) {
                    userVariables.getAllFiltersOffs().remove(filter);
                }
                return;
            }
        }
    }

    public void availabilityFilter() {
        try {
            Filter filter = new BooleanFilter(Product.class.getDeclaredMethod("isProductAvailable"));
            userVariables.getAllFiltersOffs().add(filter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void removeAvailabilityFilter() {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter instanceof BooleanFilter && filter.getName().equalsIgnoreCase("available")) {
                userVariables.getAllFiltersOffs().remove(filter);
                return;
            }
        }
    }

    @Override
    public void addSellerFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter.getName().equals("seller")) {
                ((BooleanFilter) filter).addOption(name);
                return;
            }
        }
        ArrayList<String> names = new ArrayList<>();
        names.add(name);
        BooleanFilter booleanFilter = new BooleanFilter(optionalFilterMethods.get("seller"), names);
        userVariables.addFilterOffs(booleanFilter);
    }

    @Override
    public void removeSellerFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter instanceof BooleanFilter && filter.getName().equals("seller")) {
                ((BooleanFilter) filter).removeOption(name);
                if (((BooleanFilter) filter).getOptions().isEmpty())
                    userVariables.getAllFiltersOffs().remove(filter);
                return;
            }
        }
    }

    public HashMap<String,HashSet<String>> getAllOptionalChoices(){
        ArrayList<Product> allProducts = userVariables.getFilterOffsCategory().getAllSubProducts();
        HashMap<String,HashSet<String>> options = new HashMap<>();
        for (Field field : userVariables.getFilterOffsCategory().getAllFields()) {
            if(field instanceof OptionalField)
                options.put(field.getName(), new HashSet<>());
        }
        for (Product product : allProducts) {
            for (Field field : product.getFieldsOfCategory()) {
                if(options.keySet().contains(field.getName())&& field instanceof OptionalField)
                    options.get(field.getName()).add(((OptionalField) field).getQuality());
            }
        }
        return options;
    }

    public void addOptionalFilter(String filterName,String option){
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if(filter.getName().equalsIgnoreCase(filterName)&&filter instanceof OptionalFilter){
                ((OptionalFilter) filter).addOption(option);
                return;
            }
        }
        OptionalFilter optionalFilter = new OptionalFilter(filterName);
        optionalFilter.addOption(option);
        userVariables.addFilterOffs(optionalFilter);
    }

    public void removeOptionalFilter(String filterName,String option){
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if(filter.getName().equalsIgnoreCase(filterName)&&filter instanceof OptionalFilter){
                ((OptionalFilter) filter).removeOption(option);
                if(((OptionalFilter)filter).getOptions().size()==0)
                    userVariables.getAllFiltersOffs().remove(filter);
                return;
            }
        }
    }

    @Override
    public ArrayList<String> getCategoryNames(){
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }


}
