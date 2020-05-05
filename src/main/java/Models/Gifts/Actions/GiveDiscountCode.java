package Models.Gifts.Actions;

import Models.Customer;
import Models.Discount;
import Models.WaitingLog;

public class GiveDiscountCode implements Action{
    private Discount discount;

    @Override
    public void action(WaitingLog waitingLog) {
        Customer customer = waitingLog.getCustomer();
        customer.setDiscountForCustomer(discount);
    }
}
