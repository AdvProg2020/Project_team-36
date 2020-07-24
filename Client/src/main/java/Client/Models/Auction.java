package Client.Models;

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

    public Auction(SaveAuction saveAuction) {
        this.saveAuction = saveAuction;
        this.productField = new ProductField(saveAuction.getSaveProductField());
        this.endDate = new Date(saveAuction.getEndDate());
        this.highestPrice = saveAuction.getHighestPrice();
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
}
