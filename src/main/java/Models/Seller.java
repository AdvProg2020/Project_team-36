package Models;

import java.util.ArrayList;

public class Seller extends User implements Pendable,Packable {
    private static ArrayList<Seller> allSellers = new ArrayList<>();
    private ArrayList<SellerLog> allLogs;
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

    @Override
    public String getPendingRequestType() {
        return "seller account";
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
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

//    @Override
//    public String toString() {
//        return  "    username: " + username + '\n' +
//                "    firstname: " + firstname + '\n' +
//                "    lastname: " + lastname + '\n' +
//                "    email: " + email + '\n' +
//                "    phoneNumber: " + phoneNumber + '\n' +
//                "    company: " + companyName + '\n'
//                ;
//    }

    //-..-

    @Override
    public String toString() {
        return "Seller{" +
                "credit=" + credit +
                ", companyName='" + companyName + '\'' +
                ", companyInfo='" + companyInfo + '\'' +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
