package Repository;

import Models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveCustomer {
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
    private List<Integer> allCustomerLogIds;
    private List<SaveSelectedItem> cart;
    private Map<Integer, Integer> allDiscountsForCustomer;

    private SaveCustomer() {
        this.allCustomerLogIds = new ArrayList<>();
        this.allDiscountsForCustomer = new HashMap<>();
    }

    public static Customer load(int id){
        return null;
    }
}
