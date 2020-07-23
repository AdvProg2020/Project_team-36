package Client.Models;

import Client.GUI.Constants;
import Models.Field;
import Models.Query;
import Models.Response;
import Client.Network.Client;
import Repository.SaveCategory;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Category {
    private SaveCategory saveCategory;
    private String name;
    private int categoryId;
    private Set<Field> allFields ;

    public Category(SaveCategory saveCategory) {
        this.saveCategory = saveCategory;
        this.name = saveCategory.getName();
        this.categoryId = saveCategory.getCategoryId();
        this.allFields = new HashSet<>();
        allFields.addAll(saveCategory.getAllOptionalFields());
        allFields.addAll(saveCategory.getAllIntegerFields());
    }

    public SaveCategory getSaveCategory() {
        return saveCategory;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Set<Field> getAllFields() {
        return allFields;
    }

    public Category getParentCategory() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Category");
        query.getMethodInputs().put("id", "" + saveCategory.getParentCategoryId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Category")) {
            Gson gson = new Gson();
            SaveCategory saveCategory = gson.fromJson(response.getData(), SaveCategory.class);
            return new Category(saveCategory);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public List<Category> getSubCategories() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Category");
        this.saveCategory.getSubCategoriesIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Category>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveCategory>>(){}.getType();
            List<SaveCategory> allSaveCategories = gson.fromJson(response.getData(),type);
            List<Category> allCategories = new ArrayList<>();
            allSaveCategories.forEach(saveCategory -> allCategories.add(new Category(saveCategory)));
            return allCategories;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public List<Client.Models.Product> getProducts() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Product");
        this.saveCategory.getProductsIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Product>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveProduct>>(){}.getType();
            List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(),type);
            List<Client.Models.Product> allProducts = new ArrayList<>();
            allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
            return allProducts;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public Set<Field> getCategoryOwnFields(){
        Set<Field> parentFields = this.getParentCategory().getAllFields();
        HashSet<Field> toBeReturned = new HashSet<>();
        HashSet<Field> toBeRemoved = new HashSet<>();
        for (Field field : allFields) {
            for (Field parentField : parentFields) {
                if(parentField.getName().equalsIgnoreCase(field.getName())) {
                    toBeRemoved.add(field);
                    break;
                }
            }
        }
        for (Field allField : allFields) {
            if(!toBeRemoved.contains(allField))
                toBeReturned.add(allField);
        }
        return toBeReturned;
    }

}
