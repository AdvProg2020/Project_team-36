package Models;

import java.util.ArrayList;

public class SelectedItem {
    private Product product;
    private ArrayList<Seller> sellers;
    private ArrayList<Integer> countFromEachSeller;


    public SelectedItem(Product product) {
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

    public void increaseAmountFromSeller(Seller seller, int count){
        int index = sellers.indexOf(seller);
        countFromEachSeller.set(index,countFromEachSeller.get(index)+count);
    }

    public void decreaseAmountFromSeller(Seller seller,int count) throws NoSellersForItemInCart {
        int index = sellers.indexOf(seller);
        countFromEachSeller.set(index,countFromEachSeller.get(index)-count);
        this.product.getProductFieldBySeller(seller).increaseSupply(1);
        if(countFromEachSeller.get(index)==0){
            countFromEachSeller.remove(index);
            sellers.remove(index);
        }
        if(sellers.isEmpty())
            throw new NoSellersForItemInCart();
    }

    public long getTotalPrice(){
        long sum = 0;
        int i=0;
        for (Seller seller : sellers) {
            Long eachPrice = product.getProductFieldBySeller(seller).getCurrentPrice();
            sum+=eachPrice*countFromEachSeller.get(i);
            i++;
        }
        return sum;
    }

    public static class NoSellersForItemInCart extends Exception{

    }


}
