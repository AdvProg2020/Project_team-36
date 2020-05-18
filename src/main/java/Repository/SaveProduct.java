package Repository;

import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveProduct {
    private int productId;
    private String name;
    private String company;
    private int categoryId;
    private List<Field> fieldsOfCategory;
    private String information;
    private List<SaveProductField> productFields;
    private List<SaveScore> allScore;
    private Date productionDate;
    private List<SaveComment> allComments;
    private int seenNumber;
    private static int lastId = 0;



    private SaveProduct() {
    }

    public static void save(Product product){
        SaveProduct saveProduct = new SaveProduct();
        saveProduct.productId = product.getProductId();
        saveProduct.name = product.getName();
        saveProduct.company = product.getCompany();
        saveProduct.categoryId = product.getCategory().getCategoryId();
        saveProduct.information = product.getInformation();
        saveProduct.productionDate = product.getProductionDate();
        saveProduct.seenNumber = product.getSeenNumber();
        product.getFieldsOfCategory().forEach(feildOfCategory -> saveProduct.fieldsOfCategory.add(feildOfCategory));
        product.getAllComments().forEach(comment -> saveProduct.allComments.add(new SaveComment(comment)));
        product.getAllScore().forEach(score -> saveProduct.allScore.add(new SaveScore(score)));
        product.getProductFields().forEach(productField -> saveProduct.productFields.add(new SaveProductField(productField)));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveProductGson = gson.toJson(saveProduct);
        FileUtil.write(FileUtil.generateAddress(Product.class.getName(),saveProduct.productId),saveProductGson);
    }

    public static Product load(int id){
        if (Product.getProductById(id) != null){
            return Product.getProductById(id);
        }
        lastId = Math.max(id,lastId);
        Gson gson = new Gson();
        SaveProduct saveProduct = gson.fromJson(FileUtil.read(FileUtil.generateAddress(Product.class.getName(),id)),SaveProduct.class);
        Product product = new Product(saveProduct.productId,saveProduct.name,
                saveProduct.company,SaveCategory.load(saveProduct.categoryId),
                new ArrayList<>(saveProduct.fieldsOfCategory),saveProduct.information,
                saveProduct.productionDate,saveProduct.seenNumber);
        Product.addToAllProducts(product);
        saveProduct.allScore.forEach(saveScore -> product.getAllScore().add(saveScore.generateScore()));
        saveProduct.allComments.forEach(saveComment -> product.getAllComments().add(saveComment.generateComment()));
        saveProduct.productFields.forEach(saveProductField -> product.getProductFields().add(saveProductField.generateProductField()));
        return product;
    }
}
