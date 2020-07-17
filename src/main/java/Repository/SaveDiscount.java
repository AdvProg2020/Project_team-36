package Repository;

import Models.Discount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveDiscount {
    private int id;
    private long startTime;
    private long endTime;
    private double discountPercent;//bar hasbe darsad nist
    private long discountLimit;
    private int repetitionForEachUser;
    private static int lastId = 0;
    private List<Integer> customersIncludedIds;

    private SaveDiscount() {
        this.customersIncludedIds = new ArrayList<>();
    }

    public SaveDiscount(Discount discount) {
        this.customersIncludedIds = new ArrayList<>();
        this.id = discount.getId();
        this.startTime = discount.getStartTime().getTime();
        this.endTime = discount.getEndTime().getTime();
        this.discountPercent = discount.getDiscountPercent();
        this.discountLimit = discount.getDiscountLimit();
        this.repetitionForEachUser = discount.getRepetitionForEachUser();
        discount.getCustomersIncluded().forEach(customer -> this.customersIncludedIds.add(customer.getUserId()));
    }

    public static void save(Discount discount) {
        SaveDiscount saveDiscount = new SaveDiscount(discount);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveDiscountGson = gson.toJson(saveDiscount);
        FileUtil.write(FileUtil.generateAddress(Discount.class.getName(), saveDiscount.id), saveDiscountGson);
    }

    public static Discount load(int id) {
        lastId = Math.max(lastId, id);
        Discount potentialDiscount = Discount.getDiscountById(id);
        if (potentialDiscount != null) {
            return potentialDiscount;
        }
        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Discount.class.getName(), id));
        if (data == null) {
            return null;
        }
        SaveDiscount saveDiscount = gson.fromJson(data, SaveDiscount.class);
        Discount discount = new Discount(saveDiscount.id, new Date(saveDiscount.startTime), new Date(saveDiscount.endTime),
                saveDiscount.discountPercent, saveDiscount.discountLimit, saveDiscount.repetitionForEachUser);
        Discount.addToAllDiscounts(discount);
        saveDiscount.customersIncludedIds.forEach(customerIncludedId -> discount.getCustomersIncluded().add(SaveCustomer.load(customerIncludedId)));
        return discount;
    }

    public static int getLastId() {
        return lastId;
    }
}
