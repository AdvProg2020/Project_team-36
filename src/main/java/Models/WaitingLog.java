package Models;

import Models.Gifts.Gift;

import java.util.ArrayList;


public class WaitingLog {
    private long totalPrice;
    private ArrayList<Gift> gifts;
    private Long giftDiscount;
    private long discountAmount;
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

    public long getDiscountAmount() {
        return discountAmount;
    }

    public long getGiftDiscount(){
       return  this.giftDiscount;
    }

    public void addGiftDiscount(Long giftDiscount) {
        this.giftDiscount += giftDiscount;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public Discount getDiscount() {
        return discount;
    }

    public long getPayablePrice() {
        long payable;
        if (discount == null)
            payable = totalPrice - giftDiscount;
        else {
            payable = this.discount.getPayableAfterDiscount(totalPrice) - giftDiscount;
        }
        return payable;
    }

    public void applyPurchaseChanges() {
        this.customer.decreaseCredit(this.getPayablePrice());
        for (SelectedItem item : allSelectedItems) {
            for (Seller seller : item.getSellers()) {
                long amount = item.getSellerTotalPrice(seller);
                seller.increaseCredit(amount);
                int count= item.getCountFromEachSeller().get(item.getSellers().indexOf(seller));
                item.getProduct().getProductFieldBySeller(seller).buyFromSeller(count);
            }
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void addGift(Gift gift) {
        gifts.add(gift);
    }

    public ArrayList<Gift> getGifts() {
        return this.gifts;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public void setAllItems(ArrayList<SelectedItem> selectedItems) {
        this.allSelectedItems.addAll(selectedItems);
        long sum = 0;
        for (SelectedItem item : allSelectedItems) {
            sum += item.getItemTotalPrice();
        }
        this.totalPrice = sum;
    }

    public void setDiscount(Discount discount) {
        if (this.discount != null) {
            this.customer.increaseDiscountCode(discount, 1);
        }
        this.customer.decreaseDiscountCode(discount, 1);
        this.discount = discount;
        this.discountAmount= discount.getPayableAfterDiscount(totalPrice);
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void removeDiscount() {
        this.customer.increaseDiscountCode(this.discount, 1);
    }

    public void addCustomerToBuyers(){
        for (SelectedItem item : allSelectedItems) {
            Product product = item.getProduct();
            for (Seller seller : item.getSellers()) {
                product.getProductFieldBySeller(seller).addBuyer(this.customer);
            }
        }
    }

    public ArrayList<SelectedItem> getAllItems() {
        return allSelectedItems;
    }


}