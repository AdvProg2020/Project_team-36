package Repository;

import Models.Product;
import Models.ProductionStatus;
import Models.Sale;
import Models.Seller;
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
    private Date startTime;
    private Date endTime;
    private Double salePercent;//be darsad nist

    private SaveSale() {
        productsInSaleIds = new ArrayList<>();
    }

    public static void save(Sale sale){
        SaveSale saveSale = new SaveSale();
        saveSale.sellerId = sale.getSeller().getUserId();
        saveSale.offId = sale.getOffId();
        sale.getProductsInSale().forEach(product -> saveSale.productsInSaleIds.add(product.getProductId()));
        saveSale.status = sale.getStatus();
        saveSale.startTime = sale.getStartTime();
        saveSale.endTime = sale.getEndTime();
        saveSale.salePercent = sale.getSalePercent();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveSaleGson = gson.toJson(saveSale);
        FileUtil.write(FileUtil.generateAddress(Sale.class.getName(),saveSale.offId),saveSaleGson);
    }

    public static Sale load(int id){
        if(Sale.getSaleById(id) != null){
            return Sale.getSaleById(id);
        }

        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Sale.class.getName(),id));
        if(data == null){
            return null;
        }
        SaveSale saveSale = gson.fromJson(data,SaveSale.class);
        Sale sale = new Sale(SaveSeller.load(saveSale.sellerId),saveSale.offId,
                saveSale.status,saveSale.startTime,saveSale.endTime,saveSale.salePercent);
        Sale.addToAllSales(sale);
        saveSale.productsInSaleIds.forEach(productInSaleId -> sale.getProductsInSale().add(SaveProduct.load(productInSaleId)));
        return sale;
    }

}
