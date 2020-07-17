package Repository;

import Models.Customer;
import Models.Status;
import Models.User;
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
    private String profilePictureURL;
    private static int lastId = 0;

    private long credit;
    private List<SaveCustomerLog> allCustomerLogs;
    private List<SaveSelectedItem> cart;
    private Map<Integer, Integer> allDiscountsForCustomer;

    private SaveCustomer() {
        this.allCustomerLogs = new ArrayList<>();
        this.allDiscountsForCustomer = new HashMap<>();
        this.cart = new ArrayList<>();
    }

    public SaveCustomer(Customer customer){
        this.allCustomerLogs = new ArrayList<>();
        this.allDiscountsForCustomer = new HashMap<>();
        this.cart = new ArrayList<>();
        this.profilePictureURL = customer.getProfilePictureUrl();
        this.userId = customer.getUserId();
        this.username = customer.getUsername();
        this.firstname = customer.getFirstname();
        this.lastname = customer.getLastname();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.password = customer.getPassword();
        this.status = customer.getStatus();
        this.credit = customer.getCredit();
        customer.getAllLogs().forEach(customerLog -> this.allCustomerLogs.add(new SaveCustomerLog(customerLog)));
        customer.getCart().forEach(selectedItem -> this.cart.add(new SaveSelectedItem(selectedItem)));
        if (customer.getAllDiscountsForCustomer() != null){
            customer.getAllDiscountsForCustomer().forEach((key,value) -> this.allDiscountsForCustomer.put(key.getId(),value));
        }
    }

    public static void save(Customer customer){
        SaveCustomer saveCustomer = new SaveCustomer(customer);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveCustomerGson = gson.toJson(saveCustomer);
        FileUtil.write(FileUtil.generateAddress(Customer.class.getName(),saveCustomer.userId),saveCustomerGson);
    }

    public static Customer load(int id){
        lastId = Math.max(lastId,id);
        Customer potentialCustomer = Customer.getCustomerById(id);
        if(potentialCustomer != null){
            return potentialCustomer;
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Customer.class.getName(),id));
        if(data == null){
            return null;
        }
        SaveCustomer saveCustomer = gson.fromJson(data,SaveCustomer.class);
        Customer customer = new Customer(saveCustomer.userId,saveCustomer.username,saveCustomer.firstname,
                saveCustomer.lastname,saveCustomer.email,saveCustomer.phoneNumber,saveCustomer.password,
                saveCustomer.status,saveCustomer.credit,saveCustomer.profilePictureURL);
        Customer.addToAllCustomers(customer);
        User.addToAllUsers(customer);
        saveCustomer.cart.forEach(saveSelectedItem -> customer.getCart().add(saveSelectedItem.generateSelectedItem()));
        saveCustomer.allCustomerLogs.forEach(customerlog -> customer.getAllLogs().add(customerlog.generateCustomerLog()));
        saveCustomer.allDiscountsForCustomer.forEach((key,value) -> customer.getAllDiscountsForCustomer().put(SaveDiscount.load(key),value));
        return customer;
    }

    public static int getLastId() {
        return lastId;
    }
}
