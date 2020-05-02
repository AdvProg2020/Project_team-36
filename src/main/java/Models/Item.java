package Models;

import java.util.ArrayList;

public class Item implements Packable{
    private Product product;
    private String productName;
    private int productId;
    private String company;
    private int count;
    private Seller seller;
    private long initialPrice;//per product


    public Item(Product product, int count, Seller seller) {
        this.product = product;
        this.count = count;
        this.seller = seller;
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.company = product.getCompany();
        this.initialPrice = product.getProductFieldBySeller(seller).getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }

    public Seller getUser() {
        return seller;
    }

    public long getInitialPrice() {
        return initialPrice;
    }

    public String getCompany() {
        return company;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
