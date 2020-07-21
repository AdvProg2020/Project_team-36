package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static Models.LogStatus.WAITING_TO_BE_SENT;

public class  CustomerLog {
    static Random random = new Random();
    private static int totalLogsMade = random.nextInt(4988 - 1000) + 1000;
    private String customerName;
    private Date date;
    private int id;
    private double discountPercent;//be darsad nist yani0.2 mishe 20 darsad masan
    private long discountAmount;
    private long giftDiscount;
    private String address;
    private String phoneNumber;
    private LogStatus logStatus;
    private long totalPrice;//bedune haraj va discount
    private long totalPayable;
    private ArrayList<ItemInLog> allItems;

    public CustomerLog(double discountPercent,long discountAmount,long giftDiscount,String address,String phoneNumber,ArrayList<ItemInLog> allItems,long totalPayable,long totalPrice){
        this.id = randomId();
        this.date = new Date();
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.giftDiscount = giftDiscount;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.allItems = new ArrayList<>();
        this.allItems.addAll(allItems);
        this.totalPayable = totalPayable;
        this.totalPrice = totalPrice;
        this.logStatus = WAITING_TO_BE_SENT;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setLogStatus(LogStatus logStatus) {
        this.logStatus = logStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public long getGiftDiscount() {
        return giftDiscount;
    }

    public int getId() {
        return id;
    }



    private int randomId(){
        totalLogsMade +=1;
        return totalLogsMade;
    }

    public Date getDate() {
        return date;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LogStatus getLogStatus() {
        return logStatus;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public long getTotalPayable() {
        return totalPayable;
    }

    public ArrayList<ItemInLog> getAllItems() {
        return allItems;
    }

    public static CustomerLog createCustomerLog(WaitingLog waitingLog){
        int totalPrice = 0;
        ArrayList <ItemInLog> itemsInLog = ItemInLog.createItemInLog(waitingLog.getAllItems());
        for (ItemInLog itemInLog : itemsInLog) {
            totalPrice +=(itemInLog.getInitialPrice()*itemInLog.getCount());
        }
        long payable = waitingLog.getPayablePrice();
        Discount discount = waitingLog.getDiscount();
        CustomerLog customerLog;
        if(discount!=null)
         customerLog = new CustomerLog(discount.getDiscountPercent(),waitingLog.getDiscountAmount(),
                waitingLog.getGiftDiscount(),waitingLog.getCustomerAddress(),waitingLog.getCustomerPhoneNumber(),itemsInLog,payable,totalPrice);
        else{
            customerLog = new CustomerLog(0,0,
                    waitingLog.getGiftDiscount(),waitingLog.getCustomerAddress(),waitingLog.getCustomerPhoneNumber(),itemsInLog,payable,totalPrice);
        }
        customerLog.setCustomerName(waitingLog.getCustomer().getUsername());
        waitingLog.getCustomer().addNewLog(customerLog);
        return customerLog;
    }

    public CustomerLog(Date date, int id, double discountPercent, long discountAmount, long giftDiscount, String address, String phoneNumber, LogStatus logStatus, long totalPrice, long totalPayable, ArrayList<ItemInLog> allItems) {
        this.date = date;
        this.id = id;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.giftDiscount = giftDiscount;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.logStatus = logStatus;
        this.totalPrice = totalPrice;
        this.totalPayable = totalPayable;
        this.allItems = allItems;
    }

    @Override
    public String toString() {
        String result;
        result = "id: "+id+"\ndate: "+date;
        int i = 1;
        result+="\nproduct name   count   sale percent   each price";
        for (ItemInLog item : allItems) {
            result+="\n"+i+". "+item.getProductName()+"   "+item.getCount()+"   "+"   "+item.getSalePercent()*100+"   "+item.getCurrentPrice();
        }
        result+="discount amount: "+getDiscountAmount()+"\ngift amount: "+giftDiscount;
        return result;
    }
}
