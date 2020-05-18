package Repository;

import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        this.cart = new ArrayList<>();
    }

    public static void save(Customer customer){
        SaveCustomer saveCustomer = new SaveCustomer();
        saveCustomer.userId = customer.getUserId();
        saveCustomer.username = customer.getUsername();
        saveCustomer.firstname = customer.getFirstname();
        saveCustomer.lastname = customer.getLastname();
        saveCustomer.email = customer.getEmail();
        saveCustomer.phoneNumber = customer.getPhoneNumber();
        saveCustomer.password = customer.getPassword();
        saveCustomer.status = customer.getStatus();
        saveCustomer.credit = customer.getCredit();
        customer.getAllLogs().forEach(customerLog -> saveCustomer.allCustomerLogIds.add(customerLog.getId()));
        customer.getCart().forEach(selectedItem -> saveCustomer.cart.add(new SaveSelectedItem(selectedItem)));
        customer.getAllDiscountsForCustomer().forEach((key,value) -> saveCustomer.allDiscountsForCustomer.put(key.getId(),value));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveCustomerGson = gson.toJson(saveCustomer);
        FileUtil.write(FileUtil.generateAddress(Customer.class.getName(),saveCustomer.userId),saveCustomerGson);
    }

    public static Customer load(int id){
        lastId = Math.max(lastId,id);
        if(Customer.getCustomerById(id) != null){
            return Customer.getCustomerById(id);
        }
        Gson gson = new Gson();
        SaveCustomer saveCustomer = gson.fromJson(FileUtil.read(FileUtil.generateAddress(Customer.class.getName(),id)),SaveCustomer.class);
        Customer customer = new Customer(saveCustomer.userId,saveCustomer.username,saveCustomer.firstname,
                saveCustomer.lastname,saveCustomer.email,saveCustomer.phoneNumber,saveCustomer.password,
                saveCustomer.status,saveCustomer.credit);
        Customer.addToAllCustomers(customer);
        saveCustomer.cart.forEach(saveSelectedItem -> customer.getCart().add(saveSelectedItem.generateSelectedItem()));
        saveCustomer.allCustomerLogIds.forEach(logId -> customer.getAllLogs().add(SaveCustomerLog.load(logId)));
        saveCustomer.allDiscountsForCustomer.forEach((key,value) -> customer.getAllDiscountsForCustomer().put(SaveDiscount.load(key),value));
        return customer;
    }
}
