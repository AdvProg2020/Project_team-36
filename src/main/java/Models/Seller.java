package Models;

import java.util.ArrayList;

public class Seller extends User implements Pendable {
    private static ArrayList<Seller> allSellers = new ArrayList<>();
    private ArrayList<SellerLog> allLogs;
    private ArrayList<Product> allProducts;
    private ArrayList<Sale> allSales;
    private long credit;
    private String companyName;
    private String companyInfo;
    private Wallet wallet;

    public Seller(String username) {
        super(username);
        this.allLogs = new ArrayList<>();
        this.allSales = new ArrayList<>();
        this.allProducts = new ArrayList<>();
        this.wallet = new Wallet();
    }

    @Override
    public String getType() {
        return "seller";
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public boolean sellerHasTheOff(int id){
        for (Sale sale : allSales) {
            if(sale.getOffId()==id){
                return true;
            }
        }
        return false;
    }

    public Sale getSaleWithId(int id){
        for (Sale sale : allSales) {
            if(sale.getOffId()==id){
                return sale;
            }
        }
        return null;
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

    public void addNewLog(SellerLog sellerLog) {
        this.allLogs.add(sellerLog);
    }

    public void increaseCredit(long amount) {
        this.credit += amount;
    }

    public void removeProduct(Product product) {
        allProducts.remove(product);
    }

    public void addProduct(Product product){
        allProducts.add(product);
    }

    public boolean isThereProduct(int productId){
        for (Product product : allProducts) {
            if (product.getProductId() == productId)
                return true;
        }
        return false;
    }

    public void addSale(Sale sale){
        allSales.add(sale);
    }

    public void removeSale(Sale sale){
        this.allSales.remove(sale);
    }

    @Override
    public String toString() {
        return "    username: " + username + '\n' +
                "    firstname: " + firstname + '\n' +
                "    lastname: " + lastname + '\n' +
                "    email: " + email + '\n' +
                "    phoneNumber: " + phoneNumber + '\n' +
                "    password: " + password + '\n' +
                "    company: " + companyName + '\n' +
                "    company info: : " + companyInfo + '\n'
                ;
    }

    public static Seller getSellerById(int id){
        for (Seller seller : allSellers) {
            if (seller.userId == id){
                return seller;
            }
        }
        return null;
    }

    public Seller(int userId, String username, String firstname, String lastname, String email, String phoneNumber,
                  String password, Status status, long credit, String companyName, String companyInfo,String
                   profilePictureURL) {
        super(userId, username, firstname, lastname, email, phoneNumber, password, status,profilePictureURL);
        this.credit = credit;
        this.companyName = companyName;
        this.companyInfo = companyInfo;

        this.allLogs = new ArrayList<>();
        this.allSales = new ArrayList<>();
        this.allProducts = new ArrayList<>();

    }

    public static void addToAllSellers(Seller seller){
        allSellers.add(seller);
    }

    @Override
    public void acceptAddRequest() {
        addNewUser(this);
        allSellers.add(this);
    }

    @Override
    public void acceptEditRequest() { }

    public static void updateSellers(){
        ArrayList<Seller> toBeRemoved = new ArrayList<>();
        for (Seller seller : allSellers) {
            if(seller.getStatus().equals(Status.DELETED))
                toBeRemoved.add(seller);
        }
        allSellers.removeAll(toBeRemoved);
    }
}
