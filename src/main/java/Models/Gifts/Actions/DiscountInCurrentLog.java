package Models.Gifts.Actions;

import Models.WaitingLog;

public class DiscountInCurrentLog implements Action {
    private double discountPercent;//be darsad nist
    private long discountLimit;

    @Override
    public void action(WaitingLog waitingLog) {
        long giftDiscount;
        if(waitingLog.getTotalPrice()*discountPercent>discountLimit)
            giftDiscount = this.discountLimit;
        else
            giftDiscount = (long)(waitingLog.getTotalPrice()*discountPercent);
        waitingLog.addGiftDiscount(giftDiscount);
    }

    @Override
    public String toString() {
        return "add discount to this log"+ "\ndiscount percent: "+this.discountPercent*100;
    }


}
