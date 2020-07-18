package Models;

import GUI.Constants;
import Network.Client;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class Sale implements Pendable {
    private SaveSale saveSale;
    private int offId;
    private ProductionStatus status;
    private Date startTime;
    private Date endTime;
    private Double salePercent;//be darsad nist
    private String editedField;

    public Sale(SaveSale saveSale){
        this.saveSale = saveSale;
        this.offId = saveSale.getOffId();
        this.startTime = new Date(saveSale.getStartTime());
        this.endTime = new Date(saveSale.getEndTime());
        this.status = saveSale.getStatus();
        this.salePercent = saveSale.getSalePercent();
        this.editedField = "";
    }

    public SaveSale getSaveSale() {
        return saveSale;
    }

    public Seller getSeller() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Seller");
        query.getMethodInputs().put("id", "" + saveSale.getSellerId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Seller")) {
            Gson gson = new Gson();
            SaveSeller saveSeller = gson.fromJson(response.getData(), SaveSeller.class);
            return new Seller(saveSeller);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public int getOffId() {
        return offId;
    }

    public List<Product> getProductsInSale() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Product");
        this.saveSale.getProductsInSaleIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Product>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveProduct>>(){}.getType();
            List<SaveProduct> allSaveProducts = gson.fromJson(response.getData(),type);
            List<Product> allProducts = new ArrayList<>();
            allSaveProducts.forEach(saveProduct -> allProducts.add(new Product(saveProduct)));
            return allProducts;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Double getSalePercent() {
        return salePercent;
    }
}