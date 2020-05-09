package Models;

import java.util.ArrayList;

public class Seller extends User implements Pendable,Packable {
    private static ArrayList<Seller> allSellers = new ArrayList<>();
    private ArrayList<Log> allLogs;
    private ArrayList<Product> allProducts;
    private ArrayList<Sale> allSales;
    private long credit;
    private String companyName;
    private String companyInfo;

    public Seller(String username){
        super(username);
        this.allLogs = new ArrayList<>();
        this.allSales = new ArrayList<>();
        this.allSales = new ArrayList<>();
        this.allProducts = new ArrayList<>();
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    @Override
    public String getType() {
        return "seller";
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
