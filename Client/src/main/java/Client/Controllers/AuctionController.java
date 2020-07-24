package Client.Controllers;

import Client.GUI.Constants;
import Client.Models.Auction;
import Client.Models.GlobalVariables;
import Client.Models.Product;
import Client.Network.Client;
import Models.Query;
import Models.Response;
import Repository.SaveAuction;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//todo HOSNE KHETAM
public class AuctionController {
    private final String controllerName = "AuctionController";
    private GlobalVariables userVariables;

    public AuctionController(GlobalVariables globalVariables){
        this.userVariables = globalVariables;
    }

    public ArrayList<Product> getAllAvailableProducts() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllAvailableProducts");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Product> allProducts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveProduct>>() {
        }.getType();
        List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(), type);
        allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
        return allProducts;
    }

    public void addNewAuction(int productId, Date date){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addNewAuction");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        query.getMethodInputs().put("date",Long.toString(date.getTime()));
        Client.process(query);
    }

    public ArrayList<Auction> getAllAuctions(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllAuctions");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Auction> allAuctions = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveAuction>>() {
        }.getType();
        List<SaveAuction> allSaveAuctions = gson.fromJson(response.getData(), type);
        allSaveAuctions.forEach(saveAuction -> allAuctions.add(new Auction(saveAuction)));
        return allAuctions;
    }

    public Auction getAuction(int id) throws NoAuctionWithId {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAuction");
        query.getMethodInputs().put("id",Integer.toString(id));
        Response response = Client.process(query);
       if(response.getReturnType().equals("NoAuctionWithId")) {
           throw new NoAuctionWithId();
        }
        Gson gson = new Gson();
        SaveAuction saveAuction = gson.fromJson(response.getData(), SaveAuction.class);
        return new Auction(saveAuction);
    }

    public void addOffer(int amount,int auctionId) throws NotEnoughMoneyInWallet, NoAuctionWithId {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addOffer");
        query.getMethodInputs().put("amount",Integer.toString(amount));
        query.getMethodInputs().put("auctionId",Integer.toString(auctionId));
        Response response = Client.process(query);
        if(response.getReturnType().equals("NotEnoughMoneyInWallet")){
            throw new NotEnoughMoneyInWallet();
        }else if(response.getReturnType().equals("NoAuctionWithId")){
            throw new NoAuctionWithId();
        }
    }

    public void buyAuction(int id,String address,String phoneNumber){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "buyAuction");
        query.getMethodInputs().put("id",Integer.toString(id));
        query.getMethodInputs().put("address",address);
        query.getMethodInputs().put("phoneNumber",phoneNumber);
        Response response = Client.process(query);

    }
    public static class NoAuctionWithId extends Exception{}

    public static class NotEnoughMoneyInWallet extends Exception{}
}

