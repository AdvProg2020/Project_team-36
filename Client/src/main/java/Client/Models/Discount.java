package Client.Models;

import Client.GUI.Constants;
import Models.Query;
import Models.Response;
import Client.Network.Client;
import Repository.SaveCustomer;
import Repository.SaveDiscount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class Discount {

    private SaveDiscount saveDiscount;
    private int id;
    private Date startTime;
    private Date endTime;
    private double discountPercent;//bar hasbe darsad nist
    private long discountLimit;
    private int repetitionForEachUser;

    public Discount(SaveDiscount saveDiscount) {
        this.id = saveDiscount.getId();
        this.saveDiscount = saveDiscount;
        this.startTime = new Date(saveDiscount.getStartTime());
        this.endTime = new Date(saveDiscount.getEndTime());
        this.discountLimit = saveDiscount.getDiscountLimit();
        this.discountPercent = saveDiscount.getDiscountPercent();
        this.repetitionForEachUser = saveDiscount.getRepetitionForEachUser();
    }

    public SaveDiscount getSaveDiscount() {
        return saveDiscount;
    }

    public int getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public long getDiscountLimit() {
        return discountLimit;
    }

    public int getRepetitionForEachUser() {
        return repetitionForEachUser;
    }

    public List<Customer> getCustomersIncluded() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Customer");
        this.saveDiscount.getCustomersIncludedIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Customer>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveCustomer>>(){}.getType();
            List<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(),type);
            List<Customer> allCustomers = new ArrayList<>();
            allSaveCustomers.forEach(saveCustomer -> allCustomers.add(new Customer(saveCustomer)));
            return allCustomers;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public int getDiscountPercentForTable(){ return (int)(discountPercent*100); }

    public long getPayableAfterDiscount(long totalPrice){
        if(totalPrice*this.discountPercent>this.discountLimit){
            return totalPrice-this.discountLimit;
        }else{
            return totalPrice - (long)(totalPrice*this.discountPercent);
        }
    }

}
