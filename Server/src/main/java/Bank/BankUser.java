package Bank;

public class BankUser {
    private static int allAccountsMade=1000;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private int accountId;

    public BankUser(String username,String password,String firstname,String lastname){
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountId = generateId();
    }

    public int generateId(){
         allAccountsMade++;
         return allAccountsMade;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public int getAccountId() {
        return accountId;
    }

}
