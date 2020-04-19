package Models;

import java.util.ArrayList;
import java.util.Date;

public class Log implements Packable {
    private String id;
    private Date date;
    private long totalPrice;
    private Sale sale;
    private ArrayList<item> allItems;
    private User user;
    private String status;
    private Discount discount;
    public static ArrayList<Log> allLogs;

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public Sale getSale() {
        return sale;
    }

    public ArrayList<item> getAllItems() {
        return allItems;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public Discount getDiscount() {
        return discount;
    }
}
