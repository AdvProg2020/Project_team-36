package Controllers;

import Models.*;
import Repository.SaveCategory;
import Repository.SaveComment;
import Repository.SaveProduct;
import Repository.SaveProductField;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class ProductsController implements ObjectController {
    private final GlobalVariables userVariables;
    private final HashMap<String, Method> sortMethods;
    private final HashMap<String, Method> optionalFilterMethods;
    private final HashMap<String, Method> integerFilterMethods;

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
            for (Product product : Product.getAllProducts()) {
                if (!product.isInAuction())
                    result.add(product);
            }
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

    public Product getChosenProduct() throws NoProductWithId {
        if (userVariables.getProduct().isProductDeleted())
            throw new NoProductWithId();
        return userVariables.getProduct();
    }

    public void resetDigest() {
        userVariables.setProduct(null);
        userVariables.setPendingSellerOfProduct(null);
    }

    public void addSellerForBuy(String username) throws NotEnoughSupply, NoSellerWithUsername {
        for (ProductField field : userVariables.getProduct().getProductFields()) {
            if (field.getSeller().getUsername().equalsIgnoreCase(username) && !field.getSeller().getStatus().equals(Status.DELETED)) {
                if (field.getSupply() > 0) {
                    userVariables.setPendingSellerOfProduct(field.getSeller());
                } else {
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
                if (options.containsKey(field.getName()) && field instanceof OptionalField)
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
                if (((OptionalFilter) filter).getOptions().size() == 0)
                    userVariables.getAllFiltersProducts().remove(filter);
                return;
            }
        }
    }

    public boolean canRate(Product product, User user) {
        if (!(user instanceof Customer))
            return false;
        return product.isThereBuyer((Customer) user);
    }

    public Product getProductToEdit() {
        return Product.getProductToEdit();
    }

    public Product getProductToView() {
        return Product.getProductToView();
    }

    public void setProductToEdit(Product productToEdit) {
        Product.setProductToEdit(productToEdit);
    }

    public void setProductToView(Product productToView) {
        Product.setProductToView(productToView);
    }

    public void seenProduct(int productId) {
        Product.getProduct(productId).seen();
    }

    public ProductField getBestSale(int productId) throws Product.NoSaleForProduct {
        return Product.getProduct(productId).getBestSale();
    }

    public void setProduct(int productId){
        userVariables.setProduct(Product.getProduct(productId));
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "getProduct" -> processGetProduct(query);
            case "getMainCategory" -> processGetMainCategory(query);
            case "setSort" -> processSetSort(query);
            case "getAvailableFilters" -> processGetAvailableFilters(query);
            case "getCompanyNamesForFilter" -> processGetCompanyNamesForFilter(query);
            case "removeSortProduct" -> processRemoveSortProduct(query);
            case "removeFilter" -> processRemoveFilter(query);
            case "setNewFilter" -> processSetNewFilter(query);
            case "setCategoryFilter" -> processSetCategoryFilter(query);
            case "setFilterOptions" -> processSetFilterOptions(query);
            case "setFilterRange" -> processSetFilterRange(query);
            case "getFinalProductsList" -> processGetFinalProductsList(query);
            case "setChosenProduct" -> processSetChosenProduct(query);
            case "getChosenProduct" -> processGetChosenProduct(query);
            case "resetDigest" -> processResetDigest(query);
            case "addSellerForBuy" -> processAddSellerForBuy(query);
            case "addToCart" -> processAddToCart(query);
            case "compare" -> processCompare(query);
            case "getProductComments" -> processGetProductComments(query);
            case "addComment" -> processAddComment(query);
            case "setCompanyFilter" -> processSetCompanyFilter(query);
            case "addNameFilter" -> processAddNameFilter(query);
            case "removeNameFilter" -> processRemoveNameFilter(query);
            case "availabilityFilter" -> processAvailabilityFilter(query);
            case "removeAvailabilityFilter" -> processRemoveAvailabilityFilter(query);
            case "addSellerFilter" -> processAddSellerFilter(query);
            case "removeSellerFilter" -> processRemoveSellerFilter(query);
            case "getCategoryNames" -> processGetCategoryNames(query);
            case "getSpecialIntegerFilter" -> processGetSpecialIntegerFilter(query);
            case "getAllOptionalChoices" -> processGetAllOptionalChoices(query);
            case "addOptionalFilter" -> processAddOptionalFilter(query);
            case "removeOptionalFilter" -> processRemoveOptionalFilter(query);
            case "canRate" -> processCanRate(query);
            case "getProductToEdit" -> processGetProductToEdit();
            case "getProductToView" -> processGetProductToView();
            case "setProductToEdit" -> processSetProductToEdit(query);
            case "setProductToView" -> processSetProductToView(query);
            case "seenProduct" -> processSeenProduct(query);
            case "getBestSale" -> processGetBestSale(query);
            case "setProduct" -> processSetProduct(query);
            default -> new Response("Error", "");
        };
    }

    private Response processSetProduct(Query query) {
        setProduct(Integer.parseInt(query.getMethodInputs().get("productId")));
        return new Response("void","");
    }

    private Response processGetBestSale(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            ProductField productField = getBestSale(productId);
            SaveProductField saveProductField = new SaveProductField(productField);
            Gson gson = new GsonBuilder().create();
            return new Response("ProductField", gson.toJson(saveProductField));
        } catch (Product.NoSaleForProduct noSaleForProduct) {
            return new Response("NoSaleForProduct", "");
        }
    }

    private Response processSeenProduct(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        seenProduct(productId);
        return new Response("void", "");
    }


    private Response processCanRate(Query query) {
        String userId = query.getMethodInputs().get("user");
        String productId = query.getMethodInputs().get("product");
        boolean result = canRate(Product.getProduct(Integer.parseInt(productId)), User.getUserById(Integer.parseInt(userId)));
        Gson gson = new GsonBuilder().create();
        return new Response("boolean", gson.toJson(result));
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

    private Response processGetSpecialIntegerFilter(Query query) {
        List<String> toBeRetuned = new ArrayList<>(getSpecialIntegerFilter());
        Gson gson = new GsonBuilder().create();
        String stringGson = gson.toJson(toBeRetuned);
        return new Response("List<String>", stringGson);
    }

    private Response processGetCategoryNames(Query query) {
        Gson gson = new GsonBuilder().create();
        List<String> names = new ArrayList<>(getCategoryNames());
        String categoryNames = gson.toJson(names);
        return new Response("List<String>", categoryNames);
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
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> options = gson.fromJson(query.getMethodInputs().get("options"), type);
        ArrayList<String> allOptions = new ArrayList<>(options);
        setCompanyFilter(allOptions);
        return new Response("void", "");
    }

    private Response processAddComment(Query query) {
        String title = query.getMethodInputs().get("title");
        String content = query.getMethodInputs().get("content");
        try {
            addComment(title, content);
            return new Response("void", "");
        } catch (EntryController.NotLoggedInException e) {
            return new Response("NotLoggedInException", "");
        }
    }

    private Response processGetProductComments(Query query) {
        List<SaveComment> comments = new ArrayList<>();
        getProductComments().forEach(c -> comments.add(new SaveComment(c)));
        Gson gson = new GsonBuilder().create();
        String saveComments = gson.toJson(comments);
        return new Response("List<Comment>", saveComments);
    }

    private Response processCompare(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            Product product = compare(productId);
            SaveProduct savedProduct = new SaveProduct(product);
            Gson gson = new GsonBuilder().create();
            String toBeReturned = gson.toJson(savedProduct);
            return new Response("Product", toBeReturned);
        } catch (NoProductWithId noProductWithId) {
            return new Response("NoProductWithId", "");
        } catch (NotInTheSameCategory notInTheSameCategory) {
            return new Response("NotInTheSameCategory", "");
        }
    }

    private Response processAddToCart(Query query) {
        try {
            addToCart();
            return new Response("void", "");
        } catch (NoSellerIsChosen noSellerIsChosen) {
            return new Response("NoSellerIsChosen", "");
        } catch (EntryController.NotLoggedInException e) {
            return new Response("NotLoggedInException", "");
        } catch (UserCantBuy userCantBuy) {
            return new Response("UserCantBuy", "");
        }
    }

    private Response processAddSellerForBuy(Query query) {
        String username = query.getMethodInputs().get("username");
        try {
            addSellerForBuy(username);
            return new Response("void", "");
        } catch (NotEnoughSupply notEnoughSupply) {
            return new Response("NotEnoughSupply", "");
        } catch (NoSellerWithUsername noSellerWithUsername) {
            return new Response("NoSellerWithUsername", "");
        }
    }

    private Response processResetDigest(Query query) {
        resetDigest();
        return new Response("void", "");
    }

    private Response processGetChosenProduct(Query query) {
        SaveProduct product = null;
        try {
            product = new SaveProduct(getChosenProduct());
        } catch (NoProductWithId noProductWithId) {
            return new Response("NoProductWithId", "");
        }
        Gson gson = new GsonBuilder().create();
        String saveProduct = gson.toJson(product);
        return new Response("Product", saveProduct);
    }

    private Response processSetChosenProduct(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            setChosenProduct(productId);
            return new Response("void", "");
        } catch (NoProductWithId noProductWithId) {
            return new Response("NoProductWithId", "");
        }
    }

    private Response processGetFinalProductsList(Query query) {
        List<SaveProduct> allSavedProducts = new ArrayList<>();
        getFinalProductsList().forEach(c -> allSavedProducts.add(new SaveProduct(c)));
        Gson gson = new GsonBuilder().create();
        String stringGson = gson.toJson(allSavedProducts);
        return new Response("List<Product>", stringGson);
    }

    private Response processSetFilterRange(Query query) {
        String min = query.getMethodInputs().get("min");
        String max = query.getMethodInputs().get("max");
        setFilterRange(min, max);
        return new Response("void", "");
    }

    private Response processSetFilterOptions(Query query) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> options = gson.fromJson(query.getMethodInputs().get("options"), type);
        ArrayList<String> allOptions = new ArrayList<>(options);
        setFilterOptions(allOptions);
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

    private Response processRemoveFilter(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            removeFilter(name);
            return new Response("void", "");
        } catch (ProductsController.NoFilterWithNameException e) {
            return new Response("NoFilterWithNameException", "");
        }
    }

    private Response processRemoveSortProduct(Query query) {
        removeSortProduct();
        return new Response("void", "");
    }

    private Response processGetCompanyNamesForFilter(Query query) {
        Set<String> toBeRetuned = new HashSet<>(getCompanyNamesForFilter());
        Gson gson = new GsonBuilder().create();
        String stringGson = gson.toJson(toBeRetuned);
        return new Response("Set<String>", stringGson);
    }

    private Response processGetAvailableFilters(Query query) {
        Set<String> toBeReturned = new HashSet<>(getAvailableFilters());
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

    private Response processGetMainCategory(Query query) {
        SaveCategory category = new SaveCategory(getMainCategory());
        Gson gson = new GsonBuilder().create();
        return new Response("Category", gson.toJson(category));
    }

    private Response processGetProduct(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            Product product = getProduct(id);
            SaveProduct saveProduct = new SaveProduct(product);
            Gson gson = new GsonBuilder().create();
            return new Response("Product", gson.toJson(saveProduct));
        } catch (NoProductWithId noProductWithId) {
            return new Response("NoProductWithId", "");
        }
    }

    private Response processGetProductToEdit() {
        SaveProduct saveProduct = new SaveProduct(getProductToEdit());
        Gson gson = new GsonBuilder().create();
        return new Response("Product", gson.toJson(saveProduct));
    }

    private Response processGetProductToView() {
        SaveProduct saveProduct = new SaveProduct(getProductToView());
        Gson gson = new GsonBuilder().create();
        return new Response("Product", gson.toJson(saveProduct));
    }

    private Response processSetProductToEdit(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("productToEdit"));
        Product productToEdit = Product.getProductById(id);
        setProductToEdit(productToEdit);
        return new Response("void", "");
    }

    private Response processSetProductToView(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("productToView"));
        Product productToView = Product.getProductById(id);
        setProductToView(productToView);
        return new Response("void", "");
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


