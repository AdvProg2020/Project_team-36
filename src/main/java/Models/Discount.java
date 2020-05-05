package Models;

import java.text.SimpleDateFormat;
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

    public long getPayableAfterDiscount(long totalPrice){
        if(totalPrice*this.discountPercent>this.discountLimit){
            return totalPrice-this.discountLimit;
        }else{
            return totalPrice - (long)(totalPrice*this.discountPercent);
        }
    }

    public ArrayList<Customer> getCustomersIncluded() {
        return customersIncluded;
    }

    public static Discount getDiscount(int id){
        for (Discount discount : allDiscounts) {
            if(discount.getId()== id)
                return discount;
        }
        return null;
    }

    public static Discount getDiscountWithId(int id){
        for (Discount discount : allDiscounts) {
            if(discount.getId() == id){
                return discount;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        ArrayList<String> customersUsername = new ArrayList<>();
        for (Customer customer : customersIncluded) {
            customersUsername.add(customer.getUsername());
        }
        return  "  id=" + id +
                "\n  start time=" + formatter.format(startTime) +
                "\n  end time=" + formatter.format(endTime) +
                "\n  discount percent=" + discountPercent +
                "\n  discount limit=" + discountLimit +
                "\n  repetition for each user=" + repetitionForEachUser +
                "\n  customers included=" + customersUsername
                ;
    }

    public void removeDiscount(){
        for (Customer customer : customersIncluded) {
            customer.removeDiscount(this);
        }
        allDiscounts.remove(this);
    }

    public boolean isDiscountAvailable() {
        Date now = new Date();
        return (now.after(this.startTime) && now.before(this.endTime)) || now.equals(this.startTime) || now.equals(this.endTime);
    }

    public static boolean isThereDiscountWithId(int id){
        for (Discount discount : allDiscounts) {
            if(discount.getId() == id){
                return true;
            }
        }
        return false;
    }

    private int makeNewId(){
        return totalCodesMade+=1;
    }

    public static ArrayList<Discount> getAllDiscounts() {
        return allDiscounts;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
