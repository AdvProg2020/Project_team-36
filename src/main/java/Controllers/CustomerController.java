package Controllers;

import Models.*;
import Models.Gifts.Gift;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerController extends UserController {

    public CustomerController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public boolean isThereProductInCart(int productId) {
        return ((Customer) userVariables.getLoggedInUser()).isThereProductInCart(productId);
    }

    public ArrayList<SelectedItem> getCart() {
        return ((Customer) userVariables.getLoggedInUser()).getCart();
    }

    public boolean isThereMultipleSellers(int productId) {
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        return item.getSellers().size() > 1;
    }

    public void increaseProductInCart(int productId) throws NoProductWithIdInCart, MoreThanOneSellerForItem, NotEnoughSupply {
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        if (!((Customer) userVariables.getLoggedInUser()).isThereProductInCart(productId))
            throw new NoProductWithIdInCart("There is no product with this id in your cart!");
        else if (item.getSellers().size() > 1) {
            throw new MoreThanOneSellerForItem(item.getSellers());
        } else if (item.getProduct().enoughSupplyOfSeller(item.getSellers().get(0), 1)) {
            item.getProduct().buyProductFromSeller(item.getSellers().get(0), 1);
            item.increaseAmountFromSeller(item.getSellers().get(0), 1);
            return;
        } else {
            throw new NotEnoughSupply();
        }


    }

    public void increaseProductInCart(int sellerNumber, int productId) throws NotEnoughSupply {
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        Seller seller = item.getSellers().get(sellerNumber - 1);
        if (item.getProduct().enoughSupplyOfSeller(seller, 1)) {
            item.getProduct().buyProductFromSeller(item.getSellers().get(0), 1);
            item.increaseAmountFromSeller(item.getSellers().get(0), 1);
            return;
        } else {
            throw new NotEnoughSupply();
        }
    }

    public void decreaseProductInCart(int productId) throws NoProductWithIdInCart, MoreThanOneSellerForItem {
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        if (!((Customer) userVariables.getLoggedInUser()).isThereProductInCart(productId))
            throw new NoProductWithIdInCart("There is no product with this id in your cart!");
        else if (item.getSellers().size() > 1) {
            throw new MoreThanOneSellerForItem(item.getSellers());
        } else {
            try {
                item.decreaseAmountFromSeller(item.getSellers().get(0), 1);
            } catch (SelectedItem.NoSellersForItemInCart e) {
                ((Customer) userVariables.getLoggedInUser()).removeItemFromCart(item);
            }
        }

    }

    public void decreaseProductInCart(Seller seller, int productId) {
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        try {
            item.decreaseAmountFromSeller(seller, 1);
        } catch (SelectedItem.NoSellersForItemInCart e) {
            ((Customer) userVariables.getLoggedInUser()).removeItemFromCart(item);
        }
    }

    public long getTotalCartPrice() {
        return ((Customer) userVariables.getLoggedInUser()).getCartPrice();
    }

    public HashMap<Discount, Integer> getDiscountCodes() {

        return ((Customer) userVariables.getLoggedInUser()).getAllDiscountsForCustomer();
    }

    public long getBalance() {
        return ((Customer) userVariables.getLoggedInUser()).getCredit();
    }

    public CustomerLog getOrder(int orderId) throws NoLogWithId {
        if (!((Customer) userVariables.getLoggedInUser()).isThereLog(orderId))
            throw new NoLogWithId("There is no log with this id!");
        return ((Customer) userVariables.getLoggedInUser()).getLog(orderId);

    }

    public void rateProduct(int productId, int rate) throws NoProductWithIdInLog {
        if (Product.getProduct(productId) == null)
            throw new NoProductWithIdInLog("No product with this id in your log!");
        else {
            Score score = new Score((Customer) userVariables.getLoggedInUser(), rate);
            Product.getProduct(productId).addScore(score);
        }
    }

    public void setAddressForPurchase(String address)  {

        Customer customer = ((Customer) userVariables.getLoggedInUser());
        customer.setWaitingLog(new WaitingLog(customer, address));
        customer.getWaitingLog().setAllItems(customer.getCart());
    }

    public void setPhoneNumberForPurchase(String phoneNumber) {
        ((Customer) userVariables.getLoggedInUser()).getWaitingLog().setCustomerPhoneNumber(phoneNumber);
    }

    public void setDiscountCodeForPurchase(int discountCode) throws NoDiscountAvailableWithId {
        Customer customer = (Customer) userVariables.getLoggedInUser();
        if (customer.isThereDiscountCode(discountCode))
            throw new NoDiscountAvailableWithId("No Discount with Id");
        customer.getWaitingLog().setDiscount(Discount.getDiscount(discountCode));
    }

    public ArrayList<Gift> getGifts() {
        WaitingLog waitingLog = ((Customer) userVariables.getLoggedInUser()).getWaitingLog();
        Gift.giveGift(waitingLog);
        return waitingLog.getGifts();
    }

    public CustomerLog purchase() throws NotEnoughMoney {
        WaitingLog waitingLog = ((Customer) userVariables.getLoggedInUser()).getWaitingLog();
        Customer customer = (Customer) userVariables.getLoggedInUser();
        if (waitingLog.getPayablePrice() > customer.getCredit()) {
            waitingLog.removeDiscount();
            throw new NotEnoughMoney(waitingLog.getPayablePrice() - customer.getCredit());
        }
        waitingLog.applyCreditChanges();
        SellerLog.createSellerLogs(waitingLog);
        CustomerLog log = CustomerLog.createCustomerLog(waitingLog);
        return log;
    }

    public ArrayList<CustomerLog> getAllLogs() {
        return ((Customer) userVariables.getLoggedInUser()).getAllLogs();
    }

    public static class NoProductWithIdInCart extends Exception {
        public NoProductWithIdInCart(String message) {
            super(message);
        }
    }

    public static class EmptyCart extends Exception {
        public EmptyCart() {
        }
    }

    public static class MoreThanOneSellerForItem extends Exception {
        ArrayList<Seller> sellers = new ArrayList<>();

        public MoreThanOneSellerForItem(ArrayList<Seller> sellers) {
            this.sellers.addAll(sellers);
        }

        public ArrayList<Seller> getSellers() {
            return sellers;
        }
    }

    public static class NotEnoughSupply extends Exception {
        public NotEnoughSupply() {
        }
    }

    public static class NoLogWithId extends Exception {
        public NoLogWithId(String message) {
            super(message);
        }
    }

    public static class NoProductWithIdInLog extends Exception {
        public NoProductWithIdInLog(String message) {
            super(message);
        }
    }

    public static class NoDiscountAvailableWithId extends Exception {
        public NoDiscountAvailableWithId(String message) {
            super(message);
        }


    }

    public static class NotEnoughMoney extends Exception {
        Long amount;//amount of money that is needed!

        public NotEnoughMoney(long amount) {
            this.amount = amount;
        }

        public Long getAmount() {
            return amount;
        }
    }
}
