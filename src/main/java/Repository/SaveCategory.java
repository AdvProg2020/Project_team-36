package Repository;

import Models.Category;
import Models.Field;
import Models.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaveCategory {
    private String name;
    private int categoryId;
    private Set<Field> allFields;
    private int parentCategoryId;
    private List<Integer> subCategoriesIds;
    private List<Integer> productsIds;
    private static int lastId = 0;

    private SaveCategory() {
        subCategoriesIds = new ArrayList<>();
        productsIds = new ArrayList<>();
    }

    private void addSubCategory(Category subCategory) {
        int subCategoryId = subCategory.getCategoryId();
        subCategoriesIds.add(subCategoryId);
    }

    private void addProduct(Product product) {
        int productId = product.getProductId();
        productsIds.add(productId);
    }

    public static void save(Category category) {
        SaveCategory saveCategory = new SaveCategory();
        saveCategory.name = category.getName();
        saveCategory.categoryId = category.getCategoryId();
        saveCategory.parentCategoryId = category.getParentCategory().getCategoryId();
        saveCategory.allFields = category.getAllFields();
        category.getAllSubCategories().forEach(subCategory -> saveCategory.addSubCategory(subCategory));
        category.getProducts().forEach(product -> saveCategory.addProduct(product));
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
        Category parentCategory = load(saveCategory.parentCategoryId);
        HashSet<Field> allFields = new HashSet<>();
        saveCategory.allFields.forEach(field -> allFields.add(field));
        Category category = new Category(saveCategory.name, saveCategory.categoryId, allFields, parentCategory);
        Category.addToAllCategories(category);
        saveCategory.productsIds.forEach(productId -> category.getProducts().add(SaveProduct.load(productId)));
        saveCategory.subCategoriesIds.forEach(subCategoryId -> category.getAllSubCategories().add(load(subCategoryId)));
        return category;
    }

}
