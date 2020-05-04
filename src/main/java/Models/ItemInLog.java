package Models;



public class ItemInLog implements Packable{
    private String productName;
    private int productId;
    private String company;
    private int count;
    private Seller seller;
    private Sale sale;
    private long initialPrice;//per product


    public ItemInLog(Product product, int count, Seller seller) {
        this.count = count;
        this.seller = seller;
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.company = product.getCompany();
        this.initialPrice = product.getProductFieldBySeller(seller).getPrice();
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
