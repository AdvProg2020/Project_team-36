package Models;

import java.util.ArrayList;

public class ItemInCart {
    private Product product;
    ArrayList<Seller> sellers;
    ArrayList<Integer> countFromEachSeller;

    public ItemInCart(Product product) {
        this.product = product;
    }

    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    public ArrayList<Integer> getCountFromEachSeller() {
        return countFromEachSeller;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount(){
        int sum = 0;
        for (Integer count : countFromEachSeller) {
            sum +=count;
        }
        return sum;
    }

    public void buyFromSeller(Seller seller,int count){
        int index = sellers.indexOf(seller);
        countFromEachSeller.set(index,countFromEachSeller.get(index)+count);
    }


}
