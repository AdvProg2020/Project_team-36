package Models;

import java.util.ArrayList;
import java.util.Date;

public class Log implements Packable {
    private int id;
    private Date date;
    private long totalPrice;
    private double sale;
    private ArrayList<Item> allItems;
    private User user;
    private String status;
    private double discount;
    private static ArrayList<Log> allLogs = new ArrayList<>();

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public double getSale() {
        return sale;
    }

    public ArrayList<Item> getAllItems() {
        return allItems;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public double getDiscount() {
        return discount;
    }


    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
