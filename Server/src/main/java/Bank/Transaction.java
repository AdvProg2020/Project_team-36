package Bank;


public class Transaction {

    private static int totalTransactionsMade=1000;
    private int transactionId;
    private String receiptType;
    private long money;
    private int sourceAccountId;
    private int destAccountId;
    private String description;
    private int paid;

    public Transaction(String receiptType, long money, int sourceAccountId, int destAccountId, String description) {
        this.transactionId = generateId();
        this.receiptType = receiptType;
        this.money = money;
        this.sourceAccountId = sourceAccountId;
        this.destAccountId = destAccountId;
        this.description = description;
        this.paid = 0;
    }

    public static int generateId(){
        totalTransactionsMade++;
        return totalTransactionsMade;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getDestAccountId() {
        return destAccountId;
    }

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public static class NotEnoughMoney extends Exception{

    }


}
