package Models.Gifts.Actions;

import Models.WaitingLog;

public class DiscountInCurrentLog implements Action {
    private double discountInLog;//be darsad nist
    private long discountLimit;

    @Override
    public void action(WaitingLog waitingLog) {
        long discount;
        if(waitingLog.getTotalPrice()*discountInLog>discountLimit)
            discount = this.discountLimit;
        else
            discount = (long)(waitingLog.getTotalPrice()*discountInLog);
        waitingLog.setTotalPrice(waitingLog.getTotalPrice()-discount);
    }


}
