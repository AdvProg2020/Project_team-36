package Controllers;

import Models.*;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

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
            for (Product product : Product.getAllProducts()) {
                if(!product.isInAuction())
                    result.add(product);
            }
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
            if (filter.getName().equals("name") && filter instanceof OptionalFilter) {
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

    public HashMap<String, HashSet<String>> getAllOptionalChoices() {
        ArrayList<Product> allProducts = userVariables.getFilterOffsCategory().getAllSubProducts();
        HashMap<String, HashSet<String>> options = new HashMap<>();
        for (Field field : userVariables.getFilterOffsCategory().getAllFields()) {
            if (field instanceof OptionalField)
                options.put(field.getName(), new HashSet<>());
        }
        for (Product product : allProducts) {
            for (Field field : product.getFieldsOfCategory()) {
                if (options.keySet().contains(field.getName()) && field instanceof OptionalField)
                    options.get(field.getName()).add(((OptionalField) field).getQuality());
            }
        }
        return options;
    }

    public void addOptionalFilter(String filterName, String option) {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter.getName().equalsIgnoreCase(filterName) && filter instanceof OptionalFilter) {
                ((OptionalFilter) filter).addOption(option);
                return;
            }
        }
        OptionalFilter optionalFilter = new OptionalFilter(filterName);
        optionalFilter.addOption(option);
        userVariables.addFilterOffs(optionalFilter);
    }

    public void removeOptionalFilter(String filterName, String option) {
        for (Filter filter : userVariables.getAllFiltersOffs()) {
            if (filter.getName().equalsIgnoreCase(filterName) && filter instanceof OptionalFilter) {
                ((OptionalFilter) filter).removeOption(option);
                if (((OptionalFilter) filter).getOptions().size() == 0)
                    userVariables.getAllFiltersOffs().remove(filter);
                return;
            }
        }
    }

    @Override
    public ArrayList<String> getCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "removeSortProduct" -> processRemoveSortProduct(query);
            case "setSort" -> processSetSort(query);
            case "getAvailableFilters" -> processGetAvailableFilters(query);
            case "setNewFilter" -> processSetNewFilter(query);
            case "setFilterRange" -> processSetFilterRange(query);
            case "setFilterOptions" -> processSetFilterOptions(query);
            case "removeFilter" -> processRemoveFilter(query);
            case "getCompanyNamesForFilter" -> processGetCompanyNamesForFilter(query);
            case "getSpecialIntegerFilter" -> processGetSpecialIntegerFilter(query);
            case "getFinalProductsList" -> processGetFinalProductsList(query);
            case "setCategoryFilter" -> processSetCategoryFilter(query);
            case "setCompanyFilter" -> processSetCompanyFilter(query);
            case "addNameFilter" -> processAddNameFilter(query);
            case "removeNameFilter" -> processRemoveNameFilter(query);
            case "availabilityFilter" -> processAvailabilityFilter(query);
            case "removeAvailabilityFilter" -> processRemoveAvailabilityFilter(query);
            case "addSellerFilter" -> processAddSellerFilter(query);
            case "removeSellerFilter" -> processRemoveSellerFilter(query);
            case "getAllOptionalChoices" -> processGetAllOptionalChoices(query);
            case "addOptionalFilter" -> processAddOptionalFilter(query);
            case "removeOptionalFilter" -> processRemoveOptionalFilter(query);
            case "getCategoryNames" -> processGetCategoryNames(query);
            default -> new Response("Error", "");
        };
    }

    private Response processGetCategoryNames(Query query) {
        Gson gson = new GsonBuilder().create();
        List<String> names = new ArrayList<>();
        names.addAll(getCategoryNames());
        String categoryNames = gson.toJson(names);
        return new Response("List<String>", categoryNames);
    }

    private Response processRemoveOptionalFilter(Query query) {
        String filterName = query.getMethodInputs().get("filterName");
        String option = query.getMethodInputs().get("option");
        removeOptionalFilter(filterName, option);
        return new Response("void", "");
    }

    private Response processAddOptionalFilter(Query query) {
        String filterName = query.getMethodInputs().get("filterName");
        String option = query.getMethodInputs().get("option");
        addOptionalFilter(filterName, option);
        return new Response("void", "");
    }

    private Response processGetAllOptionalChoices(Query query) {
        Gson gson = new GsonBuilder().create();
        Map<String, Set<String>> toBeReturned = new HashMap<>();
        for (String key : getAllOptionalChoices().keySet()) {
            Set<String> value = new HashSet<>();
            for (String s : getAllOptionalChoices().get(key)) {
                value.add(s);
            }
            toBeReturned.put(key, value);
        }
        String choices = gson.toJson(toBeReturned);
        return new Response("Map<String,Set<String>>", choices);
    }

    private Response processRemoveSellerFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        removeSellerFilter(name);
        return new Response("void", "");
    }

    private Response processAddSellerFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        addSellerFilter(name);
        return new Response("void", "");
    }

    private Response processRemoveAvailabilityFilter(Query query) {
        removeAvailabilityFilter();
        return new Response("void", "");
    }

    private Response processAvailabilityFilter(Query query) {
        availabilityFilter();
        return new Response("void", "");
    }

    private Response processRemoveNameFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        removeNameFilter(name);
        return new Response("void", "");
    }

    private Response processAddNameFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        addNameFilter(name);
        return new Response("void", "");
    }

    private Response processSetCompanyFilter(Query query) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> options = gson.fromJson(query.getMethodInputs().get("options"), type);
        ArrayList<String> allOptions = new ArrayList<>(options);
        setCompanyFilter(allOptions);
        return new Response("void", "");
    }

    private Response processSetCategoryFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            setCategoryFilter(name);
            return new Response("void", "");
        } catch (ProductsController.NoCategoryWithName noCategoryWithName) {
            return new Response("NoCategoryWithName", "");
        }
    }

    private Response processGetFinalProductsList(Query query) {
        List<SaveProduct> allSavedProducts = new ArrayList<>();
        getFinalProductsList().forEach(c -> allSavedProducts.add(new SaveProduct(c)));
        Gson gson = new GsonBuilder().create();
        String stringGson = gson.toJson(allSavedProducts);
        return new Response("List<Product>", stringGson);
    }

    private Response processGetSpecialIntegerFilter(Query query) {
        List<String> toBeRetuned = new ArrayList<>();
        toBeRetuned.addAll(getSpecialIntegerFilter());
        Gson gson = new GsonBuilder().create();
        String stringGson = gson.toJson(toBeRetuned);
        return new Response("List<String>", stringGson);
    }

    private Response processGetCompanyNamesForFilter(Query query) {
        Set<String> toBeRetuned = new HashSet<>();
        toBeRetuned.addAll(getCompanyNamesForFilter());
        Gson gson = new GsonBuilder().create();
        String stringGson = gson.toJson(toBeRetuned);
        return new Response("Set<String>", stringGson);
    }

    private Response processRemoveFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            removeFilter(name);
            return new Response("void", "");
        } catch (ProductsController.NoFilterWithNameException e) {
            return new Response("NoFilterWithNameException", "");
        }

    }

    private Response processSetFilterOptions(Query query) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> options = gson.fromJson(query.getMethodInputs().get("options"), type);
        ArrayList<String> allOptions = new ArrayList<>(options);
        setFilterOptions(allOptions);
        return new Response("void", "");
    }

    private Response processSetFilterRange(Query query) {
        String min = query.getMethodInputs().get("min");
        String max = query.getMethodInputs().get("max");
        setFilterRange(min, max);
        return new Response("void", "");
    }

    private Response processSetNewFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            setNewFilter(name);
            return new Response("void", "");
        } catch (ProductsController.IntegerFieldException e) {
            return new Response("IntegerFieldException", "");
        } catch (ProductsController.OptionalFieldException e) {
            return new Response("OptionalFieldException", "");
        } catch (ProductsController.NoFilterWithNameException e) {
            return new Response("NoFilterWithNameException", "");
        }

    }

    private Response processGetAvailableFilters(Query query) {
        Set<String> toBeReturned = new HashSet<>();
        toBeReturned.addAll(getAvailableFilters());
        Gson gson = new GsonBuilder().create();
        String stringGson = gson.toJson(toBeReturned);
        return new Response("Set<String>", stringGson);

    }

    private Response processSetSort(Query query) {
        String name = query.getMethodInputs().get("name");
        String type = query.getMethodInputs().get("type");
        try {
            setSort(name, type);
            return new Response("void", "");
        } catch (ProductsController.NoSortException e) {
            return new Response("NoSortException", "");
        }
    }

    private Response processRemoveSortProduct(Query query) {
        removeSortProduct();
        return new Response("void", "");
    }


}
