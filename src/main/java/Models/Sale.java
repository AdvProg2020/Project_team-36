package Models;

import java.util.ArrayList;
import java.util.Date;

public class Sale implements Pendable {
    private static ArrayList<Sale> allSales = new ArrayList<>();
    private int offId;
    private ArrayList<Product> productsInSale;
    private ProductionStatus status;
    private Date startTime;
    private Date endTime;
    private Double salePercent;//be darsad nist

    public int getOffId() {
        return offId;
    }

    public ArrayList<Product> getProductsInSale() {
        return productsInSale;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public boolean isSaleAvailable(){
        Date now = new Date();
        return (now.after(this.startTime) && now.before(this.endTime)) || now.equals(this.startTime) || now.equals(this.endTime);

    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Double getSalePercent() {
        return salePercent;
    }

    @Override
    public String toString() {
        return  "    offId: " + offId + '\n' +
                "    startTime: " + startTime + '\n' +
                "    endTime: " + endTime + '\n' +
                "    salePercent: " + (salePercent*100) + '\n'
                ;
    }

    @Override
    public String getPendingRequestType() {
        return "sale";
    }

    //-..-
}
