package Client.Models;

import Client.Controllers.ProductsController;
import Client.GUI.Constants;
import Client.Network.Client;
import Models.Query;
import Models.Response;
import Repository.SaveAuction;
import Repository.SaveChat;
import Repository.SaveCustomer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Auction {
    private SaveAuction saveAuction;
    private ProductField productField;
    private Date endDate;
    private long highestPrice;
    private int id;

    public Auction(SaveAuction saveAuction) {
        this.saveAuction = saveAuction;
        this.productField = new ProductField(saveAuction.getSaveProductField());
        this.endDate = new Date(saveAuction.getEndDate());
        this.highestPrice = saveAuction.getHighestPrice();
        this.id = saveAuction.getId();
    }

    public int getId() {
        return id;
    }

    public ProductField getProductField() {
        return productField;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getHighestPrice() {
        return highestPrice;
    }

    public Customer getFinalBuyer() {
        if (saveAuction.getFinalBuyerId() == -100000000){
            return null;
        }
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Customer");
        query.getMethodInputs().put("id", "" + saveAuction.getFinalBuyerId());
        Response response = Client.process(query);
        Gson gson = new Gson();
        return new Customer(gson.fromJson(response.getData(), SaveCustomer.class));
    }

    public Chat getChat(){
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Chat");
        query.getMethodInputs().put("id", "" + saveAuction.getChatId());
        Response response = Client.process(query);
        Gson gson = new Gson();
        return new Chat(gson.fromJson(response.getData(), SaveChat.class));
    }

    public Product getProduct() throws ProductsController.NoProductWithId {
        int productId = productField.getMainProductId();
        Product product = Constants.productsController.getProduct(productId);
        return product;
    }

    public String getProductName(){
        try {
            return getProduct().getName();
        } catch (ProductsController.NoProductWithId noProductWithId) {
            noProductWithId.printStackTrace();
            return "error";
        }
    }
}
