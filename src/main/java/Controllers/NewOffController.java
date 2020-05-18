package Controllers;

import Models.*;

import java.util.ArrayList;
import java.util.Date;

public class NewOffController {
    private Seller seller;
    private ArrayList<Product> productsInSale;
    private ProductionStatus status;
    private Date startTime;
    private Date endTime;
    private Double salePercent;

    public NewOffController(SellerController sellerController) {
        productsInSale = new ArrayList<>();
        seller = (Seller)sellerController.userVariables.getLoggedInUser();
    }

    public void setProductsInSale(int productId) throws InvalidProductIdException, ProductAlreadyAddedException {
        if(!seller.isThereProduct(productId)){
            throw new InvalidProductIdException();
        } else if(isThereProduct(productId)){
            throw new ProductAlreadyAddedException();
        } else {
            productsInSale.add(Product.getProduct(productId));
        }
    }

    public boolean isThereProduct(int productId){
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
        new Request(new Sale(seller,productsInSale,startTime,endTime,salePercent));
    }

    public static class InvalidProductIdException extends Exception {

    }

    public static class ProductAlreadyAddedException extends Exception{

    }

    public static class EndDateBeforeStartDateException extends Exception {

    }

    public static class EndDatePassedException extends Exception {

    }

}
