package Repository;

import Models.CustomerLog;
import Models.ItemInLog;
import Models.LogStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveCustomerLog {
    private static int lastId = 0;
    private long date;
    private int id;
    private double discountPercent;//be darsad nist yani0.2 mishe 20 darsad masan
    private long discountAmount;
    private long giftDiscount;
    private String address;
    private String phoneNumber;
    private LogStatus logStatus;
    private long totalPrice;
    private long totalPayable;
    private List<ItemInLog> allItems;
    private String customerName;

    public SaveCustomerLog(CustomerLog customerLog) {
        this.date = customerLog.getDate().getTime();
        this.id = customerLog.getId();
        lastId = Math.max(lastId,this.id);
        this.customerName = customerLog.getCustomerName();
        this.discountPercent = customerLog.getDiscountPercent();
        this.discountAmount = customerLog.getDiscountAmount();
        this.giftDiscount = customerLog.getGiftDiscount();
        this.address = customerLog.getAddress();
        this.phoneNumber = customerLog.getPhoneNumber();
        this.logStatus = customerLog.getLogStatus();
        this.totalPrice = customerLog.getTotalPrice();
        this.totalPayable = customerLog.getTotalPayable();
        this.allItems = customerLog.getAllItems();
    }

    public CustomerLog generateCustomerLog(){
        ArrayList<ItemInLog> allItems = new ArrayList<>(this.allItems);
        return new CustomerLog(new Date(this.date),this.id,this.discountPercent,this.discountAmount,this.giftDiscount,
                this.address,this.phoneNumber,this.logStatus,this.totalPrice,this.totalPayable,allItems,this.customerName);
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

    public double getDiscountPercent() {
        return discountPercent;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public long getGiftDiscount() {
        return giftDiscount;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LogStatus getLogStatus() {
        return logStatus;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public long getTotalPayable() {
        return totalPayable;
    }

    public List<ItemInLog> getAllItems() {
        return allItems;
    }

    public String getCustomerName() {
        return customerName;
    }
}
