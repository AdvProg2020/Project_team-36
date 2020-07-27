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
    private Customer finalBuyer = null;
    private Chat chat;
    private int id;
    private static int lastId;

    public Auction(ProductField productField, Date endDate) {
        this.productField = productField;
        this.endDate = endDate;
        this.chat = new Chat();
        lastId++;
        this.id = lastId;
    }

    public Auction(ProductField productField, Date endDate, long highestPrice, Customer finalBuyer, int id) {
        this.productField = productField;
        this.endDate = endDate;
        this.highestPrice = highestPrice;
        this.finalBuyer = finalBuyer;
        this.id = id;
    }

    public static Auction getAuctionById(int id){
        for (Auction auction : allAuctions) {
            if (auction.id == id){
                return auction;
            }
        }
        return null;
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
        updateAllAuctions();
        return allAuctions;
    }

    public ProductField getProductField() {
        return productField;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isAuctionExpired() {
        return !this.getEndDate().before(new Date());
    }

    public void action() {
        if (finalBuyer == null) {
            productField.setInAuction(false);
            return;
        }
        finalBuyer.addAuction(this);
    }

    public long getHighestPrice() {
        return highestPrice;
    }

    public Customer getFinalBuyer() {
        return finalBuyer;
    }

    public static void updateAllAuctions() {
        Iterator<Auction> iter = allAuctions.iterator();
        while (iter.hasNext()) {
            Auction auction = iter.next();
            if (auction.isAuctionExpired()) {
                iter.remove();
                auction.action();
            }
        }
    }

    public static void addNewAuction(Auction auction) {
        allAuctions.add(auction);
    }

    public int getId() {
        return id;
    }

    public static void addToAllAuctions(Auction auction){
        allAuctions.add(auction);
    }
}
