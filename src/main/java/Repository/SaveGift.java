package Repository;

import Models.Gifts.Actions.Action;
import Models.Gifts.Actions.DiscountInCurrentLog;
import Models.Gifts.Actions.GiveDiscountCode;
import Models.Gifts.Events.Event;
import Models.Gifts.Events.FirstBuyEvent;
import Models.Gifts.Events.HighPriceEvent;
import Models.Gifts.Events.PeriodicEvent;
import Models.Gifts.Gift;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveGift {
    private String name;
    private DiscountInCurrentLog discountInCurrentLog;
    private SaveGiveDiscountCode saveGiveDiscountCode;
    private FirstBuyEvent firstBuyEvent;
    private HighPriceEvent highPriceEvent;
    private PeriodicEvent periodicEvent;
    private static int id = 0;

    private SaveGift() {
    }

    public SaveGift(Gift gift){
        this.name = gift.getName();
        Action action = gift.getAction();
        if (action instanceof DiscountInCurrentLog) {
            this.discountInCurrentLog = (DiscountInCurrentLog) action;
        } else if (action instanceof GiveDiscountCode) {
            this.saveGiveDiscountCode = new SaveGiveDiscountCode((GiveDiscountCode) action);
        }

        Event event = gift.getEvent();
        if (event instanceof FirstBuyEvent) {
            this.firstBuyEvent = (FirstBuyEvent) event;
        } else if (event instanceof HighPriceEvent) {
            this.highPriceEvent = (HighPriceEvent) event;
        } else if (event instanceof PeriodicEvent) {
            this.periodicEvent = (PeriodicEvent) event;
        }
    }

    public Gift generateGift(){
        Action action = null;
        if (this.discountInCurrentLog != null){
            action = this.discountInCurrentLog;
        }else if (this.saveGiveDiscountCode != null){
            action = this.saveGiveDiscountCode.generateGiveDiscountCode();
        }

        Event event = null;
        if (this.firstBuyEvent != null){
            event = this.firstBuyEvent;
        }else if (this.highPriceEvent != null){
            event = this.highPriceEvent;
        }else if (this.periodicEvent != null){
            event = this.periodicEvent;
        }

        return new Gift(this.name,action,event);
    }

    public static void save(Gift gift) {
        SaveGift saveGift = new SaveGift(gift);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveGiftGson = gson.toJson(saveGift);
        id++;
        FileUtil.write(FileUtil.generateAddress(Gift.class.getName(), id), saveGiftGson);
    }

    public static Gift load(int id){
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Gift.class.getName(),id));
        if (data == null){
            return null;
        }
        SaveGift saveGift = gson.fromJson(data,SaveGift.class);

        Action action = null;
        if (saveGift.discountInCurrentLog != null){
            action = saveGift.discountInCurrentLog;
        }else if (saveGift.saveGiveDiscountCode != null){
            action = saveGift.saveGiveDiscountCode.generateGiveDiscountCode();
        }

        Event event = null;
        if (saveGift.firstBuyEvent != null){
            event = saveGift.firstBuyEvent;
        }else if (saveGift.highPriceEvent != null){
            event = saveGift.highPriceEvent;
        }else if (saveGift.periodicEvent != null){
            event = saveGift.periodicEvent;
        }

        return new Gift(saveGift.name,action,event);
    }

    public String getName() {
        return name;
    }

    public DiscountInCurrentLog getDiscountInCurrentLog() {
        return discountInCurrentLog;
    }

    public SaveGiveDiscountCode getSaveGiveDiscountCode() {
        return saveGiveDiscountCode;
    }

    public FirstBuyEvent getFirstBuyEvent() {
        return firstBuyEvent;
    }

    public HighPriceEvent getHighPriceEvent() {
        return highPriceEvent;
    }

    public PeriodicEvent getPeriodicEvent() {
        return periodicEvent;
    }

    public static int getId() {
        return id;
    }
}
