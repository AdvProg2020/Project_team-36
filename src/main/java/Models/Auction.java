package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Auction {
    private static List<Auction> allAuctions = new ArrayList<>();
    private ProductField productField;
    private Date endDate;
    private long highestPrice;
    private Customer finalBuyer=null;
    private Chat chat;

    public Auction(ProductField productField, Date endDate) {
        this.productField = productField;
        this.endDate = endDate;
        this.chat = new Chat();
    }

    public Chat getChat() {
        return chat;
    }

    public void setHighestPrice(long highestPrice) {
        this.highestPrice = highestPrice;
    }

    public void setFinalBuyer(Customer finalBuyer) {
        this.finalBuyer = finalBuyer;
    }

    public static List<Auction> getAllAuctions() {
        return allAuctions;
    }

    public ProductField getProductField() {
        return productField;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isAuctionExpired(){
        return !this.getEndDate().before(new Date());
    }

    public void action(){
        if(finalBuyer==null){
            return;
        }


    }

    public long getHighestPrice() {
        return highestPrice;
    }

    public Customer getFinalBuyer() {
        return finalBuyer;
    }

    public static void updateAllAuctions(){
        Iterator<Auction> iter = allAuctions.iterator();
        while(iter.hasNext()){
            Auction auction = iter.next();
            if(auction.isAuctionExpired()){
                iter.remove();
                auction.action();
            }
        }
    }

    public static void addNewAuction(Auction auction){
        allAuctions.add(auction);
    }
}
