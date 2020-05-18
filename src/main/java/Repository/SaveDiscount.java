package Repository;

import Models.Customer;
import Models.Discount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveDiscount {
    private int id;
    private Date startTime;
    private Date endTime;
    private double discountPercent;//bar hasbe darsad nist
    private long discountLimit;
    private int repetitionForEachUser;
    private static int lastId = 0;
    private List<Integer> customersIncludedIds;

    private SaveDiscount() {
        this.customersIncludedIds = new ArrayList<>();
    }

    public static Discount load(int id){
        return null;
    }
}
