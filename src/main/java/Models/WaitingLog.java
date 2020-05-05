package Models;

import java.util.ArrayList;


public class WaitingLog {
    private long totalPrice;
    private long payablePrice;
    private ArrayList<SelectedItem> allSelectedItems;
    private Customer customer;
    private Discount discount;
    private String customerAddress;
    private String customerPhoneNumber;

    public WaitingLog(Customer customer, String customerAddress) {
        allSelectedItems = new ArrayList<>();
        this.customer = customer;
        this.customerAddress = customerAddress;
    }


    public long getTotalPrice() {
        return totalPrice;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public void setAllItems(ArrayList<SelectedItem> selectedItems){
        this.allSelectedItems.addAll(selectedItems);
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
