package Client.Models;

import Client.GUI.Constants;
import Models.Query;
import Models.Response;
import Client.Network.Client;
import Models.Wallet;
import Repository.SaveChat;
import Repository.SaveProduct;
import Repository.SaveSale;
import Repository.SaveSeller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Seller extends User implements Pendable {
    private SaveSeller saveSeller;
    private List<SellerLog> allLogs;
    private long credit;
    private String companyName;
    private String companyInfo;
    private Wallet wallet;

    public Seller(SaveSeller saveSeller){
        super(saveSeller);
        this.saveSeller = saveSeller;
        this.companyName = saveSeller.getCompanyName();
        this.companyInfo = saveSeller.getCompanyInfo();
        this.wallet = saveSeller.getWallet();
        this.credit = saveSeller.getCredit();
        this.allLogs = new ArrayList<>();
        saveSeller.getAllSellerLogs().forEach(saveSellerLog -> allLogs.add(new SellerLog(saveSellerLog)));
    }

    @Override
    public String getType() {
        return null;
    }

    public SaveSeller getSaveSeller() {
        return saveSeller;
    }

    public List<SellerLog> getAllLogs() {
        return allLogs;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public List<Product> getAllProducts() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Product");
        this.saveSeller.getAllProductIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
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

    public List<Sale> getAllSales() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Sale");
        this.saveSeller.getAllOffIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Sale>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveSale>>(){}.getType();
            List<SaveSale> allSaveSales = gson.fromJson(response.getData(),type);
            List<Sale> allSales = new ArrayList<>();
            allSaveSales.forEach(saveSale -> allSales.add(new Sale(saveSale)));
            return allSales;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public long getCredit() {
        return credit;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    @Override
    public String getPendingRequestType() {
        return "seller";
    }

    @Override
    public ArrayList<Chat> getChats() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Chat");
        this.saveSeller.getChatsIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Chat>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveChat>>(){}.getType();
            List<SaveChat> allSaveChats = gson.fromJson(response.getData(),type);
            ArrayList<Chat> allChats = new ArrayList<>();
            allSaveChats.forEach(saveChat -> allChats.add(new Chat(saveChat)));
            return allChats;
        }else {
            System.out.println(response);
            return null;
        }
    }
}
