package Controllers;

import Models.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProductsController implements ObjectController {
    private final GlobalVariables userVariables;
    protected HashMap<String, Method> sortMethods;
    protected HashMap<String, Method> optionalFilterMethods;
    protected HashMap<String, Method> integerFilterMethods;

    public ProductsController(GlobalVariables userVariables) {
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
            this.sortMethods.put("production date", method);
            method = Product.class.getDeclaredMethod("getSeenNumber");
            this.sortMethods.put("seen", method);
            method = Product.class.getDeclaredMethod("getName");
            this.sortMethods.put("name", method);
            method = Product.class.getDeclaredMethod("getScore");
            this.sortMethods.put("score", method);
            method = Product.class.getDeclaredMethod("getHighestCurrentPrice");
            this.sortMethods.put("maximum price of all", method);
            method = Product.class.getDeclaredMethod("getLowestCurrentPrice");
            this.sortMethods.put("minimum price of all", method);
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

    public Product getProduct(int id) throws NoProductWithId {
        for (Product product : Product.getAllProducts()) {
            if (product.getProductId() == (id)) {
                return product;
            }
        }
        throw new NoProductWithId();
    }

    public Category getMainCategory() {
        return Category.getMainCategory();
    }

    public void setSort(String name, String type) throws NoSortException {
        for (String field : sortMethods.keySet()) {
            if (field.equalsIgnoreCase(name)) {
                userVariables.setSortProduct(name, type);
                return;
            }
        }
        throw new NoSortException();
    }


    public Set<String> getAvailableSorts() {
        return sortMethods.keySet();
    }

    public Set<String> getAvailableFilters() {
        Set<String> result = new HashSet<>();
        result.addAll(optionalFilterMethods.keySet());
        result.add("category");
        result.addAll(integerFilterMethods.keySet());
        if (userVariables.getFilterProductsCategory() != null) {
            for (Field field : userVariables.getFilterProductsCategory().getAllFields()) {
                result.add(field.getName());
            }
        }
        return result;
    }

    public HashSet<String> getCompanyNamesForFilter() {
        HashSet<String> names = new HashSet<>();
        for (Product product : Product.getAllProducts()) {
            names.add(product.getCompany());
        }
        return names;
    }

    public String getProductCurrentSortName() {
        return userVariables.getSortProduct();
    }

    public ArrayList<Filter> getCurrentFilters() {
        return userVariables.getAllFiltersProducts();
    }

    public Category getCurrentCategoryFilter() {
        return userVariables.getFilterProductsCategory();
    }

    public String getSortProductType() {
        return userVariables.getSortProductType();
    }

    public void removeSortProduct() {
        userVariables.removeSortProduct();
    }

    public void removeFilter(String name) throws NoFilterWithNameException {
        if (name.equalsIgnoreCase("category")) {
            if (userVariables.getFilterProductsCategory() != null) {
                removeCategoryRelatedFilters();
                userVariables.setFilterProductsCategory(null);
            } else
                throw new NoFilterWithNameException();
        } else {
            for (Filter filter : userVariables.getAllFiltersProducts()) {
                if (filter.getName().equalsIgnoreCase(name)) {
                    userVariables.getAllFiltersProducts().remove(filter);
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
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (!generalFilters.contains(filter.getName()))
                temp.add(filter);
        }
        userVariables.getAllFiltersProducts().removeAll(temp);
    }

    public void setNewFilter(String name) throws IntegerFieldException, OptionalFieldException, NoFilterWithNameException {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equalsIgnoreCase(name)) {
                userVariables.getAllFiltersProducts().remove(filter);
                break;
            }
        }
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
        if (userVariables.getFilterProductsCategory() != null)
            checkCategoryFieldsFilter(name);
    }

    public void setCategoryFilter(String name) throws NoCategoryWithName {
        for (Category category : Category.getAllCategories()) {
            if (category.getName().equalsIgnoreCase(name)) {
                userVariables.setFilterProductsCategory(category);
                return;
            }
        }
        throw new NoCategoryWithName();
    }

    private void checkCategoryFieldsFilter(String name) throws IntegerFieldException, OptionalFieldException, NoFilterWithNameException {
        for (Field field : userVariables.getFilterProductsCategory().getAllFields()) {
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

    public void setFilterOptions(ArrayList<String> options) {
        userVariables.setProductFilterOptions(options);
    }

    public void setFilterRange(String min, String max) {
        userVariables.setProductFilterRange(min, max);
    }

    public ArrayList<Product> getFinalProductsList() {
        ArrayList<Product> result = new ArrayList<>();
        if (userVariables.getFilterProductsCategory() != null) {
            result.addAll(userVariables.getFilterProductsCategory().getAllSubProducts());
        } else {
            result.addAll(Product.getAllProducts());
        }
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            result = filter.filter(result);
        }
        boolean isAscending = false;
        if (userVariables.getSortProductType().equalsIgnoreCase("ascending"))
            isAscending = true;
        for (String type : sortMethods.keySet()) {
            if (type.equalsIgnoreCase(userVariables.getSortProduct()))
                new Sort().sort(result, sortMethods.get(type), isAscending);
        }
        return result;
    }

    public void setChosenProduct(int productId) throws NoProductWithId {
        if (!Product.isThereProductWithId(productId))
            throw new NoProductWithId();
        userVariables.setProduct(Product.getProduct(productId));
        Product.getProduct(productId).seen();
    }

    public Product getChosenProduct() {
        return userVariables.getProduct();
    }

    public void resetDigest() {
        userVariables.setProduct(null);
        userVariables.setPendingSellerOfProduct(null);
    }

    public void addSellerForBuy(String username) throws NotEnoughSupply, NoSellerWithUsername {
        for (ProductField field : userVariables.getProduct().getProductFields()) {
            if (field.getSeller().getUsername().equalsIgnoreCase(username) && !field.getSeller().getStatus().equals(Status.DELETED)) {
                if (field.getSupply() > 0)
                    userVariables.setPendingSellerOfProduct(field.getSeller());
                else {
                    userVariables.setPendingSellerOfProduct(null);
                    throw new NotEnoughSupply();
                }
            } else {
                userVariables.setPendingSellerOfProduct(null);
                throw new NoSellerWithUsername();
            }

        }
    }

    public void addToCart() throws NoSellerIsChosen, EntryController.NotLoggedInException, UserCantBuy {
        if (userVariables.getPendingSellerOfProduct() == null) {
            throw new NoSellerIsChosen();
        } else if (userVariables.getLoggedInUser() == null)
            throw new EntryController.NotLoggedInException();
        else if (!(userVariables.getLoggedInUser() instanceof Customer))
            throw new UserCantBuy();
        else
            ((Customer) userVariables.getLoggedInUser()).addToCart(new SelectedItem(userVariables.getProduct(), userVariables.getPendingSellerOfProduct()));
    }

    public Product compare(int productId) throws NoProductWithId, NotInTheSameCategory {
        if (Product.getProduct(productId) == null)
            throw new NoProductWithId();
        else if (!Product.getProduct(productId).getCategory().equals(userVariables.getProduct().getCategory()))
            throw new NotInTheSameCategory();
        else
            return Product.getProduct(productId);
    }

    public ArrayList<Comment> getProductComments() {
        return userVariables.getProduct().getAllComments();
    }

    public void addComment(String title, String content) throws EntryController.NotLoggedInException {
        if (userVariables.getLoggedInUser() == null) {
            throw new EntryController.NotLoggedInException();
        }
        if (!(userVariables.getLoggedInUser() instanceof Customer)) {
            new Request(new Comment(userVariables.getLoggedInUser(), userVariables.getProduct(), title, content, false), Status.TO_BE_ADDED);
        } else {
            boolean hasBought = userVariables.getProduct().isThereBuyer((Customer) userVariables.getLoggedInUser());
            new Request(new Comment(userVariables.getLoggedInUser(), userVariables.getProduct(), title, content, hasBought), Status.TO_BE_ADDED);

        }
    }

    public void setCompanyFilter(ArrayList<String> options) {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equals("company")) {
                userVariables.getAllFiltersProducts().remove(filter);
                break;
            }
        }
        if (options.isEmpty())
            return;
        OptionalFilter optionalFilter = new OptionalFilter(optionalFilterMethods.get("company"), "company");
        optionalFilter.setOptions(options);
        userVariables.addFilterProducts(optionalFilter);
    }

    public void addNameFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equals("name")) {
                ((OptionalFilter) filter).addOption(name);
                return;
            }
        }
        OptionalFilter optionalFilter = new OptionalFilter(optionalFilterMethods.get("name"), "name");
        optionalFilter.addOption(name);
        userVariables.addFilterProducts(optionalFilter);
    }

    public void removeNameFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equals("name")) {
                ((OptionalFilter) filter).removeOption(name);
                if (((OptionalFilter) filter).getOptions().isEmpty()) {
                    userVariables.getAllFiltersProducts().remove(filter);
                    return;
                }
                return;
            }
        }
    }

    public void availabilityFilter() {
        try {
            Filter filter = new BooleanFilter(Product.class.getDeclaredMethod("isProductAvailable"));
            userVariables.getAllFiltersProducts().add(filter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void removeAvailabilityFilter() {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter instanceof BooleanFilter && filter.getName().equals("available")) {
                userVariables.getAllFiltersProducts().remove(filter);
                return;
            }
        }
    }

    @Override
    public void addSellerFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equals("seller")) {
                ((BooleanFilter) filter).addOption(name);
                return;
            }
        }
        ArrayList<String> names = new ArrayList<>();
        names.add(name);
        BooleanFilter booleanFilter = new BooleanFilter(optionalFilterMethods.get("seller"), names);
        userVariables.addFilterProducts(booleanFilter);
    }

    @Override
    public void removeSellerFilter(String name) {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equals("seller")) {
                ((BooleanFilter) filter).removeOption(name);
                if (((BooleanFilter) filter).getOptions().isEmpty())
                    userVariables.getAllFiltersProducts().remove(filter);
                return;
            }
        }
    }

    public ArrayList<String> getCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : Category.getAllCategories()) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    @Override
    public ArrayList<String> getSpecialIntegerFilter() {
        ArrayList<String> toBeReturned = new ArrayList<>();
        for (Field field : userVariables.getFilterProductsCategory().getAllFields()) {
            if (field instanceof IntegerField)
                toBeReturned.add(field.getName());
        }
        return toBeReturned;
    }

    public HashMap<String, HashSet<String>> getAllOptionalChoices() {
        ArrayList<Product> allProducts = userVariables.getFilterProductsCategory().getAllSubProducts();
        HashMap<String, HashSet<String>> options = new HashMap<>();
        for (Field field : userVariables.getFilterProductsCategory().getAllFields()) {
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
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equalsIgnoreCase(filterName) && filter instanceof OptionalFilter) {
                ((OptionalFilter) filter).addOption(option);
                return;
            }
        }
        OptionalFilter optionalFilter = new OptionalFilter(filterName);
        optionalFilter.addOption(option);
        userVariables.addFilterProducts(optionalFilter);
    }

    public void removeOptionalFilter(String filterName, String option) {
        for (Filter filter : userVariables.getAllFiltersProducts()) {
            if (filter.getName().equalsIgnoreCase(filterName) && filter instanceof OptionalFilter) {
                ((OptionalFilter) filter).removeOption(option);
                if(((OptionalFilter)filter).getOptions().size()==0)
                    userVariables.getAllFiltersProducts().remove(filter);
                return;
            }
        }
    }

    public boolean canRate(Product product,User user){
        if(!(user instanceof Customer))
            return false;
        return product.isThereBuyer((Customer)user);
    }

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


