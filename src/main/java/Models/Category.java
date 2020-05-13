package Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class Category implements Packable {
    private String name;
    private int categoryId;
    private static int totalCategoriesMade;
    private HashSet<Field> allFields;
    private Category parentCategory;
    private ArrayList<Category> subCategories;
    private ArrayList<Product> products;
    private static ArrayList<Category> allCategories = new ArrayList<>();
    private static Category mainCategory = new Category("General Category");


    public Category(String name) {
        this.name = name;
        this.categoryId = randomId();
        this.products = new ArrayList<>();
        this.subCategories = new ArrayList<>();
        this.allFields = new HashSet<>();
    }

    private int randomId() {
        totalCategoriesMade += 1;
        return totalCategoriesMade;
    }

    public static ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
        this.setParentCategoriesFields();
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setParentCategoriesFields() {
        HashSet<Field> fields = this.parentCategory.getAllFields();
        this.allFields.addAll(fields);
    }

    public void addSubCategory(Category category) {
        this.subCategories.add(category);
    }

    public void setSubCategories(ArrayList<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public void setField(Field field) {
        this.allFields.add(field);
    }

    public boolean isThereIntegerField(String name) {
        for (Field field : allFields) {
            if (field.getName().equalsIgnoreCase(name) && field instanceof IntegerField)
                return true;
        }
        return false;
    }

    public boolean isThereOptionalField(String name) {
        for (Field field : allFields) {
            if (field.getName().equalsIgnoreCase(name) && field instanceof OptionalField)
                return true;
        }
        return false;
    }

    public Field getField(String name) {
        for (Field field : allFields) {
            if (field.getName().equalsIgnoreCase(name))
                return field;
        }
        return null;
    }


    public void addCategory() {
        allCategories.add(this);
        this.getParentCategory().addSubCategory(this);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Field> getAllFields() {
        return allFields;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public ArrayList<Category> getSubCategories() {
        return subCategories;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
