package Repository;

import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaveCategory {
    private String name;
    private int categoryId;
    private Set<IntegerField> allIntegerFields;
    private Set<OptionalField> allOptionalFields;
    private int parentCategoryId;
    private List<Integer> subCategoriesIds;
    private List<Integer> productsIds;
    private static int lastId = 0;

    public static int getLastId() {
        return lastId;
    }

    private SaveCategory() {
        subCategoriesIds = new ArrayList<>();
        productsIds = new ArrayList<>();
        allOptionalFields = new HashSet<>();
        allIntegerFields = new HashSet<>();
    }

    private void addSubCategory(Category subCategory) {
        int subCategoryId = subCategory.getCategoryId();
        subCategoriesIds.add(subCategoryId);
    }

    private void addProduct(Product product) {
        int productId = product.getProductId();
        productsIds.add(productId);
    }

    public SaveCategory(Category category){
        subCategoriesIds = new ArrayList<>();
        productsIds = new ArrayList<>();
        allOptionalFields = new HashSet<>();
        allIntegerFields = new HashSet<>();
        this.name = category.getName();
        this.categoryId = category.getCategoryId();
        if (category.getParentCategory() != null){
            this.parentCategoryId = category.getParentCategory().getCategoryId();
        }
        for (Field field : category.getAllFields()) {
            if (field instanceof IntegerField){
                this.allIntegerFields.add((IntegerField) field);
            }
            if (field instanceof OptionalField){
                this.allOptionalFields.add((OptionalField) field);
            }
        }
        category.getSubCategories().forEach(subCategory -> this.addSubCategory(subCategory));
        category.getProducts().forEach(product -> this.addProduct(product));
    }

    public static void save(Category category) {
        SaveCategory saveCategory = new SaveCategory(category);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveCategoryGson = gson.toJson(saveCategory);
        FileUtil.write(FileUtil.generateAddress(Category.class.getName(), saveCategory.categoryId), saveCategoryGson);
    }

    public static Category load(int id) {
        lastId = Math.max(lastId,id);
        Category potentialCategory = Category.getCategoryById(id);
        if (potentialCategory != null) {
            return potentialCategory;
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Category.class.getName(), id));
        if (data == null){
            return null;
        }
        SaveCategory saveCategory = gson.fromJson(data, SaveCategory.class);

        Category category;
        if (saveCategory.name.equals("General Category")){
             category = new Category(saveCategory.name, saveCategory.categoryId, new HashSet<>(), null);
             Category.setMainCategory(category);
        }else {
            Category parentCategory = load(saveCategory.parentCategoryId);
            HashSet<Field> allFields = new HashSet<>();
            saveCategory.allIntegerFields.forEach(field -> allFields.add(field));
            saveCategory.allOptionalFields.forEach(field -> allFields.add(field));
            category = new Category(saveCategory.name, saveCategory.categoryId, allFields, parentCategory);
            Category.addToAllCategories(category);
            saveCategory.productsIds.forEach(productId -> category.getProducts().add(SaveProduct.load(productId)));
            saveCategory.subCategoriesIds.forEach(subCategoryId -> category.getSubCategories().add(load(subCategoryId)));
        }

        return category;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Set<IntegerField> getAllIntegerFields() {
        return allIntegerFields;
    }

    public Set<OptionalField> getAllOptionalFields() {
        return allOptionalFields;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public List<Integer> getSubCategoriesIds() {
        return subCategoriesIds;
    }

    public List<Integer> getProductsIds() {
        return productsIds;
    }
}
