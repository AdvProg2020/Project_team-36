package Models;

import java.util.ArrayList;
import java.util.Date;

import static Models.LogStatus.WAITING_TO_BE_SENT;

public class CustomerLog {
    private static ArrayList<CustomerLog> allLogs = new ArrayList<>();
    private static int totalLogsMade;
    private Date date;
    private int id;
    private double discountPercent;//be darsad nist yani0.2 mishe 20 darsad masan
    private long discountAmount;
    private long giftDiscount;
    private String address;
    private String phoneNumber;
    private LogStatus logStatus;
    private long totalPrice;
    private long totalPayable;
    private ArrayList<ItemInLog> allItems;

    public CustomerLog(double discountPercent,long discountAmount,long giftDiscount,String address,String phoneNumber,ArrayList<ItemInLog> allItems,long totalPayable,long totalPrice){
        this.id = randomId();
        this.date = new Date();
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.giftDiscount = giftDiscount;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.allItems = new ArrayList<>();
        this.allItems.addAll(allItems);
        this.totalPayable = totalPayable;
        this.totalPrice = totalPrice;
        this.logStatus = WAITING_TO_BE_SENT;

    }

    public static ArrayList<CustomerLog> getAllLogs() {
        return allLogs;
    }

    public int getId() {
        return id;
    }

    private int randomId(){
        totalLogsMade +=1;
        return totalLogsMade;
    }

    public Date getDate() {
        return date;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public long getDiscountAmount() {
        return discountAmount;
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

    public ArrayList<ItemInLog> getAllItems() {
        return allItems;
    }

    public static CustomerLog createCustomerLog(WaitingLog waitingLog){
        int totalPrice = 0;
        ArrayList <ItemInLog> itemsInLog = ItemInLog.createItemInLog(waitingLog.getAllItems());
        for (ItemInLog itemInLog : itemsInLog) {
            totalPrice +=(itemInLog.getInitialPrice()*itemInLog.getCount());
        }
        long payable = waitingLog.getPayablePrice();
        Discount discount = waitingLog.getDiscount();
        CustomerLog customerLog = new CustomerLog(discount.getDiscountPercent(),waitingLog.getDiscountAmount(),
                waitingLog.getGiftDiscount(),waitingLog.getCustomerAddress(),waitingLog.getCustomerPhoneNumber(),itemsInLog,payable,totalPrice);
        allLogs.add(customerLog);
        waitingLog.getCustomer().addNewLog(customerLog);
        return customerLog;
    }

}
