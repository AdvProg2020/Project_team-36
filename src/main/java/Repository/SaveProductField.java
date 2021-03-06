package Repository;

import Models.ProductField;
import Models.Status;

import java.util.HashSet;
import java.util.Set;

public class SaveProductField {
    private int mainProductId;
    private Status status;
    private long price;
    private int offId;
    private int sellerId;
    private int supply;
    private boolean isInAuction;
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
        this.customerIds = new HashSet<>();
        productField.getAllBuyers().forEach(buyer -> customerIds.add(buyer.getUserId()));
        this.isInAuction = productField.isInAuction();
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

        productField.setInAuction(this.isInAuction);
        return productField;
    }

    public int getMainProductId() {
        return mainProductId;
    }

    public Status getStatus() {
        return status;
    }

    public long getPrice() {
        return price;
    }

    public int getOffId() {
        return offId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getSupply() {
        return supply;
    }

    public Set<Integer> getCustomerIds() {
        return customerIds;
    }

    public boolean isInAuction() {
        return isInAuction;
    }
}
