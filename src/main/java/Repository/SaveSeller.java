package Repository;

import Models.Seller;
import Models.Status;
import Models.User;
import Models.Wallet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class SaveSeller {
    private int userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String password;
    private Status status;
    private String profilePictureURL;
    private List<Integer> chatsIds;
    private Wallet wallet;
    private static int lastId = 0;

    private long credit;
    private String companyName;
    private String companyInfo;
    private List<SaveSellerLog> allSellerLogs;
    private List<Integer> allProductIds;
    private List<Integer> allOffIds;

    public static void save(Seller seller){
        SaveSeller saveSeller = new SaveSeller(seller);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveSellerGson = gson.toJson(saveSeller);
        FileUtil.write(FileUtil.generateAddress(Seller.class.getName(),saveSeller.userId),saveSellerGson);
    }

    public static Seller load(int id){
        lastId = Math.max(lastId,id);
        Seller potentialSeller = Seller.getSellerById(id);
        if(potentialSeller != null){
            return potentialSeller;
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Seller.class.getName(),id));
        if(data == null){
            return  null;
        }
        SaveSeller saveSeller = gson.fromJson(data,SaveSeller.class);
        Seller seller = new Seller(saveSeller.userId,saveSeller.username,saveSeller.firstname,
                saveSeller.lastname,saveSeller.email,saveSeller.phoneNumber,saveSeller.password,
                saveSeller.status,saveSeller.credit,saveSeller.companyName,
                saveSeller.companyInfo,saveSeller.profilePictureURL);
        Seller.addToAllSellers(seller);
        User.addToAllUsers(seller);
        saveSeller.allOffIds.forEach(offId -> seller.getAllSales().add(SaveSale.load(offId)));
        saveSeller.allProductIds.forEach(productId -> seller.getAllProducts().add(SaveProduct.load(productId)));
        saveSeller.allSellerLogs.forEach(sellerLog -> seller.getAllLogs().add(sellerLog.generateSellerLog()));
        seller.setWallet(saveSeller.wallet);
        return seller;
    }

    public SaveSeller(Seller seller) {
        this.profilePictureURL = seller.getProfilePictureUrl();
        this.chatsIds = new ArrayList<>();
        this.allOffIds = new ArrayList<>();
        this.allProductIds = new ArrayList<>();
        this.allSellerLogs = new ArrayList<>();
        this.userId = seller.getUserId();
        this.username = seller.getUsername();
        this.firstname = seller.getFirstname();
        this.lastname = seller.getLastname();
        this.email = seller.getEmail();
        this.phoneNumber = seller.getPhoneNumber();
        this.password = seller.getPassword();
        this.status = seller.getStatus();
        this.credit = seller.getCredit();
        this.companyInfo = seller.getCompanyInfo();
        this.companyName = seller.getCompanyName();
        this.wallet = seller.getWallet();
        seller.getChats().forEach(chat -> chatsIds.add(chat.getId()));
        seller.getAllLogs().forEach(sellerLog -> this.allSellerLogs.add(new SaveSellerLog(sellerLog)));
        seller.getAllProducts().forEach(product -> this.allProductIds.add(product.getProductId()));
        seller.getAllSales().forEach(sale -> this.allOffIds.add(sale.getOffId()));
    }

    public Seller generateSeller(){
        Seller seller = new Seller(this.userId,this.username,this.firstname,
                this.lastname,this.email,this.phoneNumber,this.password,
                this.status,this.credit,this.companyName,this.companyInfo,this.profilePictureURL);
        this.allOffIds.forEach(offId -> seller.getAllSales().add(SaveSale.load(offId)));
        this.allProductIds.forEach(productId -> seller.getAllProducts().add(SaveProduct.load(productId)));
        this.allSellerLogs.forEach(sellerLog -> seller.getAllLogs().add(sellerLog.generateSellerLog()));
        seller.setWallet(this.wallet);
        return seller;
    }

    public static int getLastId() {
        return lastId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() {
        return status;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public long getCredit() {
        return credit;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    public List<SaveSellerLog> getAllSellerLogs() {
        return allSellerLogs;
    }

    public List<Integer> getChatsIds() {
        return chatsIds;
    }

    public List<Integer> getAllProductIds() {
        return allProductIds;
    }

    public List<Integer> getAllOffIds() {
        return allOffIds;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
