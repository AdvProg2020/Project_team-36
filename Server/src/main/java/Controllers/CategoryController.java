package Controllers;

import Models.*;
import Repository.SaveCategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private Category category;
    Field field;


    public Category getMainCategory() {
        return Category.getMainCategory();
    }

    public void setCategoriesName(String name) throws InvalidCategoryName {
        for (Category category : Category.getAllCategories()) {
            if (category.getName().equalsIgnoreCase(name))
                throw new InvalidCategoryName();
        }
        category = new Category(name);
    }

    public ArrayList<Category> getAllCategories() {
        return Category.getAllCategories();
    }

    public void setParentCategory(int i) {
        ArrayList<Category> allCategories = Category.getAllCategories();
        Category parentCategory;
        if (i == 0)
            parentCategory = Category.getMainCategory();
        else
            parentCategory = allCategories.get(i - 1);

        category.setParentCategory(parentCategory);

    }

    public void setParentCategory(String name) {
        Category parentCategory = null;
        if (name.equalsIgnoreCase("none"))
            parentCategory = Category.getMainCategory();
        else {
            for (Category category : getAllCategories()) {
                if (category.getName().equalsIgnoreCase(name)) {
                    parentCategory = category;
                    break;
                }
            }
        }
        category.setParentCategory(parentCategory);
    }

    public void setIntegerField(String name) throws ThereIsFieldWithNameException {
        if (this.category.getField(name) != null)
            throw new ThereIsFieldWithNameException();
        category.setField(new IntegerField(name));
    }

    public void setOptionalField(String name) throws ThereIsFieldWithNameException {
        if (this.category.getField(name) != null)
            throw new ThereIsFieldWithNameException();
        category.setField(new OptionalField(name));
    }

    public void acceptCategory() {
        category.addCategory();
    }

    public void editCategory(String name) throws InvalidCategoryName {
        for (Category category : Category.getAllCategories()) {
            if (category.getName().equalsIgnoreCase(name)) {
                this.category = category;
                return;
            }
        }
        throw new InvalidCategoryName();
    }

    public void editName(String name) throws InvalidCategoryName {
        for (Category category1 : Category.getAllCategories()) {
            if (category1.getName().equalsIgnoreCase(name))
                throw new InvalidCategoryName();
        }
        this.category.setName(name);
    }

    public Category getPendableCategory() {
        return this.category;
    }

    public void editField(String name) throws NoFieldWithNameException {
        for (Field field : this.category.getAllFields()) {
            if (field.getName().equalsIgnoreCase(name)) {
                this.field = field;
                return;
            }
        }
        throw new NoFieldWithNameException();
    }

    public void renameField(String newName) throws ThereIsFieldWithNameException, ThereIsFieldWithNameInSubCategory {
        if (category.isThereField(newName))
            throw new ThereIsFieldWithNameException();
        if (category.isThereFieldInSubCategoryDifferentType(this.field, newName) != null)
            throw new ThereIsFieldWithNameInSubCategory(category.isThereFieldInSubCategoryDifferentType(this.field, newName));
        this.category.renameField(this.field, newName);
    }

    public void removeField(String name) {
        category.removeField(name);
    }

    public void addField(String name, String type) throws ThereIsFieldWithNameException, ThereIsFieldWithNameInSubCategory {
        if (type.equals("IntegerField"))
            field = new IntegerField(name);
        else
            field = new OptionalField(name);
        if (category.isThereField(name))
            throw new ThereIsFieldWithNameException();
        else if (category.isThereFieldInSubCategoryDifferentType(field, name) != null)
            throw new ThereIsFieldWithNameInSubCategory(category.isThereFieldInSubCategoryDifferentType(field, name));
        else {
            category.addField(field);
        }
    }

    public void removeCategory(String name) throws ProductsController.NoCategoryWithName {
        if (!Category.isThereCategory(name))
            throw new ProductsController.NoCategoryWithName();
        Category.getCategory(name).removeCategory();
    }


    public static class InvalidCategoryName extends Exception {

    }

    public static class ThereIsFieldWithNameException extends Exception {

    }

    public static class NoFieldWithNameException extends Exception {

    }

    public static class ThereIsFieldWithNameInSubCategory extends Exception {
        SaveCategory saveCategory;

        public ThereIsFieldWithNameInSubCategory(Category category) {
            this.saveCategory = new SaveCategory(category);
        }

        public SaveCategory getSaveCategory() {
            return saveCategory;
        }
    }


    public Response processQuery(Query query) {
        switch (query.getMethodName()) {
            case "setCategoriesName":
                return processSetCategoriesName(query);

            case "getMainCategory":
                return processGetMainCategory();

            case "getAllCategories":
                return processGetAllCategories();

            case "setParentCategory":
                return processSetParentCategory(query);

            case "setIntegerField":
                return processSetIntegerField(query);

            case "setOptionalField":
                return processSetOptionalField(query);

            case "acceptCategory":
                return processAcceptCategory();

            case "editCategory":
                return processEditCategory(query);

            case "editName":
                return processEditName(query);

            case "getPendableCategory":
                return processGetPendableCategory();

            case "editField":
                return processEditField(query);

            case "removeField":
                return processRemoveField(query);

            case "renameField":
                return processRenameField(query);

            case "addField":
                return processAddField(query);

            case "removeCategory":
                return processRemoveCategory(query);

            default:
                return new Response("Error", "");
        }
    }

    private Response processRemoveCategory(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            removeCategory(name);
            return new Response("void", "");
        } catch (ProductsController.NoCategoryWithName noCategoryWithName) {
            return new Response("NoCategoryWithName", "");
        }
    }

    private Response processAddField(Query query) {
        String name = query.getMethodInputs().get("name");
        String type = query.getMethodInputs().get("type");
        try {
            addField(name, type);
            return new Response("void", "");
        } catch (ThereIsFieldWithNameException e) {
            return new Response("ThereIsFieldWithNameException", "");
        } catch (ThereIsFieldWithNameInSubCategory thereIsFieldWithNameInSubCategory) {
            Gson gson = new GsonBuilder().create();
            String thereIsFieldWithNameInSubCategoryGson = gson.toJson(thereIsFieldWithNameInSubCategory);
            return new Response("ThereIsFieldWithNameInSubCategory", thereIsFieldWithNameInSubCategoryGson);
        }
    }

    private Response processRenameField(Query query) {
        String newName = query.getMethodInputs().get("newName");
        try {
            renameField(newName);
            return new Response("void", "");
        } catch (ThereIsFieldWithNameException e) {
            return new Response("ThereIsFieldWithNameException", "");
        } catch (ThereIsFieldWithNameInSubCategory thereIsFieldWithNameInSubCategory) {
            Gson gson = new GsonBuilder().create();
            String thereIsFieldWithNameInSubCategoryGson = gson.toJson(thereIsFieldWithNameInSubCategory);
            return new Response("ThereIsFieldWithNameInSubCategory", thereIsFieldWithNameInSubCategoryGson);
        }
    }

    private Response processRemoveField(Query query) {
        String name = query.getMethodInputs().get("name");
        removeField(name);
        return new Response("void", "");
    }

    private Response processEditField(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            editField(name);
            return new Response("void", "");
        } catch (NoFieldWithNameException e) {
            return new Response("NoFieldWithNameException", "");
        }
    }

    private Response processGetPendableCategory() {
        Category category = getPendableCategory();
        Gson gson = new GsonBuilder().create();
        String saveCategoryGson = gson.toJson(new SaveCategory(category));
        return new Response("Category", saveCategoryGson);
    }

    private Response processEditName(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            editName(name);
            return new Response("void", "");
        } catch (InvalidCategoryName invalidCategoryName) {
            return new Response("InvalidCategoryName", "");
        }
    }

    private Response processEditCategory(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            editCategory(name);
            return new Response("void", "");
        } catch (InvalidCategoryName invalidCategoryName) {
            return new Response("InvalidCategoryName", "");
        }
    }

    private Response processAcceptCategory() {
        acceptCategory();
        return new Response("void", "");
    }

    private Response processSetOptionalField(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            setOptionalField(name);
            return new Response("void", "");
        } catch (ThereIsFieldWithNameException e) {
            return new Response("ThereIsFieldWithNameException", "");
        }
    }

    private Response processSetIntegerField(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            setIntegerField(name);
            return new Response("void", "");
        } catch (ThereIsFieldWithNameException e) {
            return new Response("ThereIsFieldWithNameException", "");
        }
    }

    private Response processSetParentCategory(Query query) {
        if (query.getMethodInputs().containsKey("i")) {
            int i = Integer.parseInt(query.getMethodInputs().get("i"));
            setParentCategory(i);
        } else if (query.getMethodInputs().containsKey("name")) {
            String name = query.getMethodInputs().get("name");
            setParentCategory(name);
        }
        return new Response("void", "");
    }

    private Response processGetAllCategories() {
        List<SaveCategory> allSaveCategories = new ArrayList<>();
        getAllCategories().forEach(c -> allSaveCategories.add(new SaveCategory(c)));
        Gson gson = new GsonBuilder().create();
        String saveCategoryListGson = gson.toJson(allSaveCategories);
        return new Response("List<Category>", saveCategoryListGson);
    }

    private Response processGetMainCategory() {
        Category category = getMainCategory();
        Gson gson = new GsonBuilder().create();
        String saveCategoryGson = gson.toJson(new SaveCategory(category));
        return new Response("Category", saveCategoryGson);
    }

    private Response processSetCategoriesName(Query query) {
        String name = query.getMethodInputs().get("name");
        try {
            setCategoriesName(name);
            return new Response("void", "");
        } catch (InvalidCategoryName invalidCategoryName) {
            return new Response("InvalidCategoryName", "");
        }
    }
}
