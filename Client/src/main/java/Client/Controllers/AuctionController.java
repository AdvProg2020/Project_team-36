package Client.Controllers;

import Client.GUI.Constants;
import Client.Models.GlobalVariables;
import Client.Models.Product;
import Client.Models.Sale;
import Client.Network.Client;
import Models.Query;
import Models.Response;
import Repository.SaveProduct;
import Repository.SaveSale;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Response response = Client.process(query);
    }
}

