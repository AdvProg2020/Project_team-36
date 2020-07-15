package Models.Gifts.Actions;

import Models.Customer;
import Models.Discount;
import Models.WaitingLog;

public class GiveDiscountCode implements Action {
    private Discount discount;

    public GiveDiscountCode(Discount discount) {
        this.discount = discount;
    }

    @Override
    public void action(WaitingLog waitingLog) {
        Customer customer = waitingLog.getCustomer();
        customer.setDiscountForCustomer(discount);
    }

    @Override
    public String toString() {
        return "add discount code to users codes" + "\ncode id: " + discount.getId() + "\ndiscountPercent: " +
                (discount.getDiscountPercent() * 100) + "%";
    }

    public Discount getDiscount() {
        return discount;
    }
}
