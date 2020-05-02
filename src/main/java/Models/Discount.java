package Models;

import java.util.ArrayList;
import java.util.Date;

public class Discount implements Packable{
    private static ArrayList<Discount> allDiscounts = new ArrayList<>();
    private int id;
    private Date startTime;
    private Date endTime;
    private double discountPercent;//bar hasbe darsad nist
    private Long discountLimit;
    private int repetitionForEachUser;
    private ArrayList<Customer> customersIncluded;

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

    public static Discount getDiscount(int id){
        for (Discount discount : allDiscounts) {
            if(discount.getId()== id)
                return discount;
        }
        return null;
    }

    public int getRepetitionForEachUser() {
        return repetitionForEachUser;
    }

    public ArrayList<Customer> getCustomersIncluded() {
        return customersIncluded;
    }

    public boolean isDiscountAvailable(){
        Date now = new Date();
        return (now.after(this.startTime)&& now.before(this.endTime))||now.equals(this.startTime)||now.equals(this.endTime);
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
