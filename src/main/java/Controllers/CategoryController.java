package Controllers;

import Models.Category;
import Models.Field;
import Models.IntegerField;
import Models.OptionalField;

import java.util.ArrayList;

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

    public void setParentCategory(String name){
        Category parentCategory = null;
        if(name.equalsIgnoreCase("none"))
        parentCategory = Category.getMainCategory();
        else{
            for (Category category : getAllCategories()) {
                if(category.getName().equalsIgnoreCase(name)){
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
            if(category1.getName().equalsIgnoreCase(name))
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
        Category category;

        public ThereIsFieldWithNameInSubCategory(Category category) {
            this.category = category;
        }

        public Category getCategory() {
            return category;
        }
    }

    //-..-
}
