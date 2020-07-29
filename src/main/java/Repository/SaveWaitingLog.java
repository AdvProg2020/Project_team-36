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
        if(waitingLog.getCustomer() != null){
            this.customerId = waitingLog.getCustomer().getUserId();
        }else {
            this.customerId = -100000000;
        }
        if (waitingLog.getDiscount() != null) {
            this.discountId = waitingLog.getDiscount().getId();
        } else {
            this.discountId = -100000000;
        }

        this.customerAddress = waitingLog.getCustomerAddress();
        this.customerPhoneNumber = waitingLog.getCustomerPhoneNumber();
    }

    public WaitingLog generateWaitingLog() {
        WaitingLog waitingLog = new WaitingLog(this.totalPrice, this.giftDiscount, this.discountAmount
                , this.customerAddress, this.customerPhoneNumber);
        this.gifts.forEach(saveGift -> waitingLog.getGifts().add(saveGift.generateGift()));
        this.allSelectedItems.forEach(saveSelectedItem -> waitingLog.getAllItems().add(saveSelectedItem.generateSelectedItem()));
        return waitingLog;
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

    @Override
    public String toString() {
        return "SaveWaitingLog{" +
                "totalPrice=" + totalPrice +
                ", gifts=" + gifts +
                ", giftDiscount=" + giftDiscount +
                ", discountAmount=" + discountAmount +
                ", allSelectedItems=" + allSelectedItems +
                ", customerId=" + customerId +
                ", discountId=" + discountId +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                '}';
    }
}
