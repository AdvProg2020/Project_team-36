package Models;


import java.util.ArrayList;

public class ItemInLog implements Packable{
    private String productName;
    private int productId;
    private int count;
    private Seller seller;
    private double salePercent;
    private long initialPrice;//per product
    private long currentPrice;


    public ItemInLog(Product product, int count, Seller seller) {
        this.count = count;
        this.seller = seller;
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.salePercent = product.getProductFieldBySeller(seller).getSale().getSalePercent();
        this.initialPrice = product.getProductFieldBySeller(seller).getCurrentPrice();
        this.currentPrice = initialPrice-(long)(initialPrice*salePercent);
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

    public static ArrayList<ItemInLog> createItemInLog(ArrayList<SelectedItem> selectedItems){

        //TODO create
return null;

    }


    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
