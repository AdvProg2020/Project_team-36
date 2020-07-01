package Repository;

import GUI.AlertBox;
import Models.*;
import Models.Gifts.Gift;

import java.io.File;

public class RepositoryManager {
    public static void saveData(){
        Product.updateAllProducts();
        Customer.updateAllCustomers();
        Discount.updateDiscounts();
        Gift.updateGifts();
        Manager.updateManagers();
        Sale.updateSales();
        Seller.updateSellers();
        User.updateAllUsers();

        Category.getAllCategories().forEach(category -> SaveCategory.save(category));
        Customer.getAllCustomers().forEach(customer -> SaveCustomer.save(customer));
        Discount.getAllDiscounts().forEach(discount -> SaveDiscount.save(discount));
        Gift.getAllGifts().forEach(gift -> SaveGift.save(gift));
        Manager.getAllManagers().forEach(manager -> SaveManager.save(manager));
        Product.getAllProducts().forEach(product -> SaveProduct.save(product));
        Request.getAllRequests().forEach(request -> SaveRequest.save(request));
        Sale.getAllSales().forEach(sale -> SaveSale.save(sale));
        Seller.getAllSellers().forEach(seller -> SaveSeller.save(seller));

        AlertBox.display("SaveData","All data is saved successfully");
    }

    public static void loadData(){
        loadCategories();
        loadCustomers();
        loadDiscounts();
        loadGifts();
        loadManagers();
        loadProducts();
        loadRequests();
        loadSales();
        loadSellers();

        SaveUser.setLastId(User.getTotalUsersMade());
        User.getAllUsers().forEach(user -> SaveUser.load(user.getUserId()));
        User.getAllUsers().forEach(user -> User.addUsername(user.getUsername()));
        User.setTotalUsersMade(SaveUser.getLastId());

        AlertBox.display("LoadData","All data is loaded successfully");
    }

    private static void loadSellers() {
        File sellerDir = new File("./src/main/resources/"+ Seller.class.getName()+"/");
        File[] directoryListing = sellerDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveSeller.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
        User.setTotalUsersMade(Math.max(SaveSeller.getLastId(),User.getTotalUsersMade()));
    }

    private static void loadSales() {
        File saleDir = new File("./src/main/resources/"+ Sale.class.getName()+"/");
        File[] directoryListing = saleDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveSale.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
    }

    private static void loadRequests() {
        File requestDir = new File("./src/main/resources/"+ Request.class.getName()+"/");
        File[] directoryListing = requestDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveRequest.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
    }

    private static void loadProducts() {
        File productDir = new File("./src/main/resources/"+ Product.class.getName()+"/");
        File[] directoryListing = productDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveProduct.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
    }

    private static void loadManagers() {
        File managerDir = new File("./src/main/resources/"+ Manager.class.getName()+"/");
        File[] directoryListing = managerDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveManager.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
        User.setTotalUsersMade(Math.max(SaveManager.getLastId(),User.getTotalUsersMade()));
    }

    private static void loadGifts() {
        File giftDir = new File("./src/main/resources/"+ Gift.class.getName()+"/");
        File[] directoryListing = giftDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveGift.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
    }

    private static void loadDiscounts() {
        File discountDir = new File("./src/main/resources/"+ Discount.class.getName()+"/");
        File[] directoryListing = discountDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveDiscount.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
        Discount.setTotalCodesMade(SaveDiscount.getLastId());
    }

    private static void loadCustomers() {
        File customerDir = new File("./src/main/resources/"+ Customer.class.getName()+"/");
        File[] directoryListing = customerDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveCustomer.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
        User.setTotalUsersMade(Math.max(SaveCustomer.getLastId(),User.getTotalUsersMade()));
    }

    private static void loadCategories() {
        File categoryDir = new File("./src/main/resources/"+ Category.class.getName()+"/");
        File[] directoryListing = categoryDir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                SaveCategory.load(Integer.parseInt(name.split("\\.")[0]));
            }
        }
        Category.setTotalCategoriesMade(SaveCategory.getLastId());
    }
}
