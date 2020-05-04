package Models;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User implements Packable{
    private static ArrayList<Customer> allCustomers = new ArrayList<>();
    private long credit;
    private ArrayList<Log> allLogs;
    private ArrayList<Item> cart;
    private HashMap<Discount,Integer> allDiscountsForCustomer;

    public Customer(String username){
        super(username);
        this.allLogs = new ArrayList<>();
        this.cart = new ArrayList<>();
        this.allDiscountsForCustomer = new HashMap<>();
    }

    @Override
    public String getType() {
        return "customer";
    }

    public long getCredit() {
        return credit;
    }

    public ArrayList<Log> getAllLogs() {
        return allLogs;
    }

    public ArrayList<Item> getCart() {
        return cart;
    }

    public HashMap<Discount, Integer> getAllDiscountsForCustomer() {
        return allDiscountsForCustomer;
    }

    public static ArrayList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public void setDiscountForCustomer(Discount discount) {
        this.allDiscountsForCustomer.put(discount,discount.getRepetitionForEachUser());
    }

    public void removeDiscount(Discount discount){
        allDiscountsForCustomer.remove(discount);
    }

    public boolean isThereDiscountCode(int discountCode) {
        for (Discount discount : allDiscountsForCustomer.keySet()) {
            if (discount.getId() == discountCode && allDiscountsForCustomer.get(discount) > 0)
                return true;
        }
        return false;
    }

    public void decreaseDiscountCode(Discount discount, int count){
        Integer oldValue = allDiscountsForCustomer.get(discount);
        allDiscountsForCustomer.replace(discount,oldValue-count);
        if(allDiscountsForCustomer.get(discount)<0)
            allDiscountsForCustomer.replace(discount,0);
    }

    public void increaseDiscountCode(Discount discount,int count){
        Integer oldValue = allDiscountsForCustomer.get(discount);
        allDiscountsForCustomer.replace(discount,oldValue+count);
    }

    public static boolean isThereCustomerWithUsername(String username){
        for (Customer customer : allCustomers) {
            if (customer.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
