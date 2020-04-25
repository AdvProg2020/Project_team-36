package Models;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User implements Packable{
    private static ArrayList<Customer> allCustomers = new ArrayList<>();
    private long credit;
    private ArrayList<Log> allLogs;
    private ArrayList<ItemInCart> cart;
    private HashMap<Integer,Discount> allDiscountsForCustomer;

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

    public ArrayList<ItemInCart> getCart() {
        return cart;
    }

    public HashMap<Integer, Discount> getAllDiscountsForCustomer() {
        return allDiscountsForCustomer;
    }

    public boolean isThereProductInCart(int productId){
        for (ItemInCart item : cart) {
            if(item.getProduct().getProductId()==productId)
                return true;
        }
        return false;
    }

    public ItemInCart getProductInCart(int productId){
        for (ItemInCart item : cart) {
            if (item.getProduct().getProductId()==productId)
                return item;
        }
        return null;
    }

    public void removeItemFromCart(ItemInCart item){
        this.cart.remove(item);
    }

    public long getCartPrice(){
        long sum=0;
        for (ItemInCart item : cart) {
            sum+= item.getTotalPrice();
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
