package Repository;

import Models.Auction;
import Models.Customer;
import Models.ProductField;

import java.util.Date;

public class SaveAuction {
    private SaveProductField saveProductField;
    private long endDate;
    private long highestPrice;
    private int finalBuyerId;
    private int chatId;
    private int id;

    public SaveAuction(Auction auction){
        this.saveProductField = new SaveProductField(auction.getProductField());
        this.endDate = auction.getEndDate().getTime();
        if (auction.getFinalBuyer() != null){
            this.finalBuyerId = auction.getFinalBuyer().getUserId();
        }
        this.highestPrice = auction.getHighestPrice();
        this.chatId = auction.getChat().getId();
        this.id = auction.getId();
    }

    public int getChatId() {
        return chatId;
    }

    public SaveProductField getSaveProductField() {
        return saveProductField;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getHighestPrice() {
        return highestPrice;
    }

    public int getFinalBuyerId() {
        return finalBuyerId;
    }

    public int getId() {
        return id;
    }

    //todo age hesesh bud badan save o load mizarm
}
