package Models;

import java.util.ArrayList;

public class Seller extends User implements Pendable,Packable {
    private static ArrayList<Seller> allSellers = new ArrayList<Seller>();
    private ArrayList<Log> allLogs = new ArrayList<Log>();
    private ArrayList<Product> allProducts = new ArrayList<Product>();
    private ArrayList<Sale> allSales = new ArrayList<Sale>();

    public Seller(String username){
        super(username);
        this.allLogs = new ArrayList<Log>();
        this.allSales = new ArrayList<Sale>();
        this.allSales = new ArrayList<Sale>();
    }

    public static ArrayList<Seller> getAllSellers() {
        return allSellers;
    }

    public ArrayList<Log> getAllLogs() {
        return allLogs;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public ArrayList<Sale> getAllSales() {
        return allSales;
    }
}
