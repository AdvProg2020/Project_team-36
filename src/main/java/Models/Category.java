package Models;

import GUI.ManageCategoriesController;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Category {
    private String name;
    private int categoryId;
    static Random random = new Random();
    private static int totalCategoriesMade = random.nextInt(4988 - 1000) + 1000;
    private HashSet<Field> allFields ;
    private Category parentCategory;
    private ArrayList<Category> subCategories;
    private ArrayList<Product> products;
    private static ArrayList<Category> allCategories = new ArrayList<>();
    private static Category mainCategory = new Category("General Category");
    private static ManageCategoriesController manageCategoriesController;

    public static void addTest(){
        Category sublessRoot = new Category("sublessRoot",90,null,mainCategory);
        sublessRoot.addCategory();
        Category root = new Category("root",100,null,mainCategory);
        root.addCategory();
        root.setField(new IntegerField("integer1root"));
        root.setField(new OptionalField("iO2root"));
        Category rootSub1 = new Category("rootSub1",101,null,root);
        rootSub1.addCategory();
        rootSub1.setField(new IntegerField("integer1rootSub1"));
        rootSub1.setField(new IntegerField("integer2rootsub1"));
        Category rootSub2 = new Category("rootSub2",102,null,root);
        rootSub2.setField(new IntegerField("integerROotSub2"));
        rootSub2.setField(new OptionalField("optionalRootSub2"));
        rootSub2.addCategory();
        Category sub2Sub = new Category("sub2Sub",1021,null,rootSub2);
        sub2Sub.setField(new IntegerField("integerSub2Sub"));
        sub2Sub.setField(new OptionalField("optionalSub2Sub"));
        sub2Sub.addCategory();
    }

    public Category(String name) {
        this.name = name;
        this.categoryId = randomId();
        this.products = new ArrayList<>();
        this.subCategories = new ArrayList<>();
        this.allFields = new HashSet<>();
    }

    public Category(String name, int categoryId, HashSet<Field> allFields, Category parentCategory) {
        this.name = name;
        this.categoryId = categoryId;//todo
        this.allFields = new HashSet<>();
       // this.allFields = allFields;
        this.parentCategory = parentCategory;
        this.subCategories = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public static void addToAllCategories(Category category){
        allCategories.add(category);
    }
    public int getCategoryId() {
        return categoryId;
    }


    public static Category getCategoryById(int id){
        for (Category category : allCategories) {
            if (category.categoryId == id){
                return category;
            }
        }
        return null;
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

    public void addProduct(Product product){
        this.products.add(product);
    }

    public boolean isThereIntegerField(String name) {
        for (Field field : allFields) {
            if (field.getName().equalsIgnoreCase(name) && field instanceof IntegerField)
                return true;
        }
        return false;
    }

    public static void setTotalCategoriesMade(int totalCategoriesMade) {
        Category.totalCategoriesMade = totalCategoriesMade;
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

    public static boolean isThereCategory(String name){
        for (Category category : allCategories) {
            if(category.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public static Category getCategory(String name){
        for (Category category : allCategories) {
            if(category.getName().equalsIgnoreCase(name))
                return category;
        }
        return null;
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
        if (category.getSubCategories().isEmpty()) {
            if(!category.products.isEmpty()) {
                allProducts.addAll(category.products);
            }
        }
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

    public void removeCategory(){
        int size = this.getAllSubProducts().size();
        for(int i=0;i<size;i++){
            Product.removeProduct(this.getAllSubProducts().get(i));
            size--;
            i--;
        }
        this.getParentCategory().removeSubCategory(this);
        allCategories.removeAll(this.getAllSubCategories());
        allCategories.remove(this);
    }

    private void removeSubCategory(Category category){
        subCategories.remove(category);
    }

    public Hyperlink getViewHyperlink(){
        Hyperlink remove = new Hyperlink();
        remove.setText("view");
        remove.setStyle("");
        remove.setOnAction(e->{
            manageCategoriesController.viewAction(this);
        });
        return remove;
    }

    public Hyperlink getEditHyperlink(){
        Hyperlink remove = new Hyperlink();
        remove.setText("edit");
        remove.setStyle("");
        remove.setOnAction(e->{
            manageCategoriesController.editAction(this);
        });
        return remove;
    }

    public static void setManageCategoriesController(ManageCategoriesController manageCategoriesController) {
        Category.manageCategoriesController = manageCategoriesController;
    }

}
