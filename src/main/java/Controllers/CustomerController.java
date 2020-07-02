package Controllers;

import Models.*;
import Models.Gifts.Gift;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerController extends UserController {
    private HashMap<String, Method> sortCartMethods;
    private HashMap<String, Method> sortDiscountMethods;
    private HashMap<String, Method> sortLogsMethods;


    public CustomerController(GlobalVariables userVariables) {
        super(userVariables);
        setSortCartMethods();
        setSortDiscountMethods();
        setSortLogsMethods();
    }

    private void setSortCartMethods() {
        sortCartMethods = new HashMap<>();
        try {
            Method method = SelectedItem.class.getDeclaredMethod("getProductName");
            sortCartMethods.put("product name", method);
            method = SelectedItem.class.getDeclaredMethod("getCount");
            sortCartMethods.put("count", method);
            method = SelectedItem.class.getDeclaredMethod("getItemTotalPrice");
            sortCartMethods.put("total price", method);
        } catch (NoSuchMethodException e) {
        }
    }

    private void setSortLogsMethods() {
        sortLogsMethods = new HashMap<>();
        try {
            Method method = CustomerLog.class.getDeclaredMethod("getDate");
            sortLogsMethods.put("date", method);
            method = CustomerLog.class.getDeclaredMethod("getTotalPayable");
            sortLogsMethods.put("payable", method);
            method = CustomerLog.class.getDeclaredMethod("getDiscountAmount");
            sortLogsMethods.put("discount amount", method);
            method = CustomerLog.class.getDeclaredMethod("getGiftDiscount");
            sortLogsMethods.put("gift amount", method);
        } catch (NoSuchMethodException e) {
        }
    }

    private void setSortDiscountMethods() {
        sortDiscountMethods = new HashMap<>();
        try {
            Method method = Discount.class.getDeclaredMethod("getEndTime");
            sortDiscountMethods.put("end time", method);
            method = Discount.class.getDeclaredMethod("getDiscountPercent");
            sortDiscountMethods.put("percent", method);
            method = Discount.class.getDeclaredMethod("getDiscountLimit");
            sortDiscountMethods.put("limit", method);
        } catch (NoSuchMethodException e) {

        }

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

    public void startPurchase() throws EmptyCart, NotEnoughSupplyInCart {
        ArrayList<SelectedItem> cart = ((Customer) userVariables.getLoggedInUser()).getCart();
        ((Customer) userVariables.getLoggedInUser()).updateCart();
        if (cart.isEmpty())
            throw new EmptyCart();
        ArrayList<SelectedItem> temp = new ArrayList<>();
        for (SelectedItem item : cart) {
            int index = 0;

            for (Seller seller : item.getSellers()) {
                if (item.getProduct().getProductFieldBySeller(seller).getSupply() < item.getCountFromEachSeller().get(index)) {
                    temp.add(item);
                    break;
                }
            }
        }
        if (temp.size() > 0)
            throw new NotEnoughSupplyInCart(temp);
    }

    public WaitingLog getWaitingLog() {
        return ((Customer) userVariables.getLoggedInUser()).getWaitingLog();
    }

    public void increaseProductInCart(int productId) throws NoProductWithIdInCart, MoreThanOneSellerForItem, NotEnoughSupply {
        ((Customer) userVariables.getLoggedInUser()).updateCart();
        if (!((Customer) userVariables.getLoggedInUser()).isThereProductInCart(productId))
            throw new NoProductWithIdInCart("");
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        if (item.getSellers().size() > 1) {
            throw new MoreThanOneSellerForItem(item.getSellers());
        } else if (item.getProduct().enoughSupplyOfSeller(item.getSellers().get(0), 1)) {
            item.increaseAmountFromSeller(item.getSellers().get(0), 1);
            return;
        } else {
            throw new NotEnoughSupply();
        }


    }

    public void increaseProductInCart(int sellerId, int productId) throws NotEnoughSupply {
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        Seller seller = Seller.getSellerById(sellerId);
        if (item.getProduct().enoughSupplyOfSeller(seller, 1)) {
            item.increaseAmountFromSeller(seller, 1);
            return;
        } else {
            throw new NotEnoughSupply();
        }
    }

    public void decreaseProductInCart(int productId) throws NoProductWithIdInCart, MoreThanOneSellerForItem {
        ((Customer) userVariables.getLoggedInUser()).updateCart();
        if (!((Customer) userVariables.getLoggedInUser()).isThereProductInCart(productId))
            throw new NoProductWithIdInCart("NOPRODUCT");
        SelectedItem item = ((Customer) userVariables.getLoggedInUser()).getProductInCart(productId);
        if (item.getSellers().size() > 1) {
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


    public ArrayList<Discount> getDiscountCodes() {
        ArrayList<Discount> toBeReturned = new ArrayList<>();
        toBeReturned.addAll(((Customer) userVariables.getLoggedInUser()).getAllActiveDiscountsForCustomer().keySet());
        return toBeReturned;
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

    public void addNewWaitingLog(){
        Customer customer = ((Customer) userVariables.getLoggedInUser());
        customer.setWaitingLog(new WaitingLog(customer));
        customer.getWaitingLog().setAllItems(customer.getCart());
    }

    public void setAddressForPurchase(String address) {
        Customer customer = ((Customer) userVariables.getLoggedInUser());
        customer.getWaitingLog().setCustomerAddress(address);
    }

    public void setPhoneNumberForPurchase(String phoneNumber) {
        ((Customer) userVariables.getLoggedInUser()).getWaitingLog().setCustomerPhoneNumber(phoneNumber);
    }

    public void setDiscountCodeForPurchase(int discountCode) throws NoDiscountAvailableWithId {
        Customer customer = (Customer) userVariables.getLoggedInUser();
        if (!customer.isThereDiscountCode(discountCode))
            throw new NoDiscountAvailableWithId("No Discount with Id");
        customer.getWaitingLog().setDiscount(Discount.getDiscountWithId(discountCode));
    }

    public void cancelPurchase() {
        WaitingLog waitingLog = ((Customer) userVariables.getLoggedInUser()).getWaitingLog();
        if (waitingLog.getDiscount() != null) {
            waitingLog.removeDiscount();
        }
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
            cancelPurchase();
            throw new NotEnoughMoney(waitingLog.getPayablePrice() - customer.getCredit());
        }
        waitingLog.applyPurchaseChanges();
        waitingLog.addCustomerToBuyers();
        SellerLog.createSellerLogs(waitingLog);
        return CustomerLog.createCustomerLog(waitingLog);
    }

    public ArrayList<CustomerLog> getAllLogs() {
        return ((Customer) userVariables.getLoggedInUser()).getAllLogs();
    }

    public ArrayList<SelectedItem> sortCart(String field, String type) throws ProductsController.NoSortException {
        boolean isAscending = false;
        if (type.equalsIgnoreCase("ascending"))
            isAscending = true;
        for (String regex : sortCartMethods.keySet()) {
            if (regex.equalsIgnoreCase(field)) {
                new Sort().sort(((Customer) userVariables.getLoggedInUser()).getCart(), sortCartMethods.get(regex), isAscending);
                return ((Customer) userVariables.getLoggedInUser()).getCart();
            }
        }
        throw new ProductsController.NoSortException();
    }

    public ArrayList<Discount> sortDiscounts(String name, String type) throws ProductsController.NoSortException {
        Method method = null;
        for (String regex : sortDiscountMethods.keySet()) {
            if (regex.equalsIgnoreCase(name)) {
                method = sortDiscountMethods.get(regex);
                break;
            }
        }
        if (method == null)
            throw new ProductsController.NoSortException();
        ArrayList<Discount> discounts = new ArrayList<>();
        discounts.addAll(((Customer) userVariables.getLoggedInUser()).getAllActiveDiscountsForCustomer().keySet());
        new Sort().sort(discounts, method, type.equalsIgnoreCase("ascending"));
        return discounts;
    }

    public ArrayList<CustomerLog> sortLogs(String name, String type) throws ProductsController.NoSortException {
        Method method = null;
        for (String regex : sortLogsMethods.keySet()) {
            if (regex.equalsIgnoreCase(name)) {
                method = sortLogsMethods.get(regex);
                break;
            }
        }
        if (method == null)
            throw new ProductsController.NoSortException();
        ArrayList<CustomerLog> logs = new ArrayList<>();
        logs.addAll(((Customer) userVariables.getLoggedInUser()).getAllLogs());
        new Sort().sort(logs, method, type.equalsIgnoreCase("ascending"));
        return logs;
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

    public static class NotEnoughSupplyInCart extends Exception {
        private ArrayList<SelectedItem> items;

        public NotEnoughSupplyInCart(ArrayList<SelectedItem> items) {
            this.items = new ArrayList<>();
            this.items = items;
        }

        public ArrayList<SelectedItem> getItems() {
            return items;
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
