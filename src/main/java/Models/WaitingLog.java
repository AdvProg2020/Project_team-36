package Models;

import Models.Gifts.Gift;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class WaitingLog {
    private long totalPrice;
    private ArrayList<Gift> gifts;
    private Long giftDiscount;
    private ArrayList<SelectedItem> allSelectedItems;
    private Customer customer;
    private Discount discount;
    private String customerAddress;
    private String customerPhoneNumber;

    public WaitingLog(Customer customer, String customerAddress) {
        this.allSelectedItems = new ArrayList<>();
        this.gifts = new ArrayList<>();
        this.customer = customer;
        this.customerAddress = customerAddress;
    }


    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }

    public void addGiftDiscount(Long giftDiscount) {
        this.giftDiscount += giftDiscount;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public long getPayablePrice(){
        long payable;
        if(discount==null)
            payable = totalPrice - giftDiscount;
        else{
            payable =  this.discount.getPayableAfterDiscount(totalPrice)-giftDiscount;
        }
        return payable;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void addGift(Gift gift) {
        gifts.add(gift);
    }

    public ArrayList<Gift> getGiftDiscount() {
        return this.gifts;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public void setAllItems(ArrayList<SelectedItem> selectedItems){
        this.allSelectedItems.addAll(selectedItems);
        long sum = 0;
        for (SelectedItem item : allSelectedItems) {
            sum +=item.getTotalPrice();
        }
        this.totalPrice = sum;
    }

    public void setDiscount(Discount discount) {
        if(this.discount!=null){
            this.customer.increaseDiscountCode(discount,1);
        }
        this.customer.decreaseDiscountCode(discount,1);
        this.discount = discount;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void removeDiscount(){
        this.customer.increaseDiscountCode(this.discount,1);
    }


    public ArrayList<SelectedItem> getAllItems() {
        return allSelectedItems;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
