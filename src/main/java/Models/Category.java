package Models;

import java.util.ArrayList;

public class Category implements Packable {
    private String name;
    private int id;
    private ArrayList<Field> allFields;
    private Category parentCategory;
    private ArrayList<Category> subCategories;
    private ArrayList<Product> products;
    private static ArrayList<Category> allCategories = new ArrayList<>();

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
        return id;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
