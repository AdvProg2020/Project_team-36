package Models;

import Repository.SaveSupporter;

public class Supporter extends User {

    public Supporter(SaveSupporter saveSupporter){
        super(saveSupporter);
    }

    @Override
    public String getType() {
        return "supporter";
    }
}
