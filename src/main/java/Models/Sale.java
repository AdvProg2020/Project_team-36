package Models;

import java.util.ArrayList;
import java.util.Date;

import static Models.ProductionStatus.TO_BE;

public class Sale implements Pendable {
    private Seller seller;
    private int offId;
    private ArrayList<Product> productsInSale;
    private ProductionStatus status;
    private Date startTime;
    private Date endTime;
    private Double salePercent;//be darsad nist
    private static int totalOffsMade = 0;


    public Sale(Seller seller, ArrayList<Product> productsInSale, Date startTime, Date endTime, Double salePercent) {
        this.productsInSale = new ArrayList<>();
        this.seller = seller;
        this.offId = getRandomId();
        this.productsInSale = productsInSale;
        this.status = TO_BE;
        this.startTime = startTime;
        this.endTime = endTime;
        this.salePercent = salePercent;
    }


    public Sale(Sale sale){
        this.productsInSale = new ArrayList<>();
        this.seller = sale.seller;
        this.offId = sale.offId;
        this.productsInSale = sale.productsInSale;
        this.status = sale.status;
        this.startTime = sale.startTime;
        this.endTime = sale.endTime;
        this.salePercent = sale.salePercent;
    }


    private int getRandomId(){
        totalOffsMade+=1;
        return totalOffsMade;
    }

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

    public void setProductsInSale(ArrayList<Product> productsInSale) {
        this.productsInSale = productsInSale;
    }

    public void removeProducts(ArrayList<Product> products){
        productsInSale.removeAll(products);
    }

    public void addProducts(ArrayList<Product> products){
        productsInSale.addAll(products);
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setSalePercent(Double salePercent) {
        this.salePercent = salePercent;
    }

    public boolean isThereProduct(Product wantedProduct){
        for (Product product : productsInSale) {
            if(product.equals(wantedProduct)){
                return true;
            }
        }
        return false;
    }



    //-..-
}
