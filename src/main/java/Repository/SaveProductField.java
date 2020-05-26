package Repository;

import Models.*;

import java.util.HashSet;
import java.util.Set;

public class SaveProductField {
    private Status status;
    private long price;
    private int offId;
    private int sellerId;
    private int supply;
    private Set<Integer> customerIds;

    public SaveProductField(ProductField productField) {
        this.status = productField.getStatus();
        this.price = productField.getPrice();
        this.offId = productField.getSale().getOffId();
        this.sellerId = productField.getSeller().getUserId();
        this.supply = productField.getSupply();
        productField.getAllBuyers().forEach(buyer -> customerIds.add(buyer.getUserId()));
    }

    public ProductField generateProductField(){
        ProductField productField = new ProductField(status,price,SaveSale.load(offId),
                SaveSeller.load(sellerId),supply);
        customerIds.forEach(customerId -> productField.getAllBuyers().add(SaveCustomer.load(customerId)));
        return productField;
    }
}
