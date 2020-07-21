package Models;

import Repository.SaveCustomerLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerLog {
    private SaveCustomerLog saveCustomerLog;
    private Date date;
    private int id;
    private double discountPercent;//be darsad nist yani0.2 mishe 20 darsad masan
    private long discountAmount;
    private long giftDiscount;
    private String address;
    private String phoneNumber;
    private LogStatus logStatus;
    private long totalPrice;//bedune haraj va discount
    private long totalPayable;
    private List<ItemInLog> allItems;
    private String customerName;

    public CustomerLog(SaveCustomerLog saveCustomerLog) {
        this.saveCustomerLog = saveCustomerLog;
        this.id = saveCustomerLog.getId();
        this.date = new Date(saveCustomerLog.getDate());
        this.address = saveCustomerLog.getAddress();
        this.discountAmount = saveCustomerLog.getDiscountAmount();
        this.discountPercent = saveCustomerLog.getDiscountPercent();
        this.giftDiscount = saveCustomerLog.getGiftDiscount();
        this.logStatus = saveCustomerLog.getLogStatus();
        this.phoneNumber = saveCustomerLog.getPhoneNumber();
        this.totalPayable = saveCustomerLog.getTotalPayable();
        this.totalPrice = saveCustomerLog.getTotalPrice();
        this.customerName = saveCustomerLog.getCustomerName();
        this.allItems = new ArrayList<>();
        allItems.addAll(saveCustomerLog.getAllItems());
    }

    public SaveCustomerLog getSaveCustomerLog() {
        return saveCustomerLog;
    }

    public Date getDate() {
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
