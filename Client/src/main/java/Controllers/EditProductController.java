package Controllers;

import GUI.Constants;
import Models.*;
import Network.Client;
import Repository.SaveCategory;
import Repository.SaveField;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditProductController {
    private String controllerName = "EditProductController";

    public Product getProductCopy(Product product){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getProductCopy");
        query.getMethodInputs().put("id", Integer.toString(product.getProductId()));
        Response response = Client.process(query);
        if (response.getReturnType().equals("Product")) {
            Gson gson = new Gson();
            SaveProduct saveProduct = gson.fromJson(response.getData(), SaveProduct.class);
            return new Product(saveProduct);
        } else {
            return null;
        }
    }

    public void editProductName(String newName){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editProductName");
        query.getMethodInputs().put("newName", newName);
        Client.process(query);
    }

    public void editProductCompany(String newCompany){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editProductCompany");
        query.getMethodInputs().put("newCompany", newCompany);
        Client.process(query);
    }

    public void editProductCategory(String newCategory)throws InvalidCategoryException{
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editProductCategory");
        query.getMethodInputs().put("newCategory", newCategory);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidCategoryException")) {
            throw new InvalidCategoryException();
        }
    }

    public ArrayList<Field> getNeededFields(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getNeededFields");
        Response response = Client.process(query);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SaveField>>() {
        }.getType();
        List<SaveField> allFieldsList = gson.fromJson(response.getData(), type);
        ArrayList<Field> allFields = new ArrayList<>();
        allFieldsList.forEach(saveField -> allFields.add(saveField.generateField()));
        return allFields;
    }

    public void setEachCategoryField(String value, Field field) throws InvalidFieldValue {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setEachCategoryField");
        query.getMethodInputs().put("value", value);
        query.getMethodInputs().put("fieldName", field.getName());
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidFieldValue")) {
            throw new InvalidFieldValue();
        }
    }

    public void setFieldsOfCategory(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFieldsOfCategory");
        Client.process(query);
    }

    public ArrayList<Field> getCategoryFieldsToEdit(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCategoryFieldsToEdit");
        Response response = Client.process(query);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SaveField>>() {
        }.getType();
        List<SaveField> allFieldsList = gson.fromJson(response.getData(), type);
        ArrayList<Field> allFields = new ArrayList<>();
        allFieldsList.forEach(saveField -> allFields.add(saveField.generateField()));
        return allFields;
    }

    public void setEditedField(String value, Field field) throws InvalidFieldValue, InvalidFieldException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setEditedField");
        query.getMethodInputs().put("value", value);
        query.getMethodInputs().put("fieldName", field.getName());
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidFieldException")) {
            throw new InvalidFieldException();
        } else if (response.getReturnType().equals("InvalidFieldValue")) {
            throw new InvalidFieldValue();
        }
    }

    public void editProductInformation(String newInformation){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editProductInformation");
        query.getMethodInputs().put("newInformation", newInformation);
        Client.process(query);
    }

    public void editProductPrice(String newPrice)throws NumberFormatException{
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editProductPrice");
        query.getMethodInputs().put("newPrice", newPrice);
        Response response = Client.process(query);
        if (response.getReturnType().equals("NumberFormatException")) {
            throw new NumberFormatException();
        }
    }

    public void editProductSupply(String newSupply)throws NumberFormatException{
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "editProductSupply");
        query.getMethodInputs().put("newSupply", newSupply);
        Response response = Client.process(query);
        if (response.getReturnType().equals("NumberFormatException")) {
            throw new NumberFormatException();
        }
    }

    public void sendEditProductRequest(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sendEditProductRequest");
        Client.process(query);    }

    public void sendEditProductFieldRequest(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sendEditProductFieldRequest");
        Client.process(query);
    }

    public static class InvalidCategoryException extends Exception{

    }

    public static class InvalidFieldException extends Exception{

    }

    public static class InvalidFieldValue extends Exception{

    }

}
