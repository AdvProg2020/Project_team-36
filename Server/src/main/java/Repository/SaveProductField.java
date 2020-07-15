package Repository;

import Models.*;

import java.util.Set;

public class SaveProductField {
    private int mainProductId;
    private Status status;
    private long price;
    private int offId;
    private int sellerId;
    private int supply;
    private Set<Integer> customerIds;

    public SaveProductField(ProductField productField) {
        this.mainProductId = productField.getMainProductId();
        this.status = productField.getStatus();
        this.price = productField.getPrice();
        if (productField.getSale() != null){
            this.offId = productField.getSale().getOffId();
        }else {
            this.offId = -100000000;
        }
        this.sellerId = productField.getSeller().getUserId();
        this.supply = productField.getSupply();
        productField.getAllBuyers().forEach(buyer -> customerIds.add(buyer.getUserId()));
    }

    public ProductField generateProductField(){
        ProductField productField;
        if (offId == -100000000){
            productField = new ProductField(status,price,null,
                    SaveSeller.load(sellerId),supply,mainProductId);
        }else {
            productField = new ProductField(status,price,SaveSale.load(offId),
                    SaveSeller.load(sellerId),supply,mainProductId);
        }
        if (customerIds != null){
            customerIds.forEach(customerId -> productField.getAllBuyers().add(SaveCustomer.load(customerId)));
        }

        return productField;
    }
}
