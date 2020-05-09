package Models;

import java.util.ArrayList;
import java.util.Date;

public class SellerLog {
    private static ArrayList<CustomerLog> allLogs;
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


    public static void createSellerLog(WaitingLog waitingLog){
        //TODO create seller logs and add them to their arrayLists
    }
}
