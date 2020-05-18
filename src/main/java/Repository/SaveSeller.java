package Repository;

import Models.*;

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

    public static Seller load(int id){
        return null;
    }
}
