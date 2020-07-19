package Controllers;

import GUI.Constants;
import Models.*;
import Network.Client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewOffController {
    private String controllerName = "NewOffController";


    public void setProductsInSale(int productId) throws InvalidProductIdException{
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setProductsInSale");
        query.getMethodInputs().put("productId", Integer.toString(productId));
        Response response = Client.process(query);
        if (response.getReturnType().equals("InvalidProductIdException")) {
            throw new InvalidProductIdException();
        }
    }

    public void setStartTime(Date startTime) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setStartTime");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String startTimeString = dateFormat.format(startTime);
        query.getMethodInputs().put("startTime", startTimeString);
        Client.process(query);
    }

    public void setEndTime(Date endTime)throws EndDateBeforeStartDateException,EndDatePassedException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setEndTime");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String endTimeString = dateFormat.format(endTime);
        query.getMethodInputs().put("endTime", endTimeString);
        Response response = Client.process(query);
        if (response.getReturnType().equals("EndDateBeforeStartDateException")) {
            throw new EndDateBeforeStartDateException();
        } else if (response.getReturnType().equals("EndDatePassedException")) {
            throw new EndDatePassedException();
        }
    }

    public void setSalePercent(Double salePercent) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setSalePercent");
        String discountPercentString = Double.toString(salePercent);
        query.getMethodInputs().put("salePercent", discountPercentString);
        Client.process(query);    }

    public void sendNewOffRequest(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sendNewOffRequest");
        Client.process(query);      }

    public static class InvalidProductIdException extends Exception {

    }

    public static class EndDateBeforeStartDateException extends Exception {

    }

    public static class EndDatePassedException extends Exception {

    }

}
