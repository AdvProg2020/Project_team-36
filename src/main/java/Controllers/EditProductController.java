package Controllers;

import Models.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EditProductController {

    private static HashMap<String, String> productFieldsSetters = new HashMap<>();
    private Seller seller;
    private ArrayList<Field> neededFields;
    private ArrayList<Field> fieldsOfCategory;


    public EditProductController(Seller seller) {
        this.seller = seller;
        writeProductFieldsSetters();
        neededFields = new ArrayList<>();
        fieldsOfCategory = new ArrayList<>();
    }

    public Product getProductCopy(Product product){
        return new Product(product,product.getProductFieldBySeller(seller));
    }

    public Method getProductFieldEditor(String chosenField, SellerController sellerController) throws NoSuchMethodException{
        for (String regex : productFieldsSetters.keySet()) {
            if (chosenField.matches(regex)) {
                return sellerController.getClass().getMethod(productFieldsSetters.get(regex),String.class,Product.class);
            }
        }
        throw new NoSuchMethodException();
    }

    public void invokeProductEditor(String newValue,Product product, Method editor) throws IllegalAccessException, InvocationTargetException {
        editor.invoke(this,newValue,product);
    }

    public void editProductName(String newName, Product product){
        product.setName(newName);
    }

    public void editProductCompany(String newCompany, Product product){
        product.setCompany(newCompany);
    }

    public void editProductCategory(String newCategory, Product product)throws SameCategoryException,InvalidCategoryException{
        for (Category category : Category.getAllCategories()) {
            if(category.getName().equals(newCategory)){
                if(category.equals(product.getCategory())){
                    throw new SameCategoryException();
                }else{
                    product.setCategory(category);
                    neededFields.addAll(category.getAllFields());
                    return;
                }
            }
        }
        throw new InvalidCategoryException();
    }

    public ArrayList<Field> getNeededFields(){
        return neededFields;
    }

    public void setEachCategoryField(String value, Field field) throws InvalidFieldValue {
        if(field instanceof IntegerField){
            if(value.matches("\\d+\\.?\\d+")){
                IntegerField newField = new IntegerField(field.getName());
                newField.setValue(value);
                fieldsOfCategory.add(newField);
            } else {
                throw new InvalidFieldValue();
            }
        } else if (field instanceof OptionalField){
            OptionalField newField = new OptionalField(field.getName());
            newField.setValue(value);
            fieldsOfCategory.add(newField);
        }
    }

    public void setFieldsOfCategory(Product product){
        product.setFieldsOfCategory(fieldsOfCategory);
    }

    public ArrayList<Field> getCategoryFieldsToEdit(Product product){
        return product.getFieldsOfCategory();
    }

    public Field getFieldToEdit(String fieldName, Product product)throws InvalidFieldException{
        if(product.isThereField(fieldName)){
            return product.getField(fieldName);
        } else {
            throw new InvalidFieldException();
        }
    }

    public void setEditedField(String value, Field field) throws InvalidFieldValue {
        if(field instanceof IntegerField){
            if(value.matches("\\d+\\.?\\d+")){
                field.setValue(value);
            } else {
                throw new InvalidFieldValue();
            }
        } else if (field instanceof OptionalField){
            field.setValue(value);
        }
    }

    public void editProductInformation(String newInformation, Product product){
        product.setInformation(newInformation);
    }


    public void editProductPrice(String newPrice, Product product)throws NumberFormatException{
        try{
            long price = Long.parseLong(newPrice);
            product.getProductFieldBySeller(seller).setPrice(price);
        }catch (NumberFormatException e){
            throw new NumberFormatException ();
        }
    }

    public void editProductSupply(String newSupply, Product product)throws NumberFormatException{
        try{
            int supply = Integer.parseInt(newSupply);
            product.getProductFieldBySeller(seller).setSupply(supply);
        }catch (NumberFormatException e){
            throw new NumberFormatException ();
        }
    }

    public void sendEditProductRequest(Product product){
        new Request(product);
    }

    public void sendEditProductFieldRequest(Product product){
        new Request(product.getProductFieldBySeller(seller));
    }

    private void writeProductFieldsSetters() {
        productFieldsSetters.put("name", "editProductName");
        productFieldsSetters.put("company", "editProductCompany");
        productFieldsSetters.put("category", "editProductCategory");
        productFieldsSetters.put("fields\\s+of\\s+category", "editProductFieldsOfCategory");
        productFieldsSetters.put("information", "editProductInformation");
        productFieldsSetters.put("Price", "editProductPrice");
        productFieldsSetters.put("supply", "editProductSupply");
    }

    public static class SameCategoryException extends Exception{

    }

    public static class InvalidCategoryException extends Exception{

    }

    public static class InvalidFieldException extends Exception{

    }

    public static class InvalidFieldValue extends Exception{

    }

}
