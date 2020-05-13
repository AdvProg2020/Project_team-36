package Models;

import java.util.ArrayList;

public class OptionalFilter implements Filter {
    private int filterType;
    private ArrayList<String> options;

    public ArrayList<Product> filter(ArrayList<Product> products) {
        return null;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int getFilterType() {
        return filterType;
    }

    //-..-
}
