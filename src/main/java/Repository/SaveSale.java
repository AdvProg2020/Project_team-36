package Repository;

import Models.ProductionStatus;
import Models.Sale;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveSale {
    private int  sellerId;
    private int offId;
    private List<Integer> productsInSaleIds;
    private ProductionStatus status;
    private long startTime;
    private long endTime;
    private Double salePercent;//be darsad nist

    public static void save(Sale sale){
        SaveSale saveSale = new SaveSale(sale);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveSaleGson = gson.toJson(saveSale);
        FileUtil.write(FileUtil.generateAddress(Sale.class.getName(),saveSale.offId),saveSaleGson);
    }

    public static Sale load(int id){
        Sale potentialSale = Sale.getSaleById(id);
        if(potentialSale != null){
            return potentialSale;
        }

        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Sale.class.getName(),id));
        if(data == null){
            return null;
        }
        SaveSale saveSale = gson.fromJson(data,SaveSale.class);
        Sale sale = new Sale(SaveSeller.load(saveSale.sellerId),saveSale.offId,
                saveSale.status,new Date(saveSale.startTime),new Date(saveSale.endTime),saveSale.salePercent);
        Sale.addToAllSales(sale);
        saveSale.productsInSaleIds.forEach(productInSaleId -> sale.getProductsInSale().add(SaveProduct.load(productInSaleId)));
        return sale;
    }

    public SaveSale(Sale sale) {
        this.sellerId = sale.getSeller().getUserId();
        this.offId = sale.getOffId();
        this.productsInSaleIds = new ArrayList<>();
        sale.getProductsInSale().forEach(product -> this.productsInSaleIds.add(product.getProductId()));
        this.status = sale.getStatus();
        this.startTime = sale.getStartTime().getTime();
        this.endTime = sale.getEndTime().getTime();
        this.salePercent = sale.getSalePercent();
    }

    public Sale generateSale(){
        Sale sale = new Sale(SaveSeller.load(this.sellerId),this.offId,
                this.status,new Date(this.startTime),new Date(this.endTime),this.salePercent);
        this.productsInSaleIds.forEach(productInSaleId -> sale.getProductsInSale().add(SaveProduct.load(productInSaleId)));
        return sale;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getOffId() {
        return offId;
    }

    public List<Integer> getProductsInSaleIds() {
        return productsInSaleIds;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public Double getSalePercent() {
        return salePercent;
    }
}
