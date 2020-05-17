package Repository;

import Models.*;

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
    private List<SaveProductField> saveProductFields;
    private List<SaveScore> allScore;
    private Date productionDate;
    private List<SaveComment> allComments;
    private int seenNumber;


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


    }

    public static Product load(int id){
        return null;
    }
}
