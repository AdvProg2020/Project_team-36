package Client.Controllers;

import Client.GUI.Constants;
import Client.Models.Category;
import Models.*;
import Client.Network.Client;
import Repository.SaveCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private final String controllerName = "CategoryController";

    public Client.Models.Category getMainCategory() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getMainCategory");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Category")) {
            Gson gson = new Gson();
            SaveCategory saveCategory = gson.fromJson(response.getData(), SaveCategory.class);
            return new Client.Models.Category(saveCategory);
        } else {
            return null;
        }
    }

    public void setCategoriesName(String name) throws InvalidCategoryName {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCategoriesName");
        query.getMethodInputs().put("name", name);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidCategoryName")) {
            throw new InvalidCategoryName();
        }
    }

    public ArrayList<Client.Models.Category> getAllCategories() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllCategories");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Client.Models.Category> allCategories = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveCategory>>() {
        }.getType();
        List<SaveCategory> allSaveCategories = gson.fromJson(response.getData(), type);
        allSaveCategories.forEach(saveCategory -> allCategories.add(new Client.Models.Category(saveCategory)));
        return allCategories;
    }

    public void setParentCategory(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setParentCategory");
        query.getMethodInputs().put("name", "" + name);
        Client.process(query);
    }

    public void setIntegerField(String name) throws ThereIsFieldWithNameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setIntegerField");
        query.getMethodInputs().put("name", name);
        Response response = Client.process(query);
        if (response.getReturnType().equals("ThereIsFieldWithNameException")) {
            throw new ThereIsFieldWithNameException();
        }
    }

    public void setOptionalField(String name) throws ThereIsFieldWithNameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setOptionalField");
        query.getMethodInputs().put("name", name);
        Response response = Client.process(query);
        if (response.getReturnType().equals("ThereIsFieldWithNameException")) {
            throw new ThereIsFieldWithNameException();
        }
    }

    public void acceptCategory() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "acceptCategory");
        Client.process(query);
    }

    public void editCategory(String name) throws InvalidCategoryName {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editCategory");
        query.getMethodInputs().put("name", name);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidCategoryName")) {
            throw new InvalidCategoryName();
        }
    }

    public void editName(String name) throws InvalidCategoryName {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editName");
        query.getMethodInputs().put("name", name);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidCategoryName")) {
            throw new InvalidCategoryName();
        }
    }

    public Client.Models.Category getPendableCategory() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getPendableCategory");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Category")) {
            Gson gson = new Gson();
            SaveCategory saveCategory = gson.fromJson(response.getData(), SaveCategory.class);
            return new Client.Models.Category(saveCategory);
        } else {
            return null;
        }
    }

    public void editField(String name) throws NoFieldWithNameException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editField");
        query.getMethodInputs().put("name", name);
        Response response = Client.process(query);
        if (response.getReturnType().equals("NoFieldWithNameException")) {
            throw new NoFieldWithNameException();
        }
    }

    public void renameField(String newName) throws ThereIsFieldWithNameException, ThereIsFieldWithNameInSubCategory {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "renameField");
        query.getMethodInputs().put("newName", newName);
        Response response = Client.process(query);
        if (response.getReturnType().equals("ThereIsFieldWithNameException")) {
            throw new ThereIsFieldWithNameException();
        } else if (response.getReturnType().equals("ThereIsFieldWithNameInSubCategory")) {
            Gson gson = new Gson();
            throw gson.fromJson(response.getData(), ThereIsFieldWithNameInSubCategory.class);
        }
    }

    public void removeField(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeField");
        query.getMethodInputs().put("name", name);
        Client.process(query);
    }

    public void addField(String name, String type) throws ThereIsFieldWithNameException, ThereIsFieldWithNameInSubCategory {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "renameField");
        query.getMethodInputs().put("name", name);
        query.getMethodInputs().put("type", type);
        Response response = Client.process(query);
        if (response.getReturnType().equals("ThereIsFieldWithNameException")) {
            throw new ThereIsFieldWithNameException();
        } else if (response.getReturnType().equals("ThereIsFieldWithNameInSubCategory")) {
            throw new ThereIsFieldWithNameInSubCategory();
        }
    }

    public void removeCategory(String name) throws ProductsController.NoCategoryWithName {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "removeCategory");
        query.getMethodInputs().put("name", name);
        Response response = Client.process(query);
        if (response.getReturnType().equals("NoCategoryWithName")) {
            throw new ProductsController.NoCategoryWithName();
        }
    }

    public Client.Models.Category getCategoryToEdit() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCategoryToEdit");
        Response response = Client.process(query);
        if (response.getReturnType().equals("Category")) {
            Gson gson = new Gson();
            SaveCategory saveCategory = gson.fromJson(response.getData(), SaveCategory.class);
            return new Client.Models.Category(saveCategory);
        } else {
            return null;
        }
    }

    public void setCategoryToEdit(int id) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCategoryToEdit");
        query.getMethodInputs().put("id", "" + id);
        Client.process(query);
    }

    public static class InvalidCategoryName extends Exception {
    }

    public static class ThereIsFieldWithNameException extends Exception {

    }

    public static class NoFieldWithNameException extends Exception {

    }

    public static class ThereIsFieldWithNameInSubCategory extends Exception {
        SaveCategory saveCategory;

        public SaveCategory getSaveCategory() {
            return saveCategory;
        }

        public Client.Models.Category getCategory() {
            return new Category(saveCategory);
        }
    }
}
