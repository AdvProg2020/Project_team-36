package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Customer extends User  {
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

    public void setCredit(long credit) {
        this.credit = credit;
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
        updateCart();
        for (SelectedItem selectedItem : this.cart) {
            selectedItem.checkTag();
        }
        return this.cart;
    }

    public WaitingLog getWaitingLog(){
        return this.waitingLog;
    }

    public void setWaitingLog(WaitingLog waitingLog) {
        this.waitingLog = waitingLog;
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

    public HashMap<Discount, Integer> getAllActiveDiscountsForCustomer() {
        HashMap<Discount,Integer> toBeReturned = new HashMap<>();
        for (Discount discount : this.allDiscountsForCustomer.keySet()) {
            if(discount.isDiscountAvailable()&&allDiscountsForCustomer.get(discount)>0)
                toBeReturned.put(discount,allDiscountsForCustomer.get(discount));
        }
        return toBeReturned;
    }


    public void decreaseCredit(long totalDecrease){
        this.credit -= totalDecrease;
    }

    public boolean isThereProductInCart(int productId) {
        updateCart();
        for (SelectedItem item : cart) {
            if (item.getProduct().getProductId()==(productId))
                return true;
        }
        return false;
    }

    public SelectedItem getProductInCart(int productId) {
        for (SelectedItem item : cart) {
            if (item.getProduct().getProductId()==(productId))
                return item;
        }
        return null;
    }

    public void removeItemFromCart(SelectedItem item) {
        this.cart.remove(item);
    }

    public void setDiscountForCustomer(Discount discount) {
        this.allDiscountsForCustomer.put(discount, discount.getRepetitionForEachUser());
    }


    public long getCartPrice() {
        updateCart();
        long sum = 0;
        for (SelectedItem item : cart) {
            sum += item.getItemTotalPrice();
        }
        return sum;
    }

    public static boolean isThereCustomerWithUsername(String username){
        for (Customer customer : allCustomers) {
            if (customer.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public static Customer getCustomerById(int id){
        for (Customer customer : allCustomers) {
            if (customer.userId == id){
                return customer;
            }
        }
        return null;
    }

    public Customer(int userId, String username, String firstname, String lastname,
                    String email, String phoneNumber, String password, Status status,long credit) {
        super(userId, username, firstname, lastname, email, phoneNumber, password, status);
        this.credit = credit;
        this.allLogs = new ArrayList<>();
        this.allDiscountsForCustomer = new HashMap<>();
        this.cart = new ArrayList<>();
    }

    public static void addToAllCustomers(Customer customer){
        allCustomers.add(customer);
    }

    public void addToCart(SelectedItem selectedItem){
        for (SelectedItem item : cart) {
            if(item.getProduct().equals(selectedItem.getProduct())){
                for (Seller seller : item.getSellers()) {
                    if(seller.equals(selectedItem.getSellers())){
                        item.increaseAmountFromSeller(seller,1);
                        return;
                    }
                }
                item.getSellers().addAll(selectedItem.getSellers());
                item.getCountFromEachSeller().add(1);
                return;
            }
        }
        cart.add(selectedItem);
    }

    public void updateCart(){
        ArrayList<SelectedItem> temp = new ArrayList<>();
        for (SelectedItem item : cart) {
            if(!Product.isThereProductWithId(item.getProduct().getProductId()))
                temp.add(item);
            else{
                item.updateSelectedItem();
                if(item.getSellers().size()==0)
                    temp.add(item);
            }
        }
        cart.removeAll(temp);
    }

    public HashMap<Discount, Integer> getAllDiscountsForCustomer() {
        return allDiscountsForCustomer;
    }

    @Override
    public String toString() {
        return "username: "+username+"\nfirstname: "+firstname+
                "\nlastname: "+lastname+"\nphone: "+phoneNumber+
                "\nemail: "+email+"\ncredit: "+credit+
                "\npassword: "+password;
    }
}
