package Models;

import java.util.ArrayList;
import java.util.Date;

public class Discount implements Packable{
    private static ArrayList<Discount> allDiscounts = new ArrayList<>();
    private String id;
    private Date startTime;
    private Date endTime;
    private double discountPercent;//bar hasbe darsad nist
    private Long discountLimit;
    private int repetitionForEachUser;
    private ArrayList<Customer> customersIncluded;

    public String getId() {
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

    public Long getDiscountLimit() {
        return discountLimit;
    }

    public int getRepetitionForEachUser() {
        return repetitionForEachUser;
    }

    public ArrayList<Customer> getCustomersIncluded() {
        return customersIncluded;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
