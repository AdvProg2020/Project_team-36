package Models;

import java.util.ArrayList;

import static Models.CartTag.*;

public class SelectedItem {
    private Product product;
    private ArrayList<Seller> sellers;
    private ArrayList<Integer> countFromEachSeller;
    private CartTag tag ;


    public SelectedItem(Product product) {
        this.product = product;
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
        if(!Product.getAllProducts().contains(this.getProduct()))
            this.tag = PRODUCT_DOESNOT_EXIST;
        int i = 0;
        for (Seller seller : this.sellers) {
            if(!product.getAllSellers().contains(seller)||!Seller.getAllSellers().contains(seller)){
                this.tag = CartTag.SELLER_DOESNOT_EXIST;
                return;
            }else if(product.getProductFieldBySeller(seller).getSupply()<countFromEachSeller.get(i)){
                this.tag = CartTag.NOT_ENOUGH_SUPPLY;
                return;
            }
            i++;
        }
        this.tag = CartTag.ENOUGH_SUPPLY;
    }

    public SelectedItem editForAvailability(){
        if(this.tag.equals(PRODUCT_DOESNOT_EXIST))
            return null;
        else if(this.tag.equals(ENOUGH_SUPPLY))
            return this;
        else{
            ArrayList<Seller> temp = new ArrayList<>();
            for (Seller seller : this.sellers) {
                if(!Seller.getAllSellers().contains(seller)||this.getProduct().getProductFieldBySeller(seller)==null){
                    temp.add(seller);
                }
                else if(this.countFromEachSeller.get(sellers.indexOf(seller))>this.product.getProductFieldBySeller(seller).getSupply()){
                    int supply = this.product.getProductFieldBySeller(seller).getSupply();
                    if(supply==0)
                        temp.add(seller);
                    else{
                        countFromEachSeller.set(sellers.indexOf(seller),supply);
                    }
                }
            }
            if(temp.size()==sellers.size())
                return null;
            for (Seller seller : temp) {
                countFromEachSeller.remove(sellers.indexOf(seller));
            }
            sellers.removeAll(temp);
            return this;
        }
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



    public static class NoSellersForItemInCart extends Exception {

    }


    public SelectedItem(Product product, CartTag tag) {
        this.product = product;
        this.tag = tag;
        this.sellers = new ArrayList<>();
        this.countFromEachSeller = new ArrayList<>();
    }
}
