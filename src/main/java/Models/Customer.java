package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Customer extends User implements Packable {
    private static ArrayList<Customer> allCustomers = new ArrayList<>();
    private long credit;
    private ArrayList<CustomerLog> allLogs;
    private WaitingLog waitingLog;
    private ArrayList<SelectedItem> cart;
    private HashMap<Discount, Integer> allDiscountsForCustomer;

    public Customer(String username) {
        super(username);
        this.allLogs = new ArrayList<>();
        this.cart = new ArrayList<>();
        this.allDiscountsForCustomer = new HashMap<>();
    }

    @Override
    public String getType() {
        return "customer";
    }


    public static void addNewCustomer(Customer customer) {
        allCustomers.add(customer);
    }

    public long getCredit() {
        return this.credit;
    }

    public void addNewLog(CustomerLog customerLog){
        this.allLogs.add(customerLog);
    }

    public ArrayList<CustomerLog> getAllLogs() {
        return allLogs;
    }

    public ArrayList<SelectedItem> getCart() {
        return cart;
    }

    public void setWaitingLog(WaitingLog waitingLog) {
        this.waitingLog = waitingLog;
    }

    public WaitingLog getWaitingLog() {
        return waitingLog;
    }

    public boolean isThereLog(int logId) {
        for (CustomerLog log : allLogs) {
            if (log.getId() == logId)
                return true;
        }
        return false;
    }

    public void removeDiscount(Discount discount) {
        allDiscountsForCustomer.remove(discount);
    }

    public boolean isThereDiscountCode(int discountCode) {
        for (Discount discount : allDiscountsForCustomer.keySet()) {
            if (discount.getId() == discountCode && allDiscountsForCustomer.get(discount) > 0)
                return true;
        }
        return false;
    }

    public void decreaseDiscountCode(Discount discount, int count) {
        Integer oldValue = allDiscountsForCustomer.get(discount);
        allDiscountsForCustomer.replace(discount, oldValue - count);
        if (allDiscountsForCustomer.get(discount) < 0)
            allDiscountsForCustomer.replace(discount, 0);
    }

    public static ArrayList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public void increaseDiscountCode(Discount discount, int count) {
        Integer oldValue = allDiscountsForCustomer.get(discount);
        allDiscountsForCustomer.replace(discount, oldValue + count);
    }

    public CustomerLog getLog(int logId) {
        for (CustomerLog log : allLogs) {
            if (log.getId() == logId) {
                return log;
            }
        }
        return null;
    }

    public HashMap<Discount, Integer> getAllDiscountsForCustomer() {
        this.updateDiscounts();
        return allDiscountsForCustomer;
    }

    private void updateDiscounts() {
        Set<Discount> temp = new HashSet<>();
        for (Discount discount : this.allDiscountsForCustomer.keySet()) {
            if (!discount.isDiscountAvailable()) {
                temp.add(discount);
            }
        }
        this.allDiscountsForCustomer.keySet().removeAll(temp);

    }

    public void decreaseCredit(long totalDecrease){
        this.credit -= totalDecrease;
    }

    public boolean isThereProductInCart(int productId) {
        for (SelectedItem item : cart) {
            if (item.getProduct().getProductId() == productId)
                return true;
        }
        return false;
    }

    public SelectedItem getProductInCart(int productId) {
        for (SelectedItem item : cart) {
            if (item.getProduct().getProductId() == productId)
                return item;
        }
        return null;
    }

    public void removeItemFromCart(SelectedItem item) {
        this.cart.remove(item);
    }

    public static boolean isThereCustomerWithUsername(String username) {
        for (Customer customer : allCustomers) {
            if (customer.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void setDiscountForCustomer(Discount discount) {
        this.allDiscountsForCustomer.put(discount, discount.getRepetitionForEachUser());
    }


    public long getCartPrice() {
        long sum = 0;
        for (SelectedItem item : cart) {
            sum += item.getItemTotalPrice();
        }
        return sum;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }


}
