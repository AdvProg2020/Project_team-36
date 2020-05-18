package Models;

import java.util.ArrayList;

import static Models.CartTag.*;

public class SelectedItem {
    private Product product;
    private ArrayList<Seller> sellers;
    private ArrayList<Integer> countFromEachSeller;
    private CartTag tag ;


    public SelectedItem(Product product,Seller seller) {
        this.product = product;
        this.sellers = new ArrayList<>();
        this.countFromEachSeller = new ArrayList<>();
        this.sellers.add(seller);
        this.countFromEachSeller.add(1);
        this.tag = CartTag.ENOUGH_SUPPLY;
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

    public CartTag getTag() {
        return tag;
    }

    public int getCount() {
        int sum = 0;
        for (Integer count : countFromEachSeller) {
            sum += count;
        }
        return sum;
    }

    public void checkTag(){
        int i = 0;
        for (Seller seller : this.sellers) {
             if(product.getProductFieldBySeller(seller).getSupply()<countFromEachSeller.get(i)){
                this.tag = CartTag.NOT_ENOUGH_SUPPLY;
                return;
            }
            i++;
        }
        this.tag = CartTag.ENOUGH_SUPPLY;
    }

    public void increaseAmountFromSeller(Seller seller, int count) {
        int index = sellers.indexOf(seller);
        countFromEachSeller.set(index, countFromEachSeller.get(index) + count);
    }

    public void decreaseAmountFromSeller(Seller seller, int count) throws NoSellersForItemInCart {
        int index = sellers.indexOf(seller);
        countFromEachSeller.set(index, countFromEachSeller.get(index) - count);
        if (countFromEachSeller.get(index) == 0) {
            countFromEachSeller.remove(index);
            sellers.remove(index);
        }
        if (sellers.isEmpty())
            throw new NoSellersForItemInCart();
    }

    public long getItemTotalPrice() {
        long sum = 0;
        int i = 0;
        for (Seller seller : sellers) {
            Long eachPrice = product.getProductFieldBySeller(seller).getCurrentPrice();
            sum += eachPrice * countFromEachSeller.get(i);
            i++;
        }
        return sum;
    }

    public long getSellerTotalPrice(Seller seller) {
        int index = this.sellers.indexOf(seller);
        int count = countFromEachSeller.get(index);
        return (this.product.getProductFieldBySeller(seller).getCurrentPrice()) * count;

    }

    public void updateSelectedItem(){
        ArrayList<Seller> temp = new ArrayList<>();
        int i =0;
        for (Seller seller : this.sellers) {
            if(seller.getStatus().equals(Status.DELETED)||!User.isThereUsername(seller.getUsername())||!product.getAllSellers().contains(seller)){
                temp.add(seller);
                countFromEachSeller.remove(i);
            }
            i++;
        }
        this.sellers.removeAll(temp);
    }





    public static class NoSellersForItemInCart extends Exception {

    }


}
