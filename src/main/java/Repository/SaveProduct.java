package Repository;

import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class SaveProduct {
    private int productId;
    private String name;
    private String company;
    private int categoryId;
    private List<IntegerField> integerFieldsOfCategory;
    private List<OptionalField> optionalFieldsOfCategory;
    private String information;
    private List<SaveProductField> productFields;
    private List<SaveScore> allScore;
    private long productionDate;
    private List<SaveComment> allComments;
    private String productImageURL;
    private Set<Integer> allBuyers;
    private int seenNumber;

    public static void save(Product product){
        SaveProduct saveProduct = new SaveProduct(product);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveProductGson = gson.toJson(saveProduct);
        FileUtil.write(FileUtil.generateAddress(Product.class.getName(),saveProduct.productId),saveProductGson);
    }

    public static Product load(int id){
        Product potentialProduct = Product.getProductById(id);
        if (potentialProduct != null){
            return potentialProduct;
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Product.class.getName(),id));
        if(data == null){
            return null;
        }
        SaveProduct saveProduct = gson.fromJson(data,SaveProduct.class);
        Product product = new Product(saveProduct.productId,saveProduct.name,
                saveProduct.company,SaveCategory.load(saveProduct.categoryId),
                new ArrayList<>(saveProduct.integerFieldsOfCategory),new ArrayList<>(saveProduct.optionalFieldsOfCategory),saveProduct.information,
                new Date(saveProduct.productionDate),saveProduct.seenNumber,saveProduct.productImageURL);
        Product.addToAllProducts(product);
        saveProduct.allScore.forEach(saveScore -> product.getAllScore().add(saveScore.generateScore()));
        saveProduct.allComments.forEach(saveComment -> product.getAllComments().add(saveComment.generateComment()));
        saveProduct.productFields.forEach(saveProductField -> product.getProductFields().add(saveProductField.generateProductField()));
        saveProduct.allBuyers.forEach(buyyerId ->  product.getAllBuyers().add((Customer) SaveUser.load(buyyerId)));
        return product;
    }

    public SaveProduct(Product product) {
        this.allBuyers = new HashSet<>();
        this.productImageURL = product.getProductImageUrl();
        this.integerFieldsOfCategory = new ArrayList<>();
        this.optionalFieldsOfCategory = new ArrayList<>();
        this.productFields = new ArrayList<>();
        this.allScore = new ArrayList<>();
        this.allComments = new ArrayList<>();
        this.productId = product.getProductId();
        this.name = product.getName();
        this.company = product.getCompany();
        this.categoryId = product.getCategory().getCategoryId();
        this.information = product.getInformation();
        this.productionDate = product.getProductionDate().getTime();
        this.seenNumber = product.getSeenNumber();
        for (Field field : product.getFieldsOfCategory()) {
            if (field instanceof IntegerField){
                this.integerFieldsOfCategory.add((IntegerField) field);
            }
            if (field instanceof OptionalField){
                this.optionalFieldsOfCategory.add((OptionalField) field);
            }
        }
        product.getAllBuyers().forEach(customer -> this.allBuyers.add(customer.getUserId()));
        product.getAllComments().forEach(comment -> this.allComments.add(new SaveComment(comment)));
        product.getAllScore().forEach(score -> this.allScore.add(new SaveScore(score)));
        product.getProductFields().forEach(productField -> this.productFields.add(new SaveProductField(productField)));
    }

    public Product generateProduct(){
        Product product = new Product(this.productId,this.name,
                this.company,SaveCategory.load(this.categoryId),
                new ArrayList<>(this.integerFieldsOfCategory),new ArrayList<>(this.optionalFieldsOfCategory),this.information,
                new Date(this.productionDate),this.seenNumber,this.productImageURL);
        this.allScore.forEach(saveScore -> product.getAllScore().add(saveScore.generateScore()));
        this.allComments.forEach(saveComment -> product.getAllComments().add(saveComment.generateComment()));
        this.productFields.forEach(saveProductField -> product.getProductFields().add(saveProductField.generateProductField()));
        this.allBuyers.forEach(buyer -> product.getAllBuyers().add(SaveCustomer.load(buyer)));
        return product;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public List<IntegerField> getIntegerFieldsOfCategory() {
        return integerFieldsOfCategory;
    }

    public List<OptionalField> getOptionalFieldsOfCategory() {
        return optionalFieldsOfCategory;
    }

    public String getInformation() {
        return information;
    }

    public List<SaveProductField> getProductFields() {
        return productFields;
    }

    public List<SaveScore> getAllScore() {
        return allScore;
    }

    public long getProductionDate() {
        return productionDate;
    }

    public List<SaveComment> getAllComments() {
        return allComments;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public Set<Integer> getAllBuyers() {
        return allBuyers;
    }

    public int getSeenNumber() {
        return seenNumber;
    }
}
