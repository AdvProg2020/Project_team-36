package Repository;

import Models.CartTag;
import Models.Product;
import Models.SelectedItem;
import Models.Seller;

import java.util.ArrayList;
import java.util.List;

public class SaveSelectedItem {
    private int productId;
    private List<Integer> sellerIds;
    private List<Integer> countFromEachSeller;
    private CartTag tag ;

    public SaveSelectedItem(SelectedItem selectedItem) {
        this.productId = selectedItem.getProduct().getProductId();
        this.tag = selectedItem.getTag();
        this.countFromEachSeller = new ArrayList<>();
        selectedItem.getCountFromEachSeller().forEach(count -> this.countFromEachSeller.add(count));
        this.sellerIds = new ArrayList<>();
        selectedItem.getSellers().forEach(seller -> this.sellerIds.add(seller.getUserId()));
    }

    public SelectedItem generateSelectedItem(){
        SelectedItem selectedItem = new SelectedItem(SaveProduct.load(this.productId),this.tag);
        this.countFromEachSeller.forEach(count -> selectedItem.getCountFromEachSeller().add(count));
        this.sellerIds.forEach(sellerId -> selectedItem.getSellers().add(SaveSeller.load(sellerId)));
        return selectedItem;
    }
}