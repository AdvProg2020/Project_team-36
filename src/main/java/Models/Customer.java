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
