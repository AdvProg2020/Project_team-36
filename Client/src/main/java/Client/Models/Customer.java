package Client.Models;

import Client.GUI.Constants;
import Client.Network.Client;
import Models.Query;
import Models.Response;
import Models.Wallet;
import Repository.SaveChat;
import Repository.SaveCustomer;
import Repository.SaveDiscount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    private Wallet wallet;

    public Customer(SaveCustomer saveCustomer) {
        super(saveCustomer);
        this.saveCustomer = saveCustomer;
        this.credit = saveCustomer.getCredit();
        this.wallet = saveCustomer.getWallet();
        this.cart = new ArrayList<>();
        saveCustomer.getCart().forEach(saveSelectedItem -> cart.add(new SelectedItem(saveSelectedItem)));
        this.allLogs = new ArrayList<>();
        saveCustomer.getAllCustomerLogs().forEach(saveCustomerLog -> allLogs.add(new CustomerLog(saveCustomerLog)));
        if (saveCustomer.getWaitingLog() != null){
            this.waitingLog = new WaitingLog(saveCustomer.getWaitingLog());
        }
    }

    public List<Auction> getWinningAuctions() {
//        return winningAuctions;
        return null;
        //todo
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

    public Wallet getWallet() {
        return wallet;
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

    @Override
    public ArrayList<Chat> getChats() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Chat");
        this.saveCustomer.getChatsIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Chat>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveChat>>(){}.getType();
            List<SaveChat> allSaveChats = gson.fromJson(response.getData(),type);
            ArrayList<Chat> allChats = new ArrayList<>();
            allSaveChats.forEach(saveChat -> allChats.add(new Chat(saveChat)));
            return allChats;
        }else {
            System.out.println(response);
            return null;
        }
    }
}
