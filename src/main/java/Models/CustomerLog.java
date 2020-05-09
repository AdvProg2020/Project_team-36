package Models;

import java.util.ArrayList;
import java.util.Date;

public class CustomerLog {
    private static ArrayList<CustomerLog> allLogs;
    private static int totalLogsMade;
    private Date date;
    private int id;
    private double discountPercent;//be darsad nist yani0.2 mishe 20 darsad masan
    private long discountAmount;
    private long giftDiscount;
    private String customerAddress;
    private String customerPhoneNumber;
    private LogStatus logStatus;
    private long totalPrice;
    private long totalPayable;
    private ArrayList<ItemInLog> allItems;


    public static ArrayList<CustomerLog> getAllLogs() {
        return allLogs;
    }

    public int getId() {
        return id;
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

    public long getTotalPayable() {
        return totalPayable;
    }

    public ArrayList<ItemInLog> getAllItems() {
        return allItems;
    }

    public static CustomerLog createCustomerLog(WaitingLog waitingLog){
        //TODO create logs for customer and seller

        return null;
    }

}
