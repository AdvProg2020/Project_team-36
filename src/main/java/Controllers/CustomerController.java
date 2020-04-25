package Controllers;

import Models.*;

import java.util.ArrayList;

public class CustomerController extends UserController {
    public CustomerController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public boolean isThereProductInCart(int productId){
        return ((Customer)userVariables.getLoggedInUser()).isThereProductInCart(productId);
}

    public ArrayList<ItemInCart> getCart(){
        return ((Customer)userVariables.getLoggedInUser()).getCart();
    }

    public boolean isThereMultipleSellers(int productId){
        ItemInCart item = ((Customer)userVariables.getLoggedInUser()).getProductInCart(productId);
        if(item.getSellers().size()>1)
            return true;
        return false;
    }

    public void increaseProductInCart(int productId) throws Exception {
        ItemInCart item = ((Customer)userVariables.getLoggedInUser()).getProductInCart(productId);
        if(((Customer)userVariables.getLoggedInUser()).isThereProductInCart(productId))
            throw  new NoProductWithIdInCart("There is no product with this id in your cart!");
        else if(item.getSellers().size()>1){
            throw new MoreThanOneSellerForItem(item.getSellers());
        }
        else if(item.getProduct().enoughSupplyOfSeller(item.getSellers().get(0),1)){
            item.getProduct().buyProductFromSeller(item.getSellers().get(0),1);
            item.buyFromSeller(item.getSellers().get(0),1);
            return;
        } else{
            throw new NotEnoughSupply();
        }


    }

    public void increaseProductInCart(int sellerNumber, int productId) throws NotEnoughSupply {
        ItemInCart item = ((Customer)userVariables.getLoggedInUser()).getProductInCart(productId);
        Seller seller = item.getSellers().get(sellerNumber-1);
        if(item.getProduct().enoughSupplyOfSeller(seller,1)){
            item.getProduct().buyProductFromSeller(item.getSellers().get(0),1);
            item.buyFromSeller(item.getSellers().get(0),1);
            return;
        } else{
            throw new NotEnoughSupply();
        }
    }

    public static class NoProductWithIdInCart extends Exception{
        public NoProductWithIdInCart(String message) {
            super(message);
        }
    }

    public static class MoreThanOneSellerForItem extends Exception{
        ArrayList<Seller> sellers = new ArrayList<>();
        public MoreThanOneSellerForItem(ArrayList<Seller> sellers) {
            this.sellers.addAll(sellers);
        }

        public ArrayList<Seller> getSellers() {
            return sellers;
        }
    }

    public static class NotEnoughSupply extends Exception{
        public NotEnoughSupply() {
        }
    }
}
