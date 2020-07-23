package Controllers;

import Models.*;
import Models.Gifts.Gift;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;
import java.util.*;

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
        } catch (NoSuchMethodException ignored) {
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

    public void removeDiscount(int id, String username){
        Customer customer = (Customer)User.getUserByUsername(username);
        Discount discount = Discount.getDiscountWithId(id);
        customer.removeDiscount(discount);
    }

    public void setDiscountForCustomer(int id, String username){
        Customer customer = (Customer)User.getUserByUsername(username);
        Discount discount = Discount.getDiscountWithId(id);
        customer.setDiscountForCustomer(discount);
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

    public void addNewWaitingLog() {
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

    public long getCartPrice(){
        return ((Customer)userVariables.getLoggedInUser()).getCartPrice();
    }

    public long getCartPriceConsideringSale(){
        return ((Customer)userVariables.getLoggedInUser()).getCartPriceConsideringSale();
    }

    public long getWaitingLogPayable(){
        return ((Customer)userVariables.getLoggedInUser()).getWaitingLog().getPayablePrice();
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "isThereProductInCart" -> processIsThereProductInCart(query);
            case "getCart" -> processGetCart(query);
            case "isThereMultipleSellers" -> processIsThereMultipleSellers(query);
            case "startPurchase" -> processStartPurchase(query);
            case "getWaitingLog" -> processGetWaitingLog(query);
            case "increaseProductInCart" -> processIncreaseProductInCart(query);
            case "decreaseProductInCart" -> processDecreaseProductInCart(query);
            case "getTotalCartPrice" -> processGetTotalCartPrice(query);
            case "getDiscountCodes" -> processGetDiscountCodes(query);
            case "getBalance" -> processGetBalance(query);
            case "getOrder" -> processGetOrder(query);
            case "rateProduct" -> processRateProduct(query);
            case "addNewWaitingLog" -> processAddNewWaitingLog(query);
            case "setAddressForPurchase" -> processSetAddressForPurchase(query);
            case "setPhoneNumberForPurchase" -> processSetPhoneNumberForPurchase(query);
            case "setDiscountCodeForPurchase" -> processSetDiscountCodeForPurchase(query);
            case "cancelPurchase" -> processCancelPurchase(query);
            case "purchase" -> processPurchase(query);
            case "getAllLogs" -> processGetAllLogs(query);
            case "sortDiscounts" -> processSortDiscounts(query);
            case "sortLogs" -> processSortLogs(query);
            case "getCartPrice" -> processGetCartPrice(query);
            case "getCartPriceConsideringSale" -> processGetCartPriceConsideringSale(query);
            case "getWaitingLogPayable" -> processGetWaitingLogPayable(query);
            case "setDiscountForCustomer" -> processSetDiscountForCustomer(query);
            case "removeDiscount" -> processRemoveDiscount(query);
            default -> new Response("Error", "");
        };
    }

    private Response processGetWaitingLogPayable(Query query) {
        return new Response("long", Long.toString(getWaitingLogPayable()));
    }

    private Response processGetCartPriceConsideringSale(Query query) {
        return new Response("long", Long.toString(getCartPriceConsideringSale()));
    }

    private Response processGetCartPrice(Query query) {
        return new Response("long", Long.toString(getCartPrice()));
    }

    private Response processSortLogs(Query query) {
        String name = query.getMethodInputs().get("name");
        String type = query.getMethodInputs().get("type");

        try {
            List<SaveCustomerLog> logs = new ArrayList<>();
            sortLogs(name, type).forEach(customerLog -> logs.add(new SaveCustomerLog(customerLog)));
            Gson gson = new GsonBuilder().create();
            return new Response("List<CustomerLog>", gson.toJson(logs));
        } catch (ProductsController.NoSortException e) {
            return new Response("NoSortException", "");
        }
    }

    private Response processSortDiscounts(Query query) {
        String name = query.getMethodInputs().get("name");
        String type = query.getMethodInputs().get("type");
        try {
            List<SaveDiscount> discounts = new ArrayList<>();
            sortDiscounts(name, type).forEach(discount -> discounts.add(new SaveDiscount(discount)));
            Gson gson = new GsonBuilder().create();
            return new Response("List<Discount>", gson.toJson(discounts));
        } catch (ProductsController.NoSortException e) {
            return new Response("NoSortException", "");
        }
    }

    private Response processGetAllLogs(Query query) {
        List<SaveCustomerLog> allLogs = new ArrayList<>();
        getAllLogs().forEach(customerLog -> allLogs.add(new SaveCustomerLog(customerLog)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<CustomerLog>", gson.toJson(allLogs));
    }

    private Response processPurchase(Query query) {
        try {
            SaveCustomerLog log = new SaveCustomerLog(purchase());
            Gson gson = new GsonBuilder().create();
            return new Response("CustomerLog", gson.toJson(log));
        } catch (NotEnoughMoney notEnoughMoney) {
            return new Response("NotEnoughMoney",Long.toString(notEnoughMoney.getAmount()));
        }
    }

    private Response processCancelPurchase(Query query) {
        cancelPurchase();
        return new Response("void", "");
    }

    private Response processSetDiscountCodeForPurchase(Query query) {
        int discountCode = Integer.parseInt(query.getMethodInputs().get("discountCode"));
        try {
            setDiscountCodeForPurchase(discountCode);
            return new Response("void", "");
        } catch (NoDiscountAvailableWithId noDiscountAvailableWithId) {
            Gson gson = new GsonBuilder().create();
            return new Response("NoDiscountAvailableWithId", gson.toJson(noDiscountAvailableWithId));
        }
    }

    private Response processSetPhoneNumberForPurchase(Query query) {
        String phoneNumber = query.getMethodInputs().get("phoneNumber");
        setPhoneNumberForPurchase(phoneNumber);
        return new Response("void", "");
    }

    private Response processSetAddressForPurchase(Query query) {
        String address = query.getMethodInputs().get("address");
        setAddressForPurchase(address);
        return new Response("void", "");
    }

    private Response processAddNewWaitingLog(Query query) {
        addNewWaitingLog();
        return new Response("void", "");
    }

    private Response processRateProduct(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        int rate = Integer.parseInt(query.getMethodInputs().get("rate"));
        try {
            rateProduct(productId, rate);
            return new Response("void", "");
        } catch (NoProductWithIdInLog noProductWithIdInLog) {
            Gson gson = new GsonBuilder().create();
            return new Response("NoProductWithIdInLog", gson.toJson(noProductWithIdInLog));
        }

    }

    private Response processGetOrder(Query query) {
        int orderId = Integer.parseInt(query.getMethodInputs().get("orderId"));
        try {
            SaveCustomerLog customerLog = new SaveCustomerLog(getOrder(orderId));
            Gson gson = new GsonBuilder().create();
            return new Response("CustomerLog", gson.toJson(customerLog));
        } catch (NoLogWithId noLogWithId) {
            Gson gson = new GsonBuilder().create();
            return new Response("NoLogWithId", gson.toJson(noLogWithId));
        }
    }

    private Response processGetBalance(Query query) {
        long balance  = getBalance();
        return new Response("long",Long.toString(balance));
    }

    private Response processGetDiscountCodes(Query query) {
        List<SaveDiscount> allDiscounts  = new ArrayList<>();
        getDiscountCodes().forEach(discount -> allDiscounts.add(new SaveDiscount(discount)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<Discount>",gson.toJson(allDiscounts));
    }

    private Response processGetTotalCartPrice(Query query) {
        long total  =getTotalCartPrice();
        return new Response("long",Long.toString(total));
    }

    private Response processDecreaseProductInCart(Query query) {
        Gson gson = new GsonBuilder().create();
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        if(query.getMethodInputs().containsKey("seller")){
            Seller seller = Seller.getSellerById(Integer.parseInt(query.getMethodInputs().get("seller")));
            decreaseProductInCart(seller,productId);
            return new Response("void","");
        }else{
            try {
                decreaseProductInCart(productId);
                return new Response("void","");
            } catch (NoProductWithIdInCart noProductWithIdInCart) {
                return new Response("NoProductWithIdInCart",gson.toJson(noProductWithIdInCart));
            } catch (MoreThanOneSellerForItem moreThanOneSellerForItem) {
                String toJson =gson.toJson( moreThanOneSellerForItem.saveSellers);
                return new Response("MoreThanOneSellerForItem",toJson);
            }
        }
    }

    private Response processIncreaseProductInCart(Query query) {
        Gson gson = new GsonBuilder().create();
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        if(query.getMethodInputs().containsKey("sellerId")){
            int sellerId = Integer.parseInt(query.getMethodInputs().get("sellerId"));
            try {
                increaseProductInCart(sellerId,productId);
                return new Response("void","");
            } catch (NotEnoughSupply notEnoughSupply) {
                return new Response("NotEnoughSupply","");
            }
        }else{
            try {
                increaseProductInCart(productId);
                return new Response("void","");
            } catch (NoProductWithIdInCart noProductWithIdInCart) {
                return new Response("NoProductWithIdInCart",gson.toJson(noProductWithIdInCart));
            } catch (MoreThanOneSellerForItem moreThanOneSellerForItem) {
                String toJson = gson.toJson(moreThanOneSellerForItem.getSaveSellers());
               return new Response("MoreThanOneSellerForItem",toJson);
            } catch (NotEnoughSupply notEnoughSupply) {
                return new Response("NotEnoughSupply","");
            }

        }
    }

    private Response processGetWaitingLog(Query query) {
        SaveWaitingLog saveWaitingLog = new SaveWaitingLog(((Customer) userVariables.getLoggedInUser()).getWaitingLog());
        Gson gson = new GsonBuilder().create();
        String saveWaitingLogGson = gson.toJson(saveWaitingLog);
       return new Response("WaitingLog",saveWaitingLogGson);
    }

    private Response processStartPurchase(Query query) {
        try {
            startPurchase();
            return new Response("void","");
        } catch (EmptyCart emptyCart) {
            return new Response("EmptyCart","");
        } catch (NotEnoughSupplyInCart notEnoughSupplyInCart) {
            Gson gson = new GsonBuilder().create();
            return new Response("NotEnoughSupplyInCart",gson.toJson(notEnoughSupplyInCart.getSaveItems()));
        }
    }

    private Response processIsThereMultipleSellers(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        return new Response("boolean",Boolean.toString(isThereMultipleSellers(productId)));
    }

    private Response processGetCart(Query query) {
        List<SaveSelectedItem> cart = new ArrayList<>();
        getCart().forEach(selectedItem -> cart.add(new SaveSelectedItem(selectedItem)));
        Gson gson = new GsonBuilder().create();
        return new Response("List<SelectedItem>",gson.toJson(cart));
    }

    private Response processIsThereProductInCart(Query query) {
        int productId = Integer.parseInt(query.getMethodInputs().get("productId"));
        return new Response("boolean",Boolean.toString(isThereProductInCart(productId)));
    }

    private Response processSetDiscountForCustomer(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        setDiscountForCustomer(id, query.getMethodInputs().get("username"));
        return new Response("void", "");
    }

    private Response processRemoveDiscount(Query query){
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        removeDiscount(id, query.getMethodInputs().get("username"));
        return new Response("void", "");
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
        private List<SaveSelectedItem> saveItems = new ArrayList<>();
        public NotEnoughSupplyInCart(ArrayList<SelectedItem> items) {
            items.forEach(selectedItem -> saveItems.add(new SaveSelectedItem(selectedItem)));
        }

        public List<SaveSelectedItem> getSaveItems() {
            return saveItems;
        }
    }

    public static class MoreThanOneSellerForItem extends Exception {
        List<SaveSeller> saveSellers = new ArrayList<>();
        public MoreThanOneSellerForItem(ArrayList<Seller> sellers) {
            sellers.forEach(seller -> saveSellers.add(new SaveSeller(seller)));
        }

        public List<SaveSeller> getSaveSellers(){
            return this.saveSellers;
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
        private long amount;//amount of money that is needed!

        public NotEnoughMoney(long amount) {
            this.amount = amount;
        }

        public long getAmount() {
            return amount;
        }
    }
}
