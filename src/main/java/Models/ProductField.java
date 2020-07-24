package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class ProductField implements Pendable {
    private int mainProductId;
    private Status status;
    private long price;
    private Sale sale;
    private Seller seller;
    private int supply;
    private HashSet<Customer> allBuyers;
    private String editedField;
    private boolean isInAuction;


    public ProductField(long price, Seller seller, int supply, int mainProductId) {
        this.allBuyers = new HashSet<>();
        this.price = price;
        this.seller = seller;
        this.supply = supply;
        this.mainProductId = mainProductId;
        this.isInAuction = false;
        status = Status.TO_BE_CONFIRMED;
    }

    public ProductField(ProductField productField) {
        this.mainProductId = productField.mainProductId;
        this.price = productField.price;
        this.seller = productField.seller;
        this.supply = productField.supply;
        this.isInAuction = productField.isInAuction;
    }

    public Status getStatus() {
        return status;
    }

    public long getCurrentPrice() {
        if (this.sale == null || !sale.isSaleAvailable()) {
            return price;
        } else {
            return price - (long) (price * sale.getSalePercent());
        }
    }

    public void setMainProductId(int mainProductId) {
        this.mainProductId = mainProductId;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setSupply(int supply) {
        this.supply = supply;
    }

    public long getOfficialPrice() {
        return this.price;
    }

    public Sale getSale() {
        updateProductField();
        return sale;
    }

    public long getPrice() {
        return price;
    }

    public Seller getSeller() {
        return seller;
    }

    public int getSupply() {
        return supply;
    }

    public HashSet<Customer> getAllBuyers() {
        updateAllBuyers();
        return allBuyers;
    }

    private void updateAllBuyers() {
        ArrayList<Customer> toBeDeleted = new ArrayList<>();
        for (Customer buyer : allBuyers) {
            if (buyer.getStatus().equals(Status.DELETED)) {
                toBeDeleted.add(buyer);
            }
        }
        allBuyers.removeAll(toBeDeleted);
    }

    public void addBuyer(Customer buyer) {
        this.allBuyers.add(buyer);
    }

    public void buyFromSeller(int count) {
        this.supply -= count;
    }

    public void increaseSupply(int amount) {
        this.supply += amount;
    }


    public ProductField(Status status, long price, Sale sale, Seller seller, int supply, int mainProductId) {
        this.status = status;
        this.price = price;
        this.sale = sale;
        this.seller = seller;
        this.supply = supply;
        this.mainProductId = mainProductId;
        this.allBuyers = new HashSet<>();
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public String getEditedField() {
        return editedField;
    }

    public void setEditedField(String editedField) {
        this.editedField = editedField;
    }

    @Override
    public String getPendingRequestType() {
        return "seller for a product";
    }

    @Override
    public void acceptAddRequest() {
        Product.updateAllProducts();
        if (Product.isThereProductWithId(mainProductId)) {
            Product.getProductWithId(mainProductId).addProductField(this);
            seller.addProduct(Product.getProductWithId(mainProductId));
        }
    }

    @Override
    public void acceptEditRequest() {
        Product.updateAllProducts();
        User.updateAllUsers();
        if ((!Product.isThereProductWithId(mainProductId)) || !User.isThereUsername(seller.getUsername())||seller.getStatus().equals(Status.DELETED)) {
            return;
        }
        Product mainProduct = Product.getProductWithId(this.mainProductId);
        ProductField mainProductField = mainProduct.getProductFieldBySeller(this.seller);
        mainProductField.setPrice(this.price);
        mainProductField.setSupply(this.supply);
    }

    @Override
    public String toString() {
        String result = "Seller: " + seller.getUsername();
        result += "\nOfficial price: " + price;
        if (sale != null && sale.isSaleAvailable())
            result += "\n**This is on SALE**\nCurrent price: " + getCurrentPrice();
        if (supply == 0)
            result += "\n not enough supply!";
        else
            result += "\nSupply: " + supply;
        return result;
    }

    public void updateProductField() {
        HashSet<Customer> toBeRemoved = new HashSet<>();
        for (Customer buyer : allBuyers) {
            if (buyer.getStatus().equals(Status.DELETED))
                toBeRemoved.add(buyer);
        }
        allBuyers.removeAll(toBeRemoved);
        if (sale != null && sale.getEndTime().before(new Date()))
            sale = null;
    }

    public int getMainProductId() {
        return mainProductId;
    }

    public long getProductFieldPriceOnSale() {
        if(sale!=null)
        return (long) ((1 - sale.getSalePercent()) * price);
        return price;
    }

    public void setInAuction(boolean isInAuction){
        this.isInAuction = isInAuction;
    }

    public boolean isInAuction() {
        return isInAuction;
    }
}
