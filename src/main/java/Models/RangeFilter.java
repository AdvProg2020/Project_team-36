package Models;

import java.util.ArrayList;

public class RangeFilter implements Filter{
    private int filterType;
    private Long min;
    private Long max;

    public int getFilterType() {
        return filterType;
    }

    public Long getMax() {
        return max;
    }

    public Long getMin() {
        return min;
    }

    public ArrayList<Product> filter(ArrayList<Product> products) {
        return null;
    }
}
