package Models;

import GUI.Constants;
import Network.Client;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductField implements Pendable{
    private SaveProductField saveProductField;
    private int mainProductId;
    private Status status;
    private long price;
    private int supply;


    public ProductField(SaveProductField saveProductField) {
        this.saveProductField = saveProductField;
        this.mainProductId = saveProductField.getMainProductId();
        this.status = saveProductField.getStatus();
        this.price = saveProductField.getPrice();
        this.supply = saveProductField.getSupply();

    }

    public SaveProductField getSaveProductField() {
        return saveProductField;
    }

    public int getMainProductId() {
        return mainProductId;
    }

    public Status getStatus() {
        return status;
    }

    public long getPrice() {
        return price;
    }

    public Sale getSale() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Sale");
        query.getMethodInputs().put("id", "" + saveProductField.getOffId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Sale")) {
            Gson gson = new Gson();
            SaveSale saveSale = gson.fromJson(response.getData(), SaveSale.class);
            return new Sale(saveSale);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public Seller getSeller() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Seller");
        query.getMethodInputs().put("id", "" + saveProductField.getSellerId());
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

    public int getSupply() {
        return supply;
    }

    public Set<Customer> getAllBuyers() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Customer");
        this.saveProductField.getCustomerIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Customer>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveCustomer>>(){}.getType();
            List<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(),type);
            Set<Customer> allCustomers = new HashSet<>();
            allSaveCustomers.forEach(saveCustomer -> allCustomers.add(new Customer(saveCustomer)));
            return allCustomers;
        }else {
            System.out.println(response);
            return null;
        }
    }
}
