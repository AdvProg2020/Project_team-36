package Models;

import GUI.Constants;
import Network.Client;
import Repository.SaveCustomer;
import Repository.SaveSellerLog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SellerLog {
    private SaveSellerLog saveSellerLog;
    private Date date;
    private int id;
    private long sale;
    private String customerAddress;
    private String customerPhoneNumber;
    private LogStatus logStatus;
    private long totalPrice;
    private List<ItemInLog> allItems;

    public SellerLog(SaveSellerLog saveSellerLog){
        this.saveSellerLog = saveSellerLog;
        this.date = new Date(saveSellerLog.getDate());
        this.id = saveSellerLog.getId();
        this.sale = saveSellerLog.getSale();
        this.customerAddress = saveSellerLog.getCustomerAddress();
        this.customerPhoneNumber = saveSellerLog.getCustomerPhoneNumber();
        this.totalPrice = saveSellerLog.getTotalPrice();
        this.logStatus = saveSellerLog.getLogStatus();
        this.allItems = new ArrayList<>();
        allItems.addAll(saveSellerLog.getAllItems());
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Customer");
        query.getMethodInputs().put("id", "" + saveSellerLog.getCustomerId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Customer")) {
            Gson gson = new Gson();
            SaveCustomer saveCustomer = gson.fromJson(response.getData(), SaveCustomer.class);
            return new Customer(saveCustomer);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public long getSale() {
        return sale;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public LogStatus getLogStatus() {
        return logStatus;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public List<ItemInLog> getAllItems() {
        return allItems;
    }
}
