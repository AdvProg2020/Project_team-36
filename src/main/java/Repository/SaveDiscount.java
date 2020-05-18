package Repository;

import Models.Customer;
import Models.Discount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public static void save(Discount discount){
        SaveDiscount saveDiscount = new SaveDiscount();
        saveDiscount.id = discount.getId();
        saveDiscount.startTime = discount.getStartTime();
        saveDiscount.endTime = discount.getEndTime();
        saveDiscount.discountPercent = discount.getDiscountPercent();
        saveDiscount.repetitionForEachUser = discount.getRepetitionForEachUser();
        discount.getCustomersIncluded().forEach(customer -> saveDiscount.customersIncludedIds.add(customer.getUserId()));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveDiscountGson = gson.toJson(saveDiscount);
        FileUtil.write(FileUtil.generateAddress(Discount.class.getName(),saveDiscount.id),saveDiscountGson);
    }
    public static Discount load(int id){
        return null;
    }
}
