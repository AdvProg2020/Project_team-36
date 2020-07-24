package Controllers;

import Models.*;
import Repository.SaveAuction;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "getAllAvailableProducts" -> processGetAllAvailableProducts(query);
            case "addNewAuction" -> processAddNewAuction(query);
            case "getAllAuctions" -> processGetAllAuctions(query);
            case "getAuction" -> processGetAuction(query);
            default -> new Response("Error", "");
        };
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


}
