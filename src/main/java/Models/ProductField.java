package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class ProductField {
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

    @Override
    public String toString() {
        String result = "Seller: "+seller.getUsername();
        result+="\nOfficial price: "+price;
        if(sale!= null&& sale.isSaleAvailable())
            result+="\n**This is on SALE**\nCurrent price: "+getCurrentPrice();
        if(supply==0)
            result+="\n not enough supply!";
        else
            result+="\nSupply: "+supply;
        return result;
    }

    public void updateProductField(){
        HashSet<Customer> toBeRemoved = new HashSet<>();
        for (Customer buyer : allBuyers) {
            if(buyer.getStatus().equals(Status.DELETED))
                toBeRemoved.add(buyer);
        }
        allBuyers.removeAll(toBeRemoved);
        if(sale.getEndTime().before(new Date()))
            sale = null;
    }
}
