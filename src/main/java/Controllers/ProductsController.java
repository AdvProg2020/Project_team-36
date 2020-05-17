package Controllers;

import Models.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProductsController {
    private GlobalVariables userVariables;
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

    private void sort(ArrayList<Product> toBeSorted, String type, Method method) {
        if (type.equalsIgnoreCase("ascending"))
            new Sort().sort(toBeSorted, method, true);
        else
            new Sort().sort(toBeSorted, method, false);
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
            if (generalFilters.contains(filter.getName()))
                temp.add(filter);
        }
        userVariables.getAllFiltersProducts().removeAll(temp);
    }

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
        if (userVariables.getFilterProductsCategory() != null)
            checkCategoryFieldsFilter(name);
        else
            throw new NoFilterWithNameException();
    }

    public void setCategoryFilter(String name) throws NoCategoryWithName {

        for (Category category : Category.getAllCategories()) {
            if (category.getName().equalsIgnoreCase(name)) {
                userVariables.setFilterProductsCategory(category);
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

    public ArrayList<Product> geFinalProductsList() {
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
        if(!(userVariables.getLoggedInUser() instanceof Customer)){
            new Request(new Comment(userVariables.getLoggedInUser(),userVariables.getProduct(),title,content,false));}
        else{
            Boolean hasBought = userVariables.getProduct().isThereBuyer((Customer)userVariables.getLoggedInUser());
            new Request(new Comment(userVariables.getLoggedInUser(),userVariables.getProduct(),title,content,hasBought));

        }
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


