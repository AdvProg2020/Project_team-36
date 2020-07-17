package Repository;

import Models.WaitingLog;

import java.util.ArrayList;
import java.util.List;

public class SaveWaitingLog {
    private long totalPrice;
    private List<SaveGift> gifts;
    private long giftDiscount;
    private long discountAmount;
    private List<SaveSelectedItem> allSelectedItems;
    private int customerId;
    private int discountId;
    private String customerAddress;
    private String customerPhoneNumber;

    public SaveWaitingLog(WaitingLog waitingLog) {
        this.totalPrice = waitingLog.getTotalPrice();
        this.gifts = new ArrayList<>();
        waitingLog.getGifts().forEach(gift -> this.gifts.add(new SaveGift(gift)));
        this.giftDiscount = waitingLog.getGiftDiscount();
        this.discountAmount = waitingLog.getDiscountAmount();
        this.allSelectedItems = new ArrayList<>();
        waitingLog.getAllItems().forEach(selectedItem -> this.allSelectedItems.add(new SaveSelectedItem(selectedItem)));
        this.customerId = waitingLog.getCustomer().getUserId();
        this.discountId = waitingLog.getDiscount().getId();
        this.customerAddress = waitingLog.getCustomerAddress();
        this.customerPhoneNumber = waitingLog.getCustomerPhoneNumber();
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public List<SaveGift> getGifts() {
        return gifts;
    }

    public long getGiftDiscount() {
        return giftDiscount;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public List<SaveSelectedItem> getAllSelectedItems() {
        return allSelectedItems;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getDiscountId() {
        return discountId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }
}
