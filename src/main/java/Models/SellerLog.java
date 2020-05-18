package Models;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SellerLog {
    private static ArrayList<SellerLog> allLogs = new ArrayList<>();
    private static int totalLogsMade;
    private Date date;
    private int id;
    private Customer customer;
    private long sale;
    private String customerAddress;
    private String customerPhoneNumber;
    private LogStatus logStatus;
    private long totalPrice;
    private ArrayList<ItemInLog> allItems;


    public SellerLog(Customer customer,String customerAddress,String customerPhoneNumber,ArrayList<ItemInLog> allItems){
        this.date = new Date();
        this.id = randomId();
        this.customer = customer;
        this.customerAddress = customerAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.logStatus = LogStatus.WAITING_TO_BE_SENT;
        this.allItems = new ArrayList<>();
        this.allItems.addAll(allItems);
        allLogs.add(this);
        this.setPrices();
    }

    private int randomId() {
        totalLogsMade += 1;
        return totalLogsMade;
    }

    private void setPrices(){
        long totalPrice =0;
        long cash = 0;
        for (ItemInLog item : allItems) {
            int count = item.getCount();
            totalPrice += item.getInitialPrice()*count;
            cash+=item.getCurrentPrice();
        }
        this.totalPrice = totalPrice;
        this.sale = totalPrice - cash;
    }

    public static ArrayList<SellerLog> createSellerLogs(WaitingLog waitingLog) {
        ArrayList<SellerLog> logs = new ArrayList<>();
        HashMap<Seller, ArrayList<ItemInLog>> allItems = getSellersItems(waitingLog.getAllItems());
        for (Seller seller : allItems.keySet()) {
            ArrayList<ItemInLog> item = allItems.get(seller);
            SellerLog log = new SellerLog(waitingLog.getCustomer(),waitingLog.getCustomerAddress(),waitingLog.getCustomerPhoneNumber(),item);
            logs.add(log);
            seller.addNewLog(log);
        }
        return logs;
    }

    private static HashMap<Seller, ArrayList<ItemInLog>> getSellersItems(ArrayList<SelectedItem> selectedItems) {
        HashMap<Seller, ArrayList<ItemInLog>> allItemsForSellers = new HashMap<>();
        for (SelectedItem selectedItem : selectedItems) {
            for (Seller seller : selectedItem.getSellers()) {
                ItemInLog itemInLog = ItemInLog.createItemInLog(selectedItem, seller);
                if (allItemsForSellers.containsKey(seller))
                    allItemsForSellers.get(seller).add(itemInLog);
                else {
                    ArrayList<ItemInLog> temp = new ArrayList<>();
                    temp.add(itemInLog);
                    allItemsForSellers.put(seller, temp);
                }
            }
        }
        return allItemsForSellers;
    }

    public int getId() {
        return id;
    }
}
