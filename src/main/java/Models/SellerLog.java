package Models;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class SellerLog {
    private static ArrayList<SellerLog> allLogs = new ArrayList<>();
    static Random random = new Random();
    private static int totalLogsMade = random.nextInt(4988 - 1000) + 1000;
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

    public Date getDate() {
        return date;
    }

    public long getSale() {
        return sale;
    }

    public long getTotalPrice() {
        return totalPrice;
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

    public static int getTotalLogsMade() {
        return totalLogsMade;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public LogStatus getLogStatus() {
        return logStatus;
    }

    public ArrayList<ItemInLog> getAllItems() {
        return allItems;
    }

    public int getId() {
        return id;
    }

    public SellerLog(Date date, int id, Customer customer, long sale, String customerAddress, String customerPhoneNumber, LogStatus logStatus,
                     long totalPrice, ArrayList<ItemInLog> allItems) {
        this.date = date;
        this.id = id;
        this.customer = customer;
        this.sale = sale;
        this.customerAddress = customerAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.logStatus = logStatus;
        this.totalPrice = totalPrice;
        this.allItems = allItems;
    }

    public StringBuilder getSellerLogInfo() {
        StringBuilder toBePrinted = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        toBePrinted.append("    log id: ").append(id).append("\n    date: ").append(formatter.format(date)).
                    append("\n    customer: ").append(customer.getUsername()).append("\n    customer address: ").
                    append(customerAddress).append("\n    customer phone number: ").append(customerPhoneNumber).
                    append("\n    sale: ").append(sale).append("\n    all items bought: ");
        for (ItemInLog item : allItems) {
            toBePrinted.append("\n        ").append(item.getProductId()).append("  ").append(item.getProductName());
        }
        toBePrinted.append("\n    total price: ").append(totalPrice);
        return toBePrinted;
    }

    public String getCustomerUsername(){
        return customer.getUsername();
    }

    public static SellerLog getSellerLogById(int id){
        for (SellerLog log : allLogs) {
            if (log.id == id){
                return log;
            }
        }
        return  null;
    }

}
