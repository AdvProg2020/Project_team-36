package Repository;

import Models.*;
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
    private static int lastId = 0;

    private long credit;
    private String companyName;
    private String companyInfo;
    private List<Integer> allSellerLogIds;
    private List<Integer> allProductIds;
    private List<Integer> allOffIds;

    private SaveSeller() {
        this.allOffIds = new ArrayList<>();
        this.allProductIds = new ArrayList<>();
        this.allSellerLogIds = new ArrayList<>();
    }

    public static void save(Seller seller){
        SaveSeller saveSeller = new SaveSeller();
        saveSeller.userId = seller.getUserId();
        saveSeller.username = seller.getUsername();
        saveSeller.firstname = seller.getFirstname();
        saveSeller.lastname = seller.getLastname();
        saveSeller.email = seller.getEmail();
        saveSeller.phoneNumber = seller.getPhoneNumber();
        saveSeller.password = seller.getPassword();
        saveSeller.status = seller.getStatus();
        saveSeller.credit = seller.getCredit();
        saveSeller.companyInfo = seller.getCompanyInfo();
        saveSeller.companyName = seller.getCompanyName();
        seller.getAllLogs().forEach(sellerLog -> saveSeller.allSellerLogIds.add(sellerLog.getId()));
        seller.getAllProducts().forEach(product -> saveSeller.allProductIds.add(product.getProductId()));
        seller.getAllSales().forEach(sale -> saveSeller.allOffIds.add(sale.getOffId()));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveSellerGson = gson.toJson(saveSeller);
        FileUtil.write(FileUtil.generateAddress(Seller.class.getName(),saveSeller.userId),saveSellerGson);
    }

    public static Seller load(int id){
        lastId = Math.max(lastId,id);
        if(Seller.getSellerById(id) != null){
            return Seller.getSellerById(id);
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Seller.class.getName(),id));
        if(data == null){
            return  null;
        }
        SaveSeller saveSeller = gson.fromJson(data,SaveSeller.class);
        Seller seller = new Seller(saveSeller.userId,saveSeller.username,saveSeller.firstname,
                saveSeller.lastname,saveSeller.email,saveSeller.phoneNumber,saveSeller.password,
                saveSeller.status,saveSeller.credit,saveSeller.companyName,saveSeller.companyInfo);
        Seller.addToAllSellers(seller);
        saveSeller.allOffIds.forEach(offId -> seller.getAllSales().add(SaveSale.load(offId)));
        saveSeller.allProductIds.forEach(productId -> seller.getAllProducts().add(SaveProduct.load(productId)));
        saveSeller.allSellerLogIds.forEach(sellerLogId -> seller.getAllLogs().add(SaveSellerLog.load(sellerLogId)));
        return seller;
    }
}
