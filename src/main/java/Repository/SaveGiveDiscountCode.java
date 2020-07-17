package Repository;

import Models.Gifts.Actions.GiveDiscountCode;

public class SaveGiveDiscountCode {
    private int discountId;

    public SaveGiveDiscountCode(GiveDiscountCode giveDiscountCode) {
        this.discountId = giveDiscountCode.getDiscount().getId();
    }

    public GiveDiscountCode generateGiveDiscountCode(){
        return new GiveDiscountCode(SaveDiscount.load(this.discountId));
    }

    public int getDiscountId() {
        return discountId;
    }
}
