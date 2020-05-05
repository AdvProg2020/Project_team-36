package Models;

import java.util.ArrayList;
import java.util.Date;

public class Log {
    private static ArrayList<Log> allLogs;
    private static int totalLogsMade;
    private Date date;
    private int id;
    private User user;
    private double discountPercent;//be darsad nist yani0.2 mishe 20 darsad masan
    private long discountAmount;
    private String customerAddress;
    private String customerPhoneNumber;
    private LogStatus logStatus;
    private long totalPrice;
    private long totalPayable;
    private ArrayList<ItemInLog> allItems;

    public static ArrayList<Log> getAllLogs() {
        return allLogs;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
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

    public static void createLogs(WaitingLog waitingLog){
        //TODO create logs for customer and seller
    }
}
