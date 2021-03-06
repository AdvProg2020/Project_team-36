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
    private String filePath;
    private String fileName;

    public NewProductController(SellerController sellerController) {
        fieldsOfCategory = new HashMap<>();
        neededFields = new ArrayList<>();
        seller = (Seller)sellerController.userVariables.getLoggedInUser();
    }

    public NewProductController() {
        fieldsOfCategory = new HashMap<>();
        neededFields = new ArrayList<>();

    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCategory(String categoryName) throws InvalidCategoryName{
        if(categoryName.equals("General Category")){
            this.category = Category.getMainCategory();
            neededFields.addAll(category.getAllFields());
            return;
        }
        for (Category category : Category.getAllCategories()) {
            if(category.getName().equalsIgnoreCase(categoryName)){
                this.category = Category.getCategory(categoryName);
                neededFields.addAll(category.getAllFields());
                return;
            }
        }
        throw new InvalidCategoryName();
    }



//    public void setCategory(Category category){
//        this.category = category;
//        //neededFields.addAll(category.getAllFields());
//    }
//
//    public ArrayList<Field> getNeededFields(){
//        return neededFields;
//    }

    public void setEachCategoryField(String value, Field field) throws InvalidFieldValue{
        if(field instanceof IntegerField){
            if(value.matches("\\d+\\.?\\d*")){
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

//    public void setProductField(long price, int supply, Seller seller) {
//        this.productField = new ProductField(price,seller,supply,0);
//    }

    private void setProductionDate() {
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
        Product product=null;
        if(filePath==null)
            product = new Product(name,company,category,fields,information,productField,productionDate, imagePath);
        else{
            product = new Product(name,company,category,fields,information,productField,productionDate, imagePath);
            product.setFileProduct(new FileProduct(filePath,fileName));
        }
        productField.setMainProductId(product.getProductId());
        new Request(product,Status.TO_BE_ADDED);
    }

    public void setImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setFile(String path){
        this.filePath = path;
    }

    public void setFileName(String fileName){
        this.fileName =fileName;
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "setName" -> processSetName(query);
            case "setCompany" -> processSetCompany(query);
            case "setCategory" -> processSetCategory(query);
            case "setEachCategoryField" -> processSetEachCategoryField(query);
            case "setInformation" -> processSetInformation(query);
            case "setProductField" -> processSetProductField(query);
            case "sendNewProductRequest" -> processSendNewProductRequest();
            case "setImage" -> processSetImage(query);
            case "setSeller" -> processSetSeller(query);
            case "setFile" -> processSetFile(query);
            case "setFileName" -> processSetFileName(query);
            default -> new Response("Error", "");
        };
    }

    private Response processSetFileName(Query query) {
        setFileName(query.getMethodInputs().get("fileName"));
        return new Response("void", "");
    }

    private Response processSetFile(Query query) {
        setFile(query.getMethodInputs().get("path"));
        return new Response("void", "");
    }

    private Response processSetName(Query query){
        setName(query.getMethodInputs().get("name"));
        return new Response("void", "");
    }

    private Response processSetCompany(Query query){
        setCompany(query.getMethodInputs().get("company"));
        return new Response("void", "");
    }

    private Response processSetCategory(Query query){
        try {
            setCategory(query.getMethodInputs().get("categoryName"));
            return new Response("void", "");
        } catch (InvalidCategoryName invalidCategoryName) {
            return new Response("InvalidCategoryName", "");
        }
    }

    private Response processSetEachCategoryField(Query query){
        String fieldName = query.getMethodInputs().get("fieldName");
        Field field = neededFields.get(0);
        for (Field neededField : neededFields) {
            if(neededField.getName().equals(fieldName)){
                field = neededField;
            }
        }
        try {
            setEachCategoryField(query.getMethodInputs().get("value"), field);
            return new Response("void", "");
        } catch (InvalidFieldValue invalidFieldValue) {
            return new Response("InvalidFieldValue", "");
        }
    }

    private Response processSetInformation(Query query){
        setInformation(query.getMethodInputs().get("information"));
        return new Response("void", "");
    }

    private Response processSetProductField(Query query){
        int supply = Integer.parseInt(query.getMethodInputs().get("supply"));
        long price = Long.parseLong(query.getMethodInputs().get("price"));
        setProductField(price, supply);
        return new Response("void", "");
    }

    private Response processSendNewProductRequest(){
        sendNewProductRequest();
        return new Response("void", "");
    }

    private Response processSetImage(Query query){
        setImage(query.getMethodInputs().get("imagePath"));
        return new Response("void", "");
    }

    private Response processSetSeller(Query query){
        Seller seller = Seller.getSellerById(Integer.parseInt(query.getMethodInputs().get("id")));
        setSeller(seller);
        return new Response("void", "");
    }

    public static class InvalidCategoryName extends Exception{

    }

    public static class InvalidFieldValue extends Exception{

    }
}
