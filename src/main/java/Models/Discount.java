package Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Discount {
    private static ArrayList<Discount> allDiscounts = new ArrayList<>();
    private int id;
    private Date startTime;
    private Date endTime;
    private double discountPercent;//bar hasbe darsad nist
    private long discountLimit;
    private int repetitionForEachUser;
    private static int totalCodesMade = 0;
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
                "\n  discount percent=" + (discountPercent*100) +
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

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setDiscountLimit(long discountLimit) {
        this.discountLimit = discountLimit;
    }

    public void setRepetitionForEachUser(int repetitionForEachUser) {
        this.repetitionForEachUser = repetitionForEachUser;
    }

    public void setTotalCodesMade(int totalCodesMade) {
        Discount.totalCodesMade = totalCodesMade;
    }

    public void setCustomersIncluded(ArrayList<Customer> customersIncluded) {
        this.customersIncluded.addAll(customersIncluded);
    }

    public void removeCustomersIncluded(ArrayList<Customer> customersIncluded){
        this.customersIncluded.removeAll(customersIncluded);
    }

    private int makeNewId(){
        return totalCodesMade+=1;
    }

    public static ArrayList<Discount> getAllDiscounts() {
        updateDiscounts();
        return allDiscounts;
    }

    public static Discount getDiscountById(int id){
        for (Discount discount : allDiscounts) {
            if (discount.id == id){
                return discount;
            }
        }
        return null;
    }

    public Discount(int id, Date startTime, Date endTime, double discountPercent, long discountLimit, int repetitionForEachUser) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountPercent = discountPercent;
        this.discountLimit = discountLimit;
        this.repetitionForEachUser = repetitionForEachUser;
        this.customersIncluded = new ArrayList<>();
    }

    public static void addToAllDiscounts(Discount discount){
        allDiscounts.add(discount);
    }

    public static void updateDiscounts(){
        ArrayList<Customer> toBeRemoved = new ArrayList<>();
        for (Discount discount : allDiscounts) {
            for (Customer customer : discount.customersIncluded) {
                if(customer.getStatus().equals(Status.DELETED)){
                    toBeRemoved.add(customer);
                }
            }
            discount.customersIncluded.removeAll(toBeRemoved);
            toBeRemoved.clear();
        }
    }
}