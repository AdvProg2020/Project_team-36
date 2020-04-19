package Models;

import java.util.ArrayList;

public class Product implements Pendable,Packable {

    private static ArrayList<Product> allProducts;
    private String productId;
    private String name;
    private String company;
    private Category category;
    private ArrayList<Field> fieldsOfCategory = new ArrayList<>();
    private String information;
    private ArrayList<ProductFieald> productFiealds = new ArrayList<>();

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public String getCompany() {
        return company;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<Field> getFieldsOfCategory() {
        return fieldsOfCategory;
    }

    public String getInformation() {
        return information;
    }

    public ArrayList<ProductFieald> getProductFiealds() {
        return productFiealds;
    }

    @Override
    public Data pack(Object object) {
        return null;
    }

    @Override
    public Object unpack(Data data) {
        return null;
    }
}
