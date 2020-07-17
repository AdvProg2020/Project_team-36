package Controllers;

import Models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewOffController {
    private Seller seller;
    private ArrayList<Product> productsInSale;
    private ProductionStatus status;
    private Date startTime;
    private Date endTime;
    private Double salePercent;

    public NewOffController(Seller seller) {
        productsInSale = new ArrayList<>();
        this.seller = seller;
    }

    public void setProductsInSale(int productId) throws InvalidProductIdException{
        if(!seller.isThereProduct(productId)){
            throw new InvalidProductIdException();
        } else {
            productsInSale.add(Product.getProduct(productId));
        }
    }

    private boolean isThereProduct(int productId){
        for (Product product : productsInSale) {
            if(product.getProductId()==productId)
                return true;
        }
        return false;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime)throws EndDateBeforeStartDateException,EndDatePassedException {
        Date now = new Date();
        if(endTime.before(now)){
            throw new EndDatePassedException();
        }else if(endTime.before(startTime)){
            throw new EndDateBeforeStartDateException();
        }
        else {
            this.endTime = endTime;
        }
    }

    public void setSalePercent(Double salePercent) {
        this.salePercent = salePercent;
    }

    public void sendNewOffRequest(){
        new Request(new Sale(seller,productsInSale,startTime,endTime,salePercent),Status.TO_BE_ADDED);
    }

    public Response processQuery(Query query) {
        switch (query.getMethodName()) {
            case "setProductsInSale":
                return processSetProductsInSale(query);

            case "setStartTime":
                return processSetStartTime(query);

            case "setEndTime":
                return processSetEndTime(query);

            case "setSalePercent":
                return processSetSalePercent(query);

            case "sendNewOffRequest":
                return processSendNewOffRequest();

            default:
                return new Response("Error", "");
        }
    }

    private Response processSetProductsInSale(Query query){
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        try {
            setProductsInSale(productId);
            return new Response("void", "");
        } catch (InvalidProductIdException e) {
            return new Response("InvalidProductIdException", "");
        }
    }

    private Response processSetStartTime(Query query) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date startTime = dateFormat.parse(query.getMethodInputs().get("startTime"));
            setStartTime(startTime);
            return new Response("void", "");
        } catch (ParseException e) {
            return new Response("ParseException", "");
        }
    }

    private Response processSetEndTime(Query query) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date endTime = dateFormat.parse(query.getMethodInputs().get("endTime"));
            setEndTime(endTime);
            return new Response("void", "");
        } catch (ParseException e) {
            return new Response("ParseException", "");
        } catch (EndDateBeforeStartDateException e) {
            return new Response("EndDateBeforeStartDateException", "");
        } catch (EndDatePassedException e) {
            return new Response("EndDatePassedException", "");
        }
    }

    private Response processSetSalePercent(Query query){
        double percent = Double.parseDouble(query.getMethodInputs().get("salePercent"));
        setSalePercent(percent);
        return new Response("void", "");
    }

    private Response processSendNewOffRequest(){
        sendNewOffRequest();
        return new Response("void", "");
    }

    public static class InvalidProductIdException extends Exception {

    }

    public static class EndDateBeforeStartDateException extends Exception {

    }

    public static class EndDatePassedException extends Exception {

    }

}
