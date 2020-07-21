package Models;

import GUI.Constants;
import Network.Client;
import Repository.SaveCustomer;
import Repository.SaveDiscount;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer extends User {
    private SaveCustomer saveCustomer;
    private long credit;
    private List<CustomerLog> allLogs;
    private WaitingLog waitingLog;
    private List<SelectedItem> cart;

    public Customer(SaveCustomer saveCustomer) {
        super(saveCustomer);
        this.saveCustomer = saveCustomer;
        this.credit = saveCustomer.getCredit();
        this.cart = new ArrayList<>();
        saveCustomer.getCart().forEach(saveSelectedItem -> cart.add(new SelectedItem(saveSelectedItem)));
        this.allLogs = new ArrayList<>();
        saveCustomer.getAllCustomerLogs().forEach(saveCustomerLog -> allLogs.add(new CustomerLog(saveCustomerLog)));
        this.waitingLog = new WaitingLog(saveCustomer.getWaitingLog());
    }

    @Override
    public String getType() {
        return "customer";
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
        Map<Discount, Integer> allDiscounts = new HashMap<>();
        for (Integer id : saveCustomer.getAllDiscountsForCustomer().keySet()) {
            Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Discount");
            query.getMethodInputs().put("id", "" + id);
            Response response = Client.process(query);

            if (response.getReturnType().equals("Discount")){
                Gson gson = new Gson();
                SaveDiscount saveDiscount = gson.fromJson(response.getData(), SaveDiscount.class);
                allDiscounts.put(new Discount(saveDiscount),saveCustomer.getAllDiscountsForCustomer().get(id));
            }else {
                System.out.println(response);
            }

        }
        return allDiscounts;
    }
}
