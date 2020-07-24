package Controllers;

import Models.*;
import Repository.SaveAuction;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuctionController {
    private GlobalVariables userVariables ;

    public AuctionController(GlobalVariables globalVariables){
     this.userVariables = globalVariables;
    }

    public void addNewAuction(int productId, Date date){
        ProductField product = Product.getProduct(productId).getProductFieldBySeller((Seller)userVariables.getLoggedInUser());
        product.setInAuction(true);
        Auction auction = new Auction(product,date);
        Auction.addNewAuction(auction);
    }

    public List<Product> getAllAvailableProducts(){
        Seller seller = (Seller) userVariables.getLoggedInUser();
        return seller.getAuctionProducts();
    }

    public ArrayList<Auction> getAllAuctions(){
        ArrayList<Auction> all = new ArrayList<>();
        all.addAll(Auction.getAllAuctions());
        return all;
    }

    public Auction getAuction(int id) throws NoAuctionWithId {
        for (Auction auction : Auction.getAllAuctions()) {
            if(auction.getId()==id)
                return auction;
        }
        throw new NoAuctionWithId();
    }

    public void addOffer(int amount,int auctionId) throws NotEnoughMoneyInWallet, NoAuctionWithId {
       if(((Customer) userVariables.getLoggedInUser()).getWallet().getAvailableMoney()<amount){
           throw new NotEnoughMoneyInWallet();
       }
       else{
           Auction auction;
           try {
                auction = getAuction(auctionId);
           } catch (NoAuctionWithId noAuctionWithId) {
               throw new NoAuctionWithId();
           }
           if(auction.getHighestPrice()<amount) {
               ((Customer) userVariables.getLoggedInUser()).getWallet().blockMoney(amount);
               if(auction.getFinalBuyer()!= null){
                   auction.getFinalBuyer().getWallet().unblockMoney(auction.getHighestPrice());
               }
               auction.setHighestPrice(amount);
               auction.setFinalBuyer(((Customer) userVariables.getLoggedInUser()));
           }
       }
    }

    public void buyAuction(int id,String address,String phoneNumber){
        try {
            Auction auction = getAuction(id);
            Product product = Product.getProduct(auction.getProductField().getMainProductId());
            Customer customer = auction.getFinalBuyer();
            long price = auction.getHighestPrice();
            auction.getProductField().setInAuction(false);
            auction.getProductField().decreaseSupply(1);
            new CustomerLog(customer,address,phoneNumber,price,product,auction.getProductField().getSeller());
            new SellerLog(customer,address,phoneNumber,product,auction.getProductField().getSeller(),price);
            customer.getWallet().withdrawFromBlocked(price);
            auction.getProductField().getSeller().getWallet().chargeWallet(((long)(price*Manager.getWage())));
            customer.removeAuction(auction.getId());
        } catch (NoAuctionWithId noAuctionWithId) {
            noAuctionWithId.printStackTrace();
        }
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "getAllAvailableProducts" -> processGetAllAvailableProducts(query);
            case "addNewAuction" -> processAddNewAuction(query);
            case "getAllAuctions" -> processGetAllAuctions(query);
            case "getAuction" -> processGetAuction(query);
            case "addOffer" -> processAddOffer(query);
            case "buyAuction" -> processBuyAuction(query);
            default -> new Response("Error", "");
        };
    }

    private Response processBuyAuction(Query query) {
        int id =Integer.parseInt( query.getMethodInputs().get("id"));
        String address = query.getMethodInputs().get("address");
        String phoneNumber = query.getMethodInputs().get("phoneNumber");
        buyAuction(id,address,phoneNumber);
        return new Response("void","");
    }

    private Response processAddOffer(Query query) {
        int amount = Integer.parseInt(query.getMethodInputs().get("amount"));
        int auctionId = Integer.parseInt(query.getMethodInputs().get("auctionId"));
        try {
            addOffer(amount,auctionId);
            return new Response("void","");
        } catch (NotEnoughMoneyInWallet notEnoughMoneyInWallet) {
            return new Response("NotEnoughMoneyInWallet","");
        } catch (NoAuctionWithId noAuctionWithId) {
            return new Response("NoAuctionWithId","");
        }
    }

    private Response processGetAuction(Query query) {
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        try {
            Auction auction = getAuction(id);
            SaveAuction saveAuction = new SaveAuction(auction);
            Gson gson = new GsonBuilder().create();
            return new Response("Auction",gson.toJson(saveAuction));
        } catch (NoAuctionWithId noAuctionWithId) {
            return new Response("NoAuctionWithId","");
        }
    }

    private Response processGetAllAuctions(Query query) {
        List<Auction> auctions =getAllAuctions();
        List<SaveAuction> saveAuctions = new ArrayList<>();
        auctions.forEach(auction -> saveAuctions.add(new SaveAuction(auction)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Auction>",gson.toJson(saveAuctions));
    }

    private Response processGetAllAvailableProducts(Query query) {
        List<Product> productList =getAllAvailableProducts();
        List<SaveProduct> saveProducts = new ArrayList<>();
        productList.forEach(product -> saveProducts.add(new SaveProduct(product)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Product>",gson.toJson(saveProducts));
    }

    private Response processAddNewAuction(Query query){
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        Date date = new Date(Long.parseLong(query.getMethodInputs().get("date")));
        addNewAuction(productId,date);
        return new Response("void","");
    }

    public static class NoAuctionWithId extends Exception{}

    public static class NotEnoughMoneyInWallet extends Exception{}

}
