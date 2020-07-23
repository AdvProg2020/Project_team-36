package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Auction {
    private static List<Auction> allAuctions = new ArrayList<>();
    private ProductField productField;
    private Date endDate;
    private long highestPrice;
    private Customer finalBuyer;

    public Auction(ProductField productField, Date endDate) {
        this.productField = productField;
        this.endDate = endDate;
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

    public long getHighestPrice() {
        return highestPrice;
    }

    public Customer getFinalBuyer() {
        return finalBuyer;
    }
}
