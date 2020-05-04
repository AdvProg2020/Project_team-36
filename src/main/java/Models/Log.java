package Models;

import java.util.ArrayList;
import java.util.Date;


public class Log implements Packable {
    private int id;
    private Date date;
    private long totalPrice;
    private long payablePrice;
    private Sale sale;
    private ArrayList<Item> allItems;
    private User user;
    private LogStatus status;
    private double discountPercent;
    private Discount discount;
    private String customerAddress;
    private String customerPhoneNumber;
    private static ArrayList<Log> allLogs = new ArrayList<>();

    public Log(Customer customer, String customerAddress) {
        allItems = new ArrayList<>();
        this.user = customer;
        this.customerAddress = customerAddress;
        this.status = LogStatus.NOT_PAID;
        this.id = randomId();

    }



    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public void setAllItems(ArrayList<ItemInCart> itemInCart){
        for (ItemInCart inCart : itemInCart) {
            ArrayList<Seller> sellers = inCart.getSellers();
            ArrayList<Integer> count = inCart.getCountFromEachSeller();
            for(int i = 0;i<sellers.size();i++){
                allItems.add(new Item(inCart.getProduct(),count.get(i),sellers.get(i)));
            }
        }
    }

    public void setDiscount(Discount discount) {
        if(this.discount!=null){
            ((Customer)this.user).increaseDiscountCode(discount,1);
        }
        ((Customer)this.user).decreaseDiscountCode(discount,1);
        this.discount = discount;
        this.discountPercent = discount.getDiscountPercent();
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public Sale getSale() {
        return sale;
    }

    private int randomId() {
        if (allLogs.isEmpty())
            return 1;
        else {
            return allLogs.get(allLogs.size() - 1).getId() + 1;
        }


    }

    public ArrayList<Item> getAllItems() {
        return allItems;
    }

    public User getUser() {
        return user;
    }

    public LogStatus getStatus() {
        return status;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
