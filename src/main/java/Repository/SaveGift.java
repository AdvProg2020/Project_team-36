package Repository;

import Models.Gifts.Actions.Action;
import Models.Gifts.Events.Event;

public class SaveGift {
    private String name;
    private SaveDiscountInCurrentLog saveDiscountInCurrentLog;
    private SaveGiveDiscountCode saveGiveDiscountCode;
    private SaveFirstBuyEvent saveFirstBuyEvent;
    private SaveHighPriceEvent saveHighPriceEvent;
    private SavePeriodicEvent savePeriodicEvent;

    private SaveGift() {
    }
}
