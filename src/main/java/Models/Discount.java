package Models;

import java.util.ArrayList;
import java.util.Date;

public class Discount implements Packable {
    private static ArrayList<Discount> allDiscounts = new ArrayList<>();
    private int id;
    private Date startTime;
    private Date endTime;
    private double discountPercent;//bar hasbe darsad nist
    private long discountLimit;
    private int repetitionForEachUser;
    private int totalCodesMade = 0;
    private ArrayList<Customer> customersIncluded;

    public Discount(Date startTime, Date endTime, double discountPercent, long discountLimit, int repetitionForEachUser, ArrayList<Customer> customersIncluded) {
        this.id = makeNewId();
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountPercent = discountPercent;
        this.discountLimit = discountLimit;
        this.repetitionForEachUser = repetitionForEachUser;
        this.customersIncluded = customersIncluded;
        allDiscounts.add(this);
    }

    public int getId() {
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

    public boolean isDiscountAvailable() {
        Date now = new Date();
        return (now.after(this.startTime) && now.before(this.endTime)) || now.equals(this.startTime) || now.equals(this.endTime);
    }

    private int makeNewId(){
        return totalCodesMade+=1;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
