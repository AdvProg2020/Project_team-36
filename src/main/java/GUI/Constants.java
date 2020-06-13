package GUI;

import Controllers.*;

public class  Constants {

    public static ManagerController managerController;
    public static CustomerController customerController;
    public static SellerController sellerController;
    public static EntryController entryController;
    public static ProductsController productsController;
    public static OffController offController;
    public static UserController userController;

    private static GUIManager guiManager = new GUIManager();

    public static GUIManager getGuiManager() {
        return guiManager;
    }

    public static void setControllers() {
        GlobalVariables user = new GlobalVariables();
        Constants.customerController = new CustomerController(user);
        Constants.entryController = new EntryController(user);
        Constants.managerController = new ManagerController(user);
        Constants.offController = new OffController(user);
        Constants.sellerController = new SellerController(user);
        Constants.userController = new UserController(user);
        Constants.productsController = new ProductsController(user);
    }

}
