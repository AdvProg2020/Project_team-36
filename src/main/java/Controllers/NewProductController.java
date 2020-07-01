package Controllers;

import Models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewProductController {
    private Seller seller;
    private String name;
    private String company;
    private Category category;
    private HashMap<String,Field> fieldsOfCategory;
    private ArrayList<Field> neededFields;
    private String information;
    private ProductField productField;
    private Date productionDate;
    private String imagePath;

    public NewProductController(SellerController sellerController) {
        fieldsOfCategory = new HashMap<>();
        neededFields = new ArrayList<>();
        seller = (Seller)sellerController.userVariables.getLoggedInUser();
    }

    public NewProductController() {
        fieldsOfCategory = new HashMap<>();
        neededFields = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCategory(String categoryName) throws InvalidCategoryName{
        for (Category category : Category.getAllCategories()) {
            if(category.getName().equalsIgnoreCase(categoryName)){
                this.category = Category.getCategory(categoryName);
                neededFields.addAll(category.getAllFields());
                return;
            }
        }
        throw new InvalidCategoryName();
    }

    public void setCategory(Category category){
        this.category = category;
        //neededFields.addAll(category.getAllFields());
    }

    public ArrayList<Field> getNeededFields(){
        return neededFields;
    }

    public void setEachCategoryField(String value, Field field) throws InvalidFieldValue{
        if(field instanceof IntegerField){
            if(value.matches("\\d+\\.?\\d+")){
                IntegerField newField = new IntegerField(field.getName());
                newField.setValue(value);
                if(fieldsOfCategory.containsKey(field.getName())){
                    fieldsOfCategory.replace(field.getName(),newField);
                } else {
                    fieldsOfCategory.put(field.getName(), newField);
                }
            } else {
                throw new InvalidFieldValue();
            }
        } else if (field instanceof OptionalField){
            OptionalField newField = new OptionalField(field.getName());
            newField.setValue(value);
            if(fieldsOfCategory.containsKey(field.getName())){
                fieldsOfCategory.replace(field.getName(),newField);
            } else {
                fieldsOfCategory.put(field.getName(), newField);
            }
        }

    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setProductField(long price, int supply) {
        this.productField = new ProductField(price,seller,supply,0);
    }

    public void setProductField(long price, int supply, Seller seller) {
        this.productField = new ProductField(price,seller,supply,0);
    }

    public void setProductionDate() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            this.productionDate = format.parse(format.format(now));
        } catch (ParseException e){
            //nothing
        }
    }

    public void sendNewProductRequest(){
        setProductionDate();
        ArrayList<Field> fields = new ArrayList<>();
        for (String field : fieldsOfCategory.keySet()) {
            fields.add(fieldsOfCategory.get(field));
        }
        Product product = new Product(name,company,category,fields,information,productField,productionDate, imagePath);
        productField.setMainProductId(product.getProductId());
        new Request(product,Status.TO_BE_ADDED);
    }

    public void setImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public static class InvalidCategoryName extends Exception{

    }

    public static class InvalidFieldValue extends Exception{

    }
}
