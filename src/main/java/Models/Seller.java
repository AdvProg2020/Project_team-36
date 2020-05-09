package Models;

import java.util.ArrayList;

public class Seller extends User implements Pendable,Packable {
    private static ArrayList<Seller> allSellers = new ArrayList<>();
    private ArrayList<SellerLog> allLogs;
    private ArrayList<Product> allProducts;
    private ArrayList<Sale> allSales;
    private long credit;

    public Seller(String username){
        super(username);
        this.allLogs = new ArrayList<>();
        this.allSales = new ArrayList<>();
        this.allSales = new ArrayList<>();
        this.allProducts = new ArrayList<>();
    }

    @Override
    public String getType() {
        return "seller";
    }


    public static ArrayList<Seller> getAllSellers() {
        return allSellers;
    }

    public ArrayList<SellerLog> getAllLogs() {
        return allLogs;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public ArrayList<Sale> getAllSales() {
        return allSales;
    }

    public void addNewLog(SellerLog sellerLog){
        this.allLogs.add(sellerLog);
    }

    public void increaseCredit(long amount){
        this.credit += amount;
    }
}
