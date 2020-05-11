package Models;

import java.util.ArrayList;

public class Product implements Pendable,Packable {
    private static ArrayList<Product> allProducts = new ArrayList<>();
    private int productId;
    private String name;
    private String company;
    private Category category;
    private ArrayList<Field> fieldsOfCategory;
    private String information;
    private ArrayList<ProductField> productFields;

    public int getProductId() {
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

    public ArrayList<ProductField> getProductFields() {
        return productFields;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
