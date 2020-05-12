package Controllers;

import Models.Discount;
import Models.Gifts.Actions.DiscountInCurrentLog;
import Models.Gifts.Actions.GiveDiscountCode;
import Models.Gifts.Events.FirstBuyEvent;
import Models.Gifts.Events.HighPriceEvent;
import Models.Gifts.Events.PeriodicEvent;
import Models.Gifts.Gift;

import java.util.Date;

public class GiftController {
    String name;
    private Date startDate;
    private Date endDate;
    private long minimumLogPrice;
    private double discountPercent;//be darsad nist
    private long discountLimit;
    private Discount discount;

    public GiftController(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endTime) throws AlreadyPastDateException, EndBeforeStartDateException {
        Date now = new Date();
        if(endTime.before(now)){
            throw new AlreadyPastDateException("we are already past this date");
        }else if(endTime.before(startDate)){
            throw new EndBeforeStartDateException("termination time must be after start time");
        }
        else {
            this.endDate = endTime;
        }
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setDiscountLimit(long discountLimit) {
        this.discountLimit = discountLimit;
    }

    public void setMinimumLogPrice(long minimumLogPrice) {
        this.minimumLogPrice = minimumLogPrice;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public void newFirstBuyGiftWithDiscountCode(){
        new Gift(name,new GiveDiscountCode(discount),new FirstBuyEvent(startDate,endDate));
    }

    public void newFirstBuyGiftWithDiscountInLog(){
        new Gift(name,new DiscountInCurrentLog(discountPercent,discountLimit),new FirstBuyEvent(startDate,endDate));
    }

    public void newHighPriceGiftWithDiscountCode(){
        new Gift(name,new GiveDiscountCode(discount),new HighPriceEvent(startDate,endDate,minimumLogPrice));
    }

    public void newHighPriceGiftWithDiscountInLog(){
        new Gift(name,new DiscountInCurrentLog(discountPercent,discountLimit),new HighPriceEvent(startDate,endDate,minimumLogPrice));
    }

    public void newPeriodicGiftWithDiscountCode(){
        new Gift(name,new GiveDiscountCode(discount),new PeriodicEvent(startDate,endDate));
    }

    public void newPeriodicGiftWithDiscountInLog(){
        new Gift(name,new DiscountInCurrentLog(discountPercent,discountLimit),new PeriodicEvent(startDate,endDate));
    }

    public static class EndBeforeStartDateException extends Exception {
        public EndBeforeStartDateException(String message) {
            super(message);
        }
    }

    public static class AlreadyPastDateException extends Exception {
        public AlreadyPastDateException(String message) {
            super(message);
        }
    }
}
