package Repository;

import Models.*;

import java.util.HashSet;
import java.util.Set;

public class SaveProductField {
    private ProductionStatus status;
    private long price;
    private int offId;
    private int sellerId;
    private int supply;
    private Set<Integer> customerId;

    public SaveProductField(ProductField productField) {
        this.status = productField.getStatus();
        this.price = productField.getPrice();
        this.offId = productField.getSale().getOffId();
        this.sellerId = productField.getSeller().getUserId();
        this.supply = productField.getSupply();
        productField.getAllBuyers().forEach(buyer -> customerId.add(buyer.getUserId()));
    }

}
