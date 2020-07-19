package Controllers;

import Models.*;
import Repository.SaveField;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditProductController {

    private static HashMap<String, String> productFieldsSetters = new HashMap<>();
    private Seller seller;
    private ArrayList<Field> neededFields;
    private HashMap<String,Field> fieldsOfCategory;
    private Product editingProduct;
    //todo: beja id iebar editingProduct set mishe o ba un kar mishe

    public EditProductController(Seller seller) {
        this.seller = seller;
        writeProductFieldsSetters();
        neededFields = new ArrayList<>();
        fieldsOfCategory = new HashMap<>();
    }

    public Product getProductCopy(Product product){
        ProductField productField = new ProductField(product.getProductFieldBySeller(seller));
        editingProduct = new Product(product, productField);
        return editingProduct;
    }

//    public Method getProductFieldEditor(String chosenField, EditProductController editProductController) throws NoSuchMethodException{
//        for (String regex : productFieldsSetters.keySet()) {
//            if (chosenField.matches(regex)) {
//                return editProductController.getClass().getMethod(productFieldsSetters.get(regex),String.class,Product.class);
//            }
//        }
//        throw new NoSuchMethodException();
//    }

//    public void invokeProductEditor(String newValue,Product product, Method editor) throws IllegalAccessException, InvocationTargetException {
//        editor.invoke(this,newValue,product);
//    }

    public void editProductName(String newName){
        editingProduct.setName(newName);
        editingProduct.setEditedField("name");
    }

    public void editProductCompany(String newCompany){
        editingProduct.setCompany(newCompany);
        editingProduct.setEditedField("company");
    }

    public void editProductCategory(String newCategory)throws InvalidCategoryException{
        for (Category category : Category.getAllCategories()) {
            if(category.getName().equals(newCategory)){
                    editingProduct.setCategory(category);
                    neededFields.addAll(category.getAllFields());
                    editingProduct.setEditedField("category");
                    return;
            }
        }
        throw new InvalidCategoryException();
    }

    public ArrayList<Field> getNeededFields(){
        return neededFields;
    }

    public void setEachCategoryField(String value, Field field) throws InvalidFieldValue {
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

    public void setFieldsOfCategory(){
        ArrayList<Field> fields = new ArrayList<>();
        for (String field : fieldsOfCategory.keySet()) {
            fields.add(fieldsOfCategory.get(field));
        }
        editingProduct.setFieldsOfCategory(fields);
    }

    public ArrayList<Field> getCategoryFieldsToEdit(){
        editingProduct.setEditedField("fieldsOfCategory");
        return editingProduct.getFieldsOfCategory();
    }

    private Field getFieldToEdit(String fieldName)throws InvalidFieldException{
        if(editingProduct.isThereField(fieldName)){
            return editingProduct.getField(fieldName);
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

    public void editProductInformation(String newInformation){
        editingProduct.setInformation(newInformation);
        editingProduct.setEditedField("information");
    }

    public void editProductPrice(String newPrice)throws NumberFormatException{
        try{
            long price = Long.parseLong(newPrice);
            editingProduct.getProductFieldBySeller(seller).setPrice(price);
            editingProduct.getProductFieldBySeller(seller).setEditedField("price");
        }catch (NumberFormatException e){
            throw new NumberFormatException ();
        }
    }

    public void editProductSupply(String newSupply)throws NumberFormatException{
        try{
            int supply = Integer.parseInt(newSupply);
            editingProduct.getProductFieldBySeller(seller).setSupply(supply);
            editingProduct.getProductFieldBySeller(seller).setEditedField("supply");
        }catch (NumberFormatException e){
            throw new NumberFormatException ();
        }
    }

    public void sendEditProductRequest(){
        new Request(editingProduct,Status.TO_BE_EDITED);
    }

    public void sendEditProductFieldRequest(){
        new Request(editingProduct.getProductFieldBySeller(seller),Status.TO_BE_EDITED);
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

    public Response processQuery(Query query) {
        switch (query.getMethodName()) {
            case "getProductCopy":
                return processGetProductCopy(query);

            case "editProductName":
                return processEditProductName(query);

            case "editProductCompany":
                return processEditProductCompany(query);

            case "editProductCategory":
                return processEditProductCategory(query);

            case "getNeededFields":
                return processGetNeededFields();

            case "setEachCategoryField":
                return processSetEachCategoryField(query);

            case "setFieldsOfCategory":
                return processSetFieldsOfCategory();

            case "getCategoryFieldsToEdit":
                return processGetCategoryFieldsToEdit(query);

            case "setEditedField":
                return processSetEditedField(query);

            case "editProductInformation":
                return processEditProductInformation(query);

            case "editProductPrice":
                return processEditProductPrice(query);

            case "editProductSupply":
                return processEditProductSupply(query);

            case "sendEditProductRequest":
                return processSendEditProductRequest();

            case "sendEditProductFieldRequest":
                return processSendEditProductFieldRequest();

            default:
                return new Response("Error", "");
        }
    }

    private Response processGetProductCopy(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        SaveProduct saveProduct = new SaveProduct(getProductCopy(Product.getProductById(id)));
        Gson gson = new GsonBuilder().create();
        String saveProductGson = gson.toJson(saveProduct);
        return new Response("Product", saveProductGson);
    }

    private Response processEditProductName(Query query){
        editProductName(query.getMethodInputs().get("newName"));
        return new Response("void", "");
    }

    private Response processEditProductCompany(Query query){
        editProductCompany(query.getMethodInputs().get("newCompany"));
        return new Response("void", "");
    }

    private Response processEditProductCategory(Query query){
        try {
            editProductCategory(query.getMethodInputs().get("newCategory"));
            return new Response("void", "");
        } catch (InvalidCategoryException e) {
            return new Response("InvalidCategoryException", "");
        }
    }


    private Response processGetNeededFields(){
        List<SaveField> allSaveFields = new ArrayList<>();
        getNeededFields().forEach(c -> allSaveFields.add(new SaveField(c)));
        Gson gson = new GsonBuilder().create();
        String saveFieldListGson = gson.toJson(allSaveFields);
        return new Response("List<Field>", saveFieldListGson);
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

    private Response processSetFieldsOfCategory(){
        setFieldsOfCategory();
        return new Response("void", "");
    }

    private Response processGetCategoryFieldsToEdit(Query query){
        List<SaveField> allSaveFields = new ArrayList<>();
        getCategoryFieldsToEdit().forEach(c -> allSaveFields.add(new SaveField(c)));
        Gson gson = new GsonBuilder().create();
        String saveFieldListGson = gson.toJson(allSaveFields);
        return new Response("List<Field>", saveFieldListGson);
    }

    private Response processSetEditedField(Query query){
        try {
            Field field = getFieldToEdit(query.getMethodInputs().get("fieldName"));
            setEditedField(query.getMethodInputs().get("value"), field);
            return new Response("void", "");
        } catch (InvalidFieldException e) {
            return new Response("InvalidFieldException", "");
        } catch (InvalidFieldValue invalidFieldValue) {
            return new Response("InvalidFieldValue", "");
        }
    }

    private Response processEditProductInformation(Query query){
        editProductInformation(query.getMethodInputs().get("newInformation"));
        return new Response("void", "");
    }

    private Response processEditProductPrice(Query query){
        editProductPrice(query.getMethodInputs().get("newPrice"));
        return new Response("void", "");
    }

    private Response processEditProductSupply(Query query){
        editProductSupply(query.getMethodInputs().get("newSupply"));
        return new Response("void", "");
    }

    private Response processSendEditProductRequest(){
        sendEditProductRequest();
        return new Response("void", "");
    }

    private Response processSendEditProductFieldRequest(){
        sendEditProductFieldRequest();
        return new Response("void", "");
    }

    public static class InvalidCategoryException extends Exception{

    }

    public static class InvalidFieldException extends Exception{

    }

    public static class InvalidFieldValue extends Exception{

    }

}
