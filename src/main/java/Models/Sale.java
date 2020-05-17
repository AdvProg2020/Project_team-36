package Models;

import java.util.ArrayList;
import java.util.Date;

public class Sale implements Pendable {
    private Seller seller;
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

    public Seller getSeller() {
        return seller;
    }

    public static Sale getSaleById(int id){
        for (Sale sale : allSales) {
            if (sale.offId == id){
                return sale;
            }
        }
        return null;
    }

    public Sale(Seller seller, int offId, ProductionStatus status, Date startTime, Date endTime, Double salePercent) {
        this.seller = seller;
        this.offId = offId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.salePercent = salePercent;
        this.productsInSale = new ArrayList<>();
    }

    public static void addToAllSales(Sale sale){
        allSales.add(sale);
    }

    public static void updateSales(){
        ArrayList<Sale> toBeRemoved = new ArrayList<>();
        ArrayList<Product> removingProducts= new ArrayList<>();
        for (Sale sale : allSales) {
            if(sale.getEndTime().before(new Date()))
                toBeRemoved.add(sale);
            else if(sale.seller.getStatus().equals(Status.DELETED))
                toBeRemoved.add(sale);
            else{
                for (Product product : sale.productsInSale) {
                    if(!Product.isThereProductWithId(product.getProductId())){
                        removingProducts.add(product);
                    }
                    else if(!product.isThereSeller(sale.seller)){
                        removingProducts.add(product);
                    }
                }
                if(removingProducts.size()==sale.productsInSale.size())
                    toBeRemoved.add(sale);
                sale.productsInSale.removeAll(removingProducts);
                removingProducts.clear();
            }
        }
        allSales.removeAll(toBeRemoved);
    }

    //-..-
}
