package Repository;

import Models.Customer;
import Models.ItemInLog;
import Models.LogStatus;
import Models.SellerLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveSellerLog {
    private static int lastId = 0;
    private long date;
    private int id;
    private int customerId;
    private long sale;
    private String customerAddress;
    private String customerPhoneNumber;
    private LogStatus logStatus;
    private long totalPrice;
    private List<ItemInLog> allItems;

    public SaveSellerLog(SellerLog sellerLog) {
        this.date = sellerLog.getDate().getTime();
        this.id = sellerLog.getId();
        lastId = Math.max(lastId,this.id);
        this.customerId = sellerLog.getCustomer().getUserId();
        this.sale = sellerLog.getSale();
        this.customerAddress = sellerLog.getCustomerAddress();
        this.customerPhoneNumber = sellerLog.getCustomerPhoneNumber();
        this.logStatus = sellerLog.getLogStatus();
        this.totalPrice = sellerLog.getTotalPrice();
        this.allItems = sellerLog.getAllItems();
    }

    public SellerLog generateSellerLog(){
        ArrayList<ItemInLog> allItems = new ArrayList<>(this.allItems);
        return new SellerLog(new Date(this.date),this.id,Customer.getCustomerById(this.customerId),
                this.sale,this.customerAddress,this.customerPhoneNumber,this.logStatus,this.totalPrice,allItems);
    }

    public static int getLastId() {
        return lastId;
    }

    public long getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
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
