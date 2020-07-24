package Models;

public class Wallet {
    private long totalMoney;
    private long blockedMoney;
    private long availableMoney;
    private final int id;
    private static int totalWalletsMade = 0;
    private static long minimumAmount = 0;

    public Wallet() {
        this.totalMoney = 0;
        availableMoney = 0;
        blockedMoney = 0;
        this.id = (totalWalletsMade+=1);
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public long getBlockedMoney() {
        return blockedMoney;
    }

    public long getAvailableMoney() {
        return (availableMoney-minimumAmount);
    }

    public int getId() {
        return id;
    }

    public static long getMinimumAmount() {
        return minimumAmount;
    }

    public void chargeWallet(long totalMoney) {
        this.totalMoney+=totalMoney;
        availableMoney+=totalMoney;
    }

    public void withdrawMoney(long totalMoney){
        this.totalMoney-=totalMoney;
        availableMoney-=totalMoney;
    }

    public void blockMoney(long money){
        availableMoney-=money;
        blockedMoney+=money;
    }

    public void unblockMoney(long money){
        availableMoney+=money;
        blockedMoney-=money;
    }

    public boolean isThereEnoughMoneyAvailable(long money){
        return (availableMoney-money)>=minimumAmount;
    }

    public static void setMinimumAmount(long minimumAmount) {
        Wallet.minimumAmount= minimumAmount;
    }

}
