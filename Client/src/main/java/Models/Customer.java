package Models;

import Repository.SaveCustomer;
import Repository.SaveCustomerLog;
import Repository.SaveSelectedItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Customer extends User {
    private SaveCustomer saveCustomer;
    private long credit;
    private List<CustomerLog> allLogs;
    private WaitingLog waitingLog;
    private List<SelectedItem> cart;

    public Customer(SaveCustomer saveCustomer) {
        this.saveCustomer = saveCustomer;
        this.credit = saveCustomer.getCredit();
        this.cart = new ArrayList<>();
        saveCustomer.getCart().forEach(saveSelectedItem -> cart.add(new SelectedItem(saveSelectedItem)));
        this.allLogs = new ArrayList<>();
        saveCustomer.getAllCustomerLogs().forEach(saveCustomerLog -> allLogs.add(new CustomerLog(saveCustomerLog)));
        //todo waiting log
        //todo user fields
    }

    public SaveCustomer getSaveCustomer() {
        return saveCustomer;
    }

    public long getCredit() {
        return credit;
    }

    public List<CustomerLog> getAllLogs() {
        return allLogs;
    }

    public WaitingLog getWaitingLog() {
        return waitingLog;
    }

    public List<SelectedItem> getCart() {
        return cart;
    }

    public Map<Discount, Integer> getAllDiscountsForCustomer() {
        //todo map?
    }
}
