package Controllers;

import Models.*;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "getAllAvailableProducts" -> processGetAllAvailableProducts(query);
            case "addNewAuction" -> processAddNewAuction(query);
            default -> new Response("Error", "");
        };
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
}
