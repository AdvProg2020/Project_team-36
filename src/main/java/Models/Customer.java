package Models;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User implements Packable{
    private static ArrayList<Customer> allCustomers;
    private long credit;
    private ArrayList<Log> allLogs;
    private HashMap<Integer,Product> cart;
    private HashMap<Integer,Discount> allDiscountsForCustomer;


    public long getCredit() {
        return credit;
    }

    public ArrayList<Log> getAllLogs() {
        return allLogs;
    }

    public HashMap<Integer, Product> getCart() {
        return cart;
    }

    public HashMap<Integer, Discount> getAllDiscounts() {
        return allDiscounts;
    }
}
