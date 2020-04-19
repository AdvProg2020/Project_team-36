package Models;

import java.util.ArrayList;

public class Item implements Packable{
    private Product product;
    private String productId;
    private int count;
    private User user;
    private long initialPrice;
    private static ArrayList<Item> allItems;

    public Product getProduct() {
        return product;
    }

    public String getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }

    public User getUser() {
        return user;
    }

    public long getInitialPrice() {
        return initialPrice;
    }


    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
