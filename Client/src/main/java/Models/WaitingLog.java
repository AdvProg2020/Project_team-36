package Models;

import GUI.Constants;
import Models.Gifts.Gift;
import Network.Client;
import Repository.SaveCategory;
import Repository.SaveCustomer;
import Repository.SaveDiscount;
import Repository.SaveWaitingLog;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

public class WaitingLog {
    private SaveWaitingLog saveWaitingLog;
    private long totalPrice;
    private List<Gift> gifts;
    private long giftDiscount;
    private long discountAmount;
    private List<SelectedItem> allSelectedItems;
    private String customerAddress;
    private String customerPhoneNumber;

    public WaitingLog(SaveWaitingLog saveWaitingLog){
        this.saveWaitingLog = saveWaitingLog;
        this.totalPrice = saveWaitingLog.getTotalPrice();
        this.giftDiscount = saveWaitingLog.getGiftDiscount();
        this.discountAmount = saveWaitingLog.getDiscountAmount();
        this.customerAddress = saveWaitingLog.getCustomerAddress();
        this.customerPhoneNumber = saveWaitingLog.getCustomerPhoneNumber();
        this.gifts = new ArrayList<>();
        this.allSelectedItems = new ArrayList<>();
        saveWaitingLog.getAllSelectedItems().forEach(saveSelectedItem -> this.allSelectedItems.add(new SelectedItem(saveSelectedItem)));
    }

    public SaveWaitingLog getSaveWaitingLog() {
        return saveWaitingLog;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public long getGiftDiscount() {
        return giftDiscount;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public List<SelectedItem> getAllSelectedItems() {
        return allSelectedItems;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public Customer getCustomer() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Customer");
        query.getMethodInputs().put("id", "" + saveWaitingLog.getCustomerId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Customer")) {
            Gson gson = new Gson();
            SaveCustomer saveCustomer = gson.fromJson(response.getData(), SaveCustomer.class);
            return new Customer(saveCustomer);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public Discount getDiscount() {
        if(saveWaitingLog.getDiscountId()==-100000000)
            return null;
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Discount");
        query.getMethodInputs().put("id", "" + saveWaitingLog.getDiscountId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Discount")) {
            Gson gson = new Gson();
            SaveDiscount saveDiscount = gson.fromJson(response.getData(), SaveDiscount.class);
            return new Discount(saveDiscount);
        } else {
            System.out.println(response);
            return null;
        }
    }


}
