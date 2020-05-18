package Repository;

import Models.Product;
import Models.ProductionStatus;
import Models.Sale;
import Models.Seller;

import java.util.ArrayList;
import java.util.Date;

public class SaveSale {
    private Seller seller;
    private int offId;
    private ArrayList<Product> productsInSale;
    private ProductionStatus status;
    private Date startTime;
    private Date endTime;
    private Double salePercent;//be darsad nist

    public static Sale load(int id){
        return null;
    }
}
