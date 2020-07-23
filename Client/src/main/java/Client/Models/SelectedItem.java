package Client.Models;

import Client.GUI.Constants;
import Models.CartTag;
import Models.Query;
import Models.Response;
import Client.Network.Client;
import Repository.SaveProduct;
import Repository.SaveSelectedItem;
import Repository.SaveSeller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectedItem {
    private SaveSelectedItem saveSelectedItem;
    private List<Integer> countFromEachSeller;
    private CartTag tag ;

    public SelectedItem(SaveSelectedItem saveSelectedItem){
        this.saveSelectedItem = saveSelectedItem;
        this.tag = saveSelectedItem.getTag();
        this.countFromEachSeller = new ArrayList<>();
        this.countFromEachSeller.addAll(saveSelectedItem.getCountFromEachSeller());
    }
    public Product getProduct() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Product");
        query.getMethodInputs().put("id", "" + saveSelectedItem.getProductId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Product")) {
            Gson gson = new Gson();
            SaveProduct saveProduct = gson.fromJson(response.getData(), SaveProduct.class);
            return new Product(saveProduct);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public List<Seller> getSellers() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Customer");
        this.saveSelectedItem.getSellerIds().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Seller>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveSeller>>(){}.getType();
            List<SaveSeller> allSaveSellers = gson.fromJson(response.getData(),type);
            List<Seller> allSellers = new ArrayList<>();
            allSaveSellers.forEach(saveSeller -> allSellers.add(new Seller(saveSeller)));
            return allSellers;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public List<Integer> getCountFromEachSeller() {
        return countFromEachSeller;
    }


    public CartTag getTag() {
        return tag;
    }

    public long getItemTotalPrice() {
        long sum = 0;
        int i = 0;
        for (Seller seller : this.getSellers()) {
            Long eachPrice = getProduct().getProductFieldBySeller(seller.getUserId()).getCurrentPrice();
            sum += eachPrice * countFromEachSeller.get(i);
            i++;
        }
        return sum;
    }
}
