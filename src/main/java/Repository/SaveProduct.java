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

    public static void save(Product product){
        SaveProduct saveProduct = new SaveProduct(product);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveProductGson = gson.toJson(saveProduct);
        FileUtil.write(FileUtil.generateAddress(Product.class.getName(),saveProduct.productId),saveProductGson);
    }

    public static Product load(int id){
        if (Product.getProductById(id) != null){
            return Product.getProductById(id);
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Product.class.getName(),id));
        if(data == null){
            return null;
        }
        SaveProduct saveProduct = gson.fromJson(data,SaveProduct.class);
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

    public SaveProduct(Product product) {
        this.fieldsOfCategory = new ArrayList<>();
        this.productFields = new ArrayList<>();
        this.allScore = new ArrayList<>();
        this.allComments = new ArrayList<>();
        this.productId = product.getProductId();
        this.name = product.getName();
        this.company = product.getCompany();
        this.categoryId = product.getCategory().getCategoryId();
        this.information = product.getInformation();
        this.productionDate = product.getProductionDate();
        this.seenNumber = product.getSeenNumber();
        product.getFieldsOfCategory().forEach(feildOfCategory -> this.fieldsOfCategory.add(feildOfCategory));
        product.getAllComments().forEach(comment -> this.allComments.add(new SaveComment(comment)));
        product.getAllScore().forEach(score -> this.allScore.add(new SaveScore(score)));
        product.getProductFields().forEach(productField -> this.productFields.add(new SaveProductField(productField)));
    }

    public Product generateProduct(){
        Product product = new Product(this.productId,this.name,
                this.company,SaveCategory.load(this.categoryId),
                new ArrayList<>(this.fieldsOfCategory),this.information,
                this.productionDate,this.seenNumber);
        this.allScore.forEach(saveScore -> product.getAllScore().add(saveScore.generateScore()));
        this.allComments.forEach(saveComment -> product.getAllComments().add(saveComment.generateComment()));
        this.productFields.forEach(saveProductField -> product.getProductFields().add(saveProductField.generateProductField()));
        return product;
    }
}
