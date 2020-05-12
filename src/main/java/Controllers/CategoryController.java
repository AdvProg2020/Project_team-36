package Controllers;

import Models.Category;
import Models.Field;
import Models.IntegerField;
import Models.OptionalField;

import java.util.ArrayList;

public class CategoryController {
    private Category category;


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
        category.setParentCategoriesFields();
    }

    public void setIntegerField(String name) throws ThereIsFieldWithNameException {
        if(this.category.getField(name)!= null)
            throw new ThereIsFieldWithNameException();
        category.setField(new IntegerField(name));
    }

    public void setOptionalField(String name) throws ThereIsFieldWithNameException {
        if(this.category.getField(name)!= null)
            throw new ThereIsFieldWithNameException();
        category.setField(new OptionalField(name));
    }

    public void acceptCategory(){
        category.addCategory();
    }


    public static class InvalidCategoryName extends Exception {

    }

    public static class ThereIsFieldWithNameException extends Exception {

    }
}
