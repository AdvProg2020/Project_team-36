package Client.Controllers;

import Client.Models.Seller;
import Client.GUI.Constants;
import Models.*;
import Client.Network.Client;


public class NewProductController {
    private final String controllerName = "NewProductController";

    public NewProductController(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "new");
        Response response = Client.process(query);
    }

    public void setSeller(Seller seller) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setSeller");
        query.getMethodInputs().put("id", Integer.toString(seller.getUserId()));
        Client.process(query);
    }

    public void setName(String name) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setName");
        query.getMethodInputs().put("name", name);
        Client.process(query);
    }

    public void setCompany(String company) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCompany");
        query.getMethodInputs().put("company", company);
        Client.process(query);    }

    public void setCategory(String categoryName) throws InvalidCategoryName {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setCategory");
        query.getMethodInputs().put("categoryName", categoryName);
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidCategoryName")) {
            throw new InvalidCategoryName();
        }
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

    public void setInformation(String information) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setInformation");
        query.getMethodInputs().put("information", information);
        Client.process(query);
    }

    public void setProductField(long price, int supply) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setProductField");
        String supplyInt = Integer.toString(supply);
        String priceLong = Long.toString(price);
        query.getMethodInputs().put("price", priceLong);
        query.getMethodInputs().put("supply", supplyInt);
        Client.process(query);
    }

    public void sendNewProductRequest() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sendNewProductRequest");
        Client.process(query);
    }

    public void setImage(String imagePath) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setImage");
        query.getMethodInputs().put("imagePath", imagePath);
        Client.process(query);
    }

    public void setFile(String path){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFile");
        query.getMethodInputs().put("path", path);
        Client.process(query);
    }

    public void setFileName(String fileName){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setFileName");
        query.getMethodInputs().put("fileName", fileName);
        Client.process(query);
    }

    public static class InvalidCategoryName extends Exception {

    }

    public static class InvalidFieldValue extends Exception {

    }
}
