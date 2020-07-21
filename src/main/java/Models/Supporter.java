package Models;

import java.util.ArrayList;
import java.util.List;

public class Supporter extends User{
    private static List<Supporter> allSupporters = new ArrayList<>();

    public Supporter(String username) {
        super(username);
    }

    public void addNewSupporter(Supporter supporter){
        allSupporters.add(supporter);
    }

    @Override
    public String getType() {
        return "supporter";
    }
}
