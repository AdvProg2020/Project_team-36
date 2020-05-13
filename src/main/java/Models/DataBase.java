package Models;

import java.util.HashMap;
import java.util.List;

public class DataBase {
    private HashMap<String,String> fileAddress = new HashMap<String, String>();

    public String getAddress(String name){
        return fileAddress.get(name);
    }

    public List<?extends Packable> readJson(String path){
        return null;
    }

    public void writeJson(List<Object> objects){
    }

    //-..-
}
