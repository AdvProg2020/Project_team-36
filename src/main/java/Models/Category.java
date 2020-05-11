package Models;

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


    public Category(String name){
        this.name = name;
        this.categoryId = randomId();
        this.products = new ArrayList<>();
        this.subCategories = new ArrayList<>();
        this.allFields = new HashSet<>();
    }

    private int randomId(){
        totalCategoriesMade+= 1;
        return totalCategoriesMade;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void addSubCategory(Category category){
        this.subCategories.add(category);
    }

    public void setSubCategories(ArrayList<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public void setField(Field field){
        allFields.add(field);
    }

    public static void addCategory(Category category){
        allCategories.add(category);
        category.getParentCategory().addSubCategory(category);
    }

    public String getName() {
        return name;
    }

    public HashSet<Field> getAllFields() {
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
