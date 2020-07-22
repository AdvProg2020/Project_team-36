package Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Supporter extends User{
    private static List<Supporter> allSupporters = new ArrayList<>();

    public Supporter(String username) {
        super(username);
    }

    public Supporter(int userId, String username, String firstname, String lastname, String email, String phoneNumber, String password, Status status, String profilePictureUrl) {
        super(userId,username,firstname,lastname,email,phoneNumber,password,status,profilePictureUrl);
    }

    public void addNewSupporter(Supporter supporter){
        allSupporters.add(supporter);
    }

    @Override
    public String getType() {
        return "supporter";
    }

    public static Supporter getSupporterById(int id){
        for (Supporter supporter : allSupporters) {
            if (supporter.userId == id){
                return supporter;
            }
        }
        return null;
    }

    public static void addToAllSupporter(Supporter supporter){
        allSupporters.add(supporter);
    }

    public static void updateAllSupporters(){
        Iterator<Supporter> iter  = allSupporters.iterator();
        while(iter.hasNext()){
            if(iter.next().getStatus().equals(Status.DELETED))
                iter.remove();
        }
    }

    public static List<Supporter> getAllSupporters(){
        return allSupporters;
    }
}
