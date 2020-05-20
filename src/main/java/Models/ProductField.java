package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class ProductField implements Pendable {
    private ProductionStatus status;
    private long price;
    private Sale sale;
    private Seller seller;
    private int supply;
    private HashSet<Customer> allBuyers;

    public ProductionStatus getStatus() {
        return status;
    }

    public long getCurrentPrice() {
        if(this.sale == null||!sale.isSaleAvailable()){
            return price;
        }else{
            return price - (long) (price*sale.getSalePercent());
        }
    }

    public long getOfficialPrice(){
        return this.price;
    }

    public Sale getSale() {
        return sale;
    }

    public long getPrice() {
        return price;
    }

    public Seller getSeller() {
        return seller;
    }

    public int getSupply() {
        return supply;
    }

    public HashSet<Customer> getAllBuyers() {
        return allBuyers;
    }

    public void addBuyer(Customer buyer){
        this.allBuyers.add(buyer);
    }

    public void buyFromSeller(int count){
        this.supply -= count;
    }

    public void increaseSupply(int amount){
        this.supply +=amount;
    }

    public ProductField(ProductionStatus status, long price, Sale sale, Seller seller, int supply) {
        this.status = status;
        this.price = price;
        this.sale = sale;
        this.seller = seller;
        this.supply = supply;
        this.allBuyers = new HashSet<>();
    }

    @Override
    public String getPendingRequestType() {
        return null;
    }

    //-..-
}
