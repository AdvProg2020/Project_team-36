package Bank;

import java.util.ArrayList;
import java.util.List;

public class BankUser {
    private static int allAccountsMade=1000;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private int accountId;
    private List<Transaction> allTransactions ;
    private long balance;

    public BankUser(String username,String password,String firstname,String lastname){
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountId = generateId();
        this.allTransactions = new ArrayList<>();
    }

    public int generateId(){
         allAccountsMade++;
         return allAccountsMade;
    }

    public String getPassword() {
        return password;
    }

    public long getBalance() {
        return balance;
    }

    public String getUsername() {
        return username;
    }

    public int getAccountId() {
        return accountId;
    }

    public void addTransaction(Transaction transaction){
        allTransactions.add(transaction);
    }

    public List<Transaction> getAllTransactions(){
        return allTransactions;
    }

    public List<Transaction> getDeposits(){
        List<Transaction> temp = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getDestAccountId()==accountId){
                temp.add(transaction);
            }
        }
        return temp;
    }

    public List<Transaction> getWithdrawals(){
        List<Transaction> temp = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getSourceAccountId()==accountId){
                temp.add(transaction);
            }
        }
        return temp;
    }

    public Transaction getTransaction(String id) throws InvalidReceiptId {
        int transactionId;
        try{
             transactionId = Integer.parseInt(id);
        }catch (NumberFormatException numberFormatException){
            throw new InvalidReceiptId();
        }
        for (Transaction transaction : allTransactions) {
            if(transaction.getTransactionId()==transactionId)
                return transaction;
        }
        throw new InvalidReceiptId();

    }

    public void addMoney(long amount){
        balance +=amount;
    }

    public void withdrawMoney(long amount) throws NotEnoughMoney {
        if(amount<balance)
        balance -= amount;
        else throw new NotEnoughMoney();
    }

    public static class InvalidReceiptId extends Exception{}

    public static class NotEnoughMoney extends Exception{}


}
