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

    public void removeProduct(Product product){
        this.products.remove(product);
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

    public HashSet<Field> getAllFields() {
        return allFields;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public static Category getMainCategory() {
        return mainCategory;
    }

    public ArrayList<Category> getSubCategories() {
        return subCategories;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Category> getAllSubCategories() {
        ArrayList<Category> allCategories = new ArrayList<>();
        this.getAllSubCategories(this, allCategories);
        allCategories.remove(this);
        return allCategories;
    }

    public boolean isThereField(String name) {
        for (Field field : this.allFields) {
            if (field.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    private void getAllSubCategories(Category category, ArrayList<Category> allSubCategories) {
        if (category.getSubCategories().isEmpty())
            allSubCategories.addAll(category.subCategories);
        else {
            allSubCategories.addAll(category.subCategories);
            for (Category subCategory : this.subCategories) {
                getAllSubCategories(subCategory, allSubCategories);
            }
        }
    }


    public ArrayList<Product> getAllSubProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();
        Product.updateAllProducts();
        this.getAllSubProducts(this, allProducts);
        return allProducts;
    }

    private void getAllSubProducts(Category category, ArrayList<Product> allProducts) {
        if (category.getSubCategories().isEmpty())
            allProducts.addAll(category.products);
        else {
            allProducts.addAll(category.products);
            for (Category subCategory : this.subCategories) {
                getAllSubProducts(subCategory, allProducts);
            }
        }
    }

    public void renameField(Field field, String newName) {
        String oldName = field.getName();
        field.setName(newName);
        for (Product subProduct : this.getAllSubProducts()) {
            subProduct.renameField(oldName, newName);
        }
    }

    public Category isThereFieldInSubCategoryDifferentType(Field field, String name) {
        for (Category subCategory : this.getAllSubCategories()) {
            for (Field field1 : subCategory.getAllFields()) {
                if (field1.getName().equalsIgnoreCase(name) && !field.getClass().equals(field.getClass()))
                    return subCategory;
            }
        }
        return null;
    }

    public void removeField(String name) {
        this.removeFieldFromEach(getField(name));
        for (Category category : this.getAllSubCategories()) {
            category.removeFieldFromEach(category.getField(name));
        }
        for (Product product : this.getAllSubProducts()) {
            product.removeField(name);
        }
    }

    private void removeFieldFromEach(Field removingField) {
        for (Field field : this.getAllFields()) {
            if (field.getName().equalsIgnoreCase(removingField.getName())) {
                this.allFields.remove(field);
                break;
            }
        }
    }

    public void addField(Field field){
        this.setField(field);
        for (Category subCategory : this.getAllSubCategories()) {
            if(!subCategory.isThereField(field.getName()))
           subCategory.setField(field);
        }
        for (Product product : this.getAllSubProducts()) {
            if(!product.isThereField(field.getName()))
            product.addField(field);
        }
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }

}
