package Bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankDatabase {

    private static BankDatabase bankDatabase = null;
    private Map<BankUser, Token> allTokens ;
    private  List<BankUser> allUsers ;

    private BankDatabase(){
        allTokens = new HashMap<>();
        allUsers = new ArrayList<>();
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

    public void addToken(BankUser bankUser,Token token){

    }

    public static class ThereIsUserException extends Exception{}

    public static class NoUserWithUsername extends Exception{}
}
