package Repository;

import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class SaveAuction {
    private SaveProductField saveProductField;
    private long endDate;
    private long highestPrice;
    private int finalBuyerId;
    private int chatId;
    private int id;
    private static int lastId = 0;

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

    public static void save(Auction auction){
        SaveAuction saveAuction = new SaveAuction(auction);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveAuctionString = gson.toJson(saveAuction);
        FileUtil.write(FileUtil.generateAddress(Auction.class.getName(),auction.getId()),saveAuctionString);
    }

    public static Auction load(int id){
        lastId = Math.max(lastId,id);
        Auction potentialAuction = Auction.getAuctionById(id);
        if(potentialAuction != null){
            return potentialAuction;
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Auction.class.getName(),id));
        if(data == null){
            return null;
        }
        SaveAuction saveAuction = gson.fromJson(data,SaveAuction.class);
        Auction auction = new Auction(saveAuction.saveProductField.generateProductField(),new Date(saveAuction.endDate),
                saveAuction.highestPrice,SaveCustomer.load(saveAuction.finalBuyerId),id);
        Auction.addToAllAuctions(auction);
        return auction;
    }
}
