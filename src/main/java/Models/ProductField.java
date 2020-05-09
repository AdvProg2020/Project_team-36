package Models;

import java.util.ArrayList;
import java.util.Date;

public class ProductField {
    private ProductionStatus status;
    private long price;
    private Sale sale;
    private Seller seller;
    private int supply;
    private Date productionDate;
    private ArrayList<Customer> allBuyers;

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

    public ArrayList<Customer> getAllBuyers() {
        return allBuyers;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void buyFromSeller(int count){
        this.supply -= count;
    }

    public void increaseSupply(int amount){
        this.supply +=amount;
    }
}
