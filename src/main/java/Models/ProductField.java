package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class ProductField implements Pendable {
    private int mainProductId;
    private Status status;
    private long price;
    private Sale sale;
    private Seller seller;
    private int supply;
    private HashSet<Customer> allBuyers;


    public ProductField(long price, Seller seller, int supply, int mainProductId) {
        this.allBuyers = new HashSet<>();
        this.price = price;
        this.seller = seller;
        this.supply = supply;
        this.mainProductId = mainProductId ;
        status = Status.TO_BE_CONFIRMED;
    }

    public ProductField(ProductField productField) {
        this.mainProductId = productField.mainProductId;
        this.price = productField.price;
        this.seller = productField.seller;
        this.supply = productField.supply;
    }

    public Status getStatus() {
        return status;
    }

    public long getCurrentPrice() {
        if(this.sale == null||!sale.isSaleAvailable()){
            return price;
        }else{
            return price - (long) (price*sale.getSalePercent());
        }
    }

    public void setMainProductId(int mainProductId) {
        this.mainProductId = mainProductId;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setSupply(int supply) {
        this.supply = supply;
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
        updateAllBuyers();
        return allBuyers;
    }

    private void updateAllBuyers(){
        ArrayList<Customer> toBeDeleted = new ArrayList<>();
        for (Customer buyer : allBuyers) {
            if(buyer.getStatus().equals(Status.DELETED)){
                toBeDeleted.add(buyer);
            }
        }
        allBuyers.removeAll(toBeDeleted);
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
    public String getPendingRequestType() {
        return "seller for a product";
    }

    //-..-
}
