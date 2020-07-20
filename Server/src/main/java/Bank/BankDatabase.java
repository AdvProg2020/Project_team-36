package Bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankDatabase {

    private static BankDatabase bankDatabase = null;
    private Map<BankUser, Token> allTokens ;
    private  List<BankUser> allUsers ;
    private List<Transaction> allTransactions;

    private BankDatabase(){
        allTokens = new HashMap<>();
        allUsers = new ArrayList<>();
        allTransactions = new ArrayList<>();
    }

    public static BankDatabase getInstance (){
        if(bankDatabase==null)
            bankDatabase = new BankDatabase();
        return bankDatabase;
    }

    public synchronized void addNewUser(BankUser bankUser) throws ThereIsUserException {
        for (BankUser user : allUsers) {
            if(user.getUsername().equalsIgnoreCase(bankUser.getUsername()))
                throw new ThereIsUserException();
        }
        allUsers.add(bankUser);
    }

    public BankUser getUser(String username) throws NoUserWithUsername {
        for (BankUser user : allUsers) {
            if(user.getUsername().equals(username))
                return user;
        }
        throw new NoUserWithUsername();
    }

    public BankUser getUser(int accountId) throws NoUserWithID {
        for (BankUser user : allUsers) {
            if(user.getAccountId() == accountId)
                return user;
        }
        throw new NoUserWithID();
    }

    public void addToken(BankUser bankUser,Token token){
        allTokens.put(bankUser,token);
    }

    public BankUser validateToken(Token token) throws TokenExpired, InvalidToken {
        for (BankUser value : allTokens.keySet()) {
            if(allTokens.get(value).equals(token)){
                if(allTokens.get(value).isTokenExpired())
                    throw new TokenExpired();
                return value;
            }
        }
        throw new InvalidToken();
    }

    public boolean isThereAccount(int id){
        for (BankUser user : allUsers) {
            if(user.getAccountId()==id)
                return true;
        }
        return false;
    }

    public void addTransaction(Transaction transaction){
        allTransactions.add(transaction);
    }

    public static class ThereIsUserException extends Exception{}

    public static class NoUserWithUsername extends Exception{}

    public static class NoUserWithID extends Exception{}

    public static class TokenExpired extends Exception{}

    public static class InvalidToken extends Exception{}
}
