package Controllers;

import GUI.Constants;
import Models.*;
import Models.Gifts.Gift;
import Network.Client;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private final String controllerName = "CustomerController";

    public boolean isThereProductInCart(int productId) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "isThereProductInCart");
        query.getMethodInputs().put("productId", Integer.toString(productId));
        Response response = Client.process(query);
        return Boolean.getBoolean(response.getData());
    }

    public ArrayList<SelectedItem> getCart() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCart");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<SelectedItem> allSelectedItems = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveSelectedItem>>() {
        }.getType();
        List<SaveSelectedItem> allSaveSelectedItem = gson.fromJson(response.getData(), type);
        allSaveSelectedItem.forEach(saveSelectedItem -> allSelectedItems.add(new SelectedItem(saveSelectedItem)));
        return allSelectedItems;
    }

    public boolean isThereMultipleSellers(int productId) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "isThereMultipleSellers");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        Response response = Client.process(query);
        return Boolean.getBoolean(response.getData());
    }

    public void startPurchase() throws EmptyCart, NotEnoughSupplyInCart {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "startPurchase");
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("EmptyCart"))
            throw new EmptyCart();
        else if(response.getReturnType().equalsIgnoreCase("NotEnoughSupplyInCart")) {
            Gson gson = new Gson();
            ArrayList<SelectedItem> allSelectedItems = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveSelectedItem>>() {
            }.getType();
            List<SaveSelectedItem> allSaveSelectedItem = gson.fromJson(response.getData(), type);
            allSaveSelectedItem.forEach(saveSelectedItem -> allSelectedItems.add(new SelectedItem(saveSelectedItem)));
            throw new NotEnoughSupplyInCart(allSelectedItems);
        }
    }

    public WaitingLog getWaitingLog() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getWaitingLog");
        Response response = Client.process(query);
        Gson gson = new Gson();
        return new WaitingLog(gson.fromJson(response.getData(), SaveWaitingLog.class));
    }

    public void increaseProductInCart(int productId) throws NoProductWithIdInCart, MoreThanOneSellerForItem, NotEnoughSupply {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "increaseProductInCart");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoProductWithIdInCart"))
            throw new NoProductWithIdInCart("No product");
        else if(response.getReturnType().equalsIgnoreCase("NotEnoughSupply"))
            throw new NotEnoughSupply();
        else if(response.getReturnType().equalsIgnoreCase("MoreThanOneSellerForItem")){
            Gson gson = new Gson();
            ArrayList<Seller> allSellers = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveSeller>>() {
            }.getType();
            List<SaveSeller> allSaveSellers = gson.fromJson(response.getData(), type);
            allSaveSellers.forEach(saveSellers -> allSellers.add(new Seller(saveSellers)));
            throw new MoreThanOneSellerForItem(allSellers);
        }
    }

    public void increaseProductInCart(int sellerId, int productId) throws NotEnoughSupply {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "increaseProductInCart");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        query.getMethodInputs().put("sellerId",Integer.toString(sellerId));
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NotEnoughSupply"))
            throw new NotEnoughSupply();
    }

    public void decreaseProductInCart(int productId) throws NoProductWithIdInCart, MoreThanOneSellerForItem {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "decreaseProductInCart");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoProductWithIdInCart"))
            throw new NoProductWithIdInCart("No product");
        else if(response.getReturnType().equalsIgnoreCase("MoreThanOneSellerForItem")){
            Gson gson = new Gson();
            ArrayList<Seller> allSellers = new ArrayList<>();
            Type type = new TypeToken<ArrayList<SaveSeller>>() {
            }.getType();
            List<SaveSeller> allSaveSellers = gson.fromJson(response.getData(), type);
            allSaveSellers.forEach(saveSellers -> allSellers.add(new Seller(saveSellers)));
            throw new MoreThanOneSellerForItem(allSellers);
        }
    }

    public void decreaseProductInCart(Seller seller, int productId) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "decreaseProductInCart");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        query.getMethodInputs().put("seller",Integer.toString(seller.getUserId()));
        Response response = Client.process(query);
    }

    public long getTotalCartPrice() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getTotalCartPrice");
        Response response = Client.process(query);
        return Long.getLong(response.getData());
    }


    public ArrayList<Discount> getDiscountCodes() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getDiscountCodes");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<Discount> allDiscounts = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveDiscount>>() {
        }.getType();
        List<SaveDiscount> allSaveDiscounts = gson.fromJson(response.getData(), type);
        allSaveDiscounts.forEach(saveDiscount -> allDiscounts.add(new Discount(saveDiscount)));
        return allDiscounts;
    }

    public long getBalance() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getBalance");
        Response response = Client.process(query);
        return Long.getLong(response.getData());
    }

    public CustomerLog getOrder(int orderId) throws NoLogWithId {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getOrder");
        query.getMethodInputs().put("orderId",Integer.toString(orderId));
        Response response = Client.process(query);
        Gson gson = new Gson();
        SaveCustomerLog saveCustomerLog = gson.fromJson(response.getData(),SaveCustomerLog.class);
        return new CustomerLog(saveCustomerLog);
    }

    public void rateProduct(int productId, int rate) throws NoProductWithIdInLog {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "rateProduct");
        query.getMethodInputs().put("productId",Integer.toString(productId));
        query.getMethodInputs().put("rate",Integer.toString(rate));
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoProductWithIdInLog"))
            throw new NoProductWithIdInLog("NO PRODUCT");
    }

    public void addNewWaitingLog() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "addNewWaitingLog");
        Client.process(query);
    }

    public void setAddressForPurchase(String address) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setAddressForPurchase");
        query.getMethodInputs().put("address",address);
        Client.process(query);
    }

    public void setPhoneNumberForPurchase(String phoneNumber) {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setPhoneNumberForPurchase");
        query.getMethodInputs().put("phoneNumber",phoneNumber);
        Client.process(query);
    }

    public void setDiscountCodeForPurchase(int discountCode) throws NoDiscountAvailableWithId {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "setDiscountCodeForPurchase");
        query.getMethodInputs().put("discountCode",Integer.toString(discountCode));
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoDiscountAvailableWithId"))
            throw new NoDiscountAvailableWithId("NOOOO");
    }

    public void cancelPurchase() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "cancelPurchase");
        Response response = Client.process(query);
    }

    public CustomerLog purchase() throws NotEnoughMoney {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "purchase");
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("CustomerLog")){
            Gson gson = new Gson();
            SaveCustomerLog saveCustomerLog = gson.fromJson(response.getData(),SaveCustomerLog.class);
            return new CustomerLog(saveCustomerLog);
        }else if(response.getReturnType().equalsIgnoreCase("NotEnoughMoney"))
            throw new NotEnoughMoney(Long.getLong(response.getData()));
        else{
            System.out.println(response.getData());
            return null;
        }
    }

    public ArrayList<CustomerLog> getAllLogs() {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getAllLogs");
        Response response = Client.process(query);
        Gson gson = new Gson();
        ArrayList<CustomerLog> allCustomerLogs = new ArrayList<>();
        Type type = new TypeToken<ArrayList<SaveCustomerLog>>() {
        }.getType();
        List<SaveCustomerLog> allSaveCustomerLogs = gson.fromJson(response.getData(), type);
        allSaveCustomerLogs.forEach(saveCustomerLog -> allCustomerLogs.add(new CustomerLog(saveCustomerLog)));
        return allCustomerLogs;
    }

    public ArrayList<SelectedItem> sortCart(String field, String type) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sortCart");
        query.getMethodInputs().put("field",field);
        query.getMethodInputs().put("type",type);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoSortException"))
            throw new ProductsController.NoSortException();
        Gson gson = new Gson();
        ArrayList<SelectedItem> allSelectedItems = new ArrayList<>();
        Type fieldType = new TypeToken<ArrayList<SaveSelectedItem>>() {
        }.getType();
        List<SaveSelectedItem> allSaveSelectedItems = gson.fromJson(response.getData(), fieldType);
        allSaveSelectedItems.forEach(saveSelectedItem -> allSelectedItems.add(new SelectedItem(saveSelectedItem)));
        return allSelectedItems;
    }

    public ArrayList<Discount> sortDiscounts(String name, String type) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sortDiscounts");
        query.getMethodInputs().put("name",name);
        query.getMethodInputs().put("type",type);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoSortException"))
            throw new ProductsController.NoSortException();
        Gson gson = new Gson();
        ArrayList<Discount> allDiscounts = new ArrayList<>();
        Type fieldType = new TypeToken<ArrayList<SaveDiscount>>() {
        }.getType();
        List<SaveDiscount> allSaveDiscounts = gson.fromJson(response.getData(), fieldType);
        allSaveDiscounts.forEach(saveDiscount -> allDiscounts.add(new Discount(saveDiscount)));
        return allDiscounts;
    }

    public ArrayList<CustomerLog> sortLogs(String name, String type) throws ProductsController.NoSortException {
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "sortLogs");
        query.getMethodInputs().put("name",name);
        query.getMethodInputs().put("type",type);
        Response response = Client.process(query);
        if(response.getReturnType().equalsIgnoreCase("NoSortException"))
            throw new ProductsController.NoSortException();
        Gson gson = new Gson();
        ArrayList<CustomerLog> allCustomerLogs = new ArrayList<>();
        Type fieldType = new TypeToken<ArrayList<SaveCustomerLog>>() {
        }.getType();
        List<SaveCustomerLog> allSaveCustomerLogs = gson.fromJson(response.getData(), fieldType);
        allSaveCustomerLogs.forEach(saveCustomerLog -> allCustomerLogs.add(new CustomerLog(saveCustomerLog)));
        return allCustomerLogs;
    }

    public long getCartPrice(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCartPrice");
        Response response = Client.process(query);
        return Long.getLong(response.getData());
    }

    public long getCartPriceConsideringSale(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getCartPriceConsideringSale");
        Response response = Client.process(query);
        return Long.getLong(response.getData());
    }

    public long getWaitingLogPayable(){
        Query query = new Query(Constants.globalVariables.getToken(), controllerName, "getWaitingLogPayable");
        Response response = Client.process(query);
        return Long.getLong(response.getData());
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
        private List<SaveSelectedItem> saveItems = new ArrayList<>();
        public NotEnoughSupplyInCart(ArrayList<SelectedItem> items) {
            this.items = new ArrayList<>();
            this.items = items;
            items.forEach(selectedItem -> saveItems.add(new SaveSelectedItem(selectedItem)));
        }

        public ArrayList<SelectedItem> getItems() {
            return items;
        }

        public List<SaveSelectedItem> getSaveItems() {
            return saveItems;
        }
    }

    public static class MoreThanOneSellerForItem extends Exception {
        ArrayList<Seller> sellers = new ArrayList<>();
        List<SaveSeller> saveSellers = new ArrayList<>();
        public MoreThanOneSellerForItem(ArrayList<Seller> sellers) {
            this.sellers.addAll(sellers);
            sellers.forEach(seller -> saveSellers.add(new SaveSeller(seller)));
        }

        public ArrayList<Seller> getSellers() {
            return sellers;
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
        long amount;//amount of money that is needed!

        public NotEnoughMoney(long amount) {
            this.amount = amount;
        }

        public long getAmount() {
            return amount;
        }
    }
}
