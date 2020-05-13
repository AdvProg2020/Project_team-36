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

    @Override
    public String getType() {
        return "seller";
    }

    @Override
    public String getPendingRequestType() {
        return "seller account";
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

    @Override
    public String toString() {
        return  "    username: " + username + '\n' +
                "    firstname: " + firstname + '\n' +
                "    lastname: " + lastname + '\n' +
                "    email: " + email + '\n' +
                "    phoneNumber: " + phoneNumber + '\n' +
                "    company: " + companyName + '\n'
                ;
    }

    //-..-
}
