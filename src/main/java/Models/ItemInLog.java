package Models;


import java.util.ArrayList;

public class ItemInLog {
    private String productName;
    private int productId;
    private int count;
    private String sellerUsername;
    private double salePercent;
    private long initialPrice;//per product
    private long currentPrice;


    public ItemInLog(Product product, int count, Seller seller) {
        this.count = count;
        this.sellerUsername = seller.getUsername();
        this.productId = product.getProductId();
        this.productName = product.getName();
        if (product.getProductFieldBySeller(seller).getSale().isSaleAvailable())
            this.salePercent = product.getProductFieldBySeller(seller).getSale().getSalePercent();
        this.initialPrice = product.getProductFieldBySeller(seller).getPrice();
        this.currentPrice = product.getProductFieldBySeller(seller).getCurrentPrice();
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public double getSalePercent() {
        return salePercent;
    }

    public long getCurrentPrice() {
        return currentPrice;
    }

    public String getSeller() {
        return sellerUsername;
    }



    public int getCount() {
        return count;
    }

    public long getInitialPrice() {
        return initialPrice;
    }

    public static ArrayList<ItemInLog> createItemInLog(ArrayList<SelectedItem> selectedItems) {
        ArrayList<ItemInLog> itemsInLog = new ArrayList<>();
        for (SelectedItem item : selectedItems) {
            for (Seller seller : item.getSellers()) {
                int index = item.getSellers().indexOf(seller);
                int count = item.getCountFromEachSeller().get(index);
                itemsInLog.add(new ItemInLog(item.getProduct(), count, seller));
            }
        }
        return itemsInLog;
    }

    public static ItemInLog createItemInLog(SelectedItem selectedItem, Seller seller){
        int index = selectedItem.getSellers().indexOf(seller);
        int count = selectedItem.getCountFromEachSeller().get(index);
        return new ItemInLog(selectedItem.getProduct(),count,seller);
    }

}
