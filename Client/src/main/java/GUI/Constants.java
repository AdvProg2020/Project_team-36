package GUI;

import Controllers.*;
import Models.GlobalVariables;

public class  Constants {

    public static ManagerController managerController;
    public static CustomerController customerController;
    public static SellerController sellerController;
    public static EntryController entryController;
    public static ProductsController productsController;
    public static OffController offController;
    public static UserController userController;
    public static GlobalVariables globalVariables;

    private static GUIManager guiManager = new GUIManager();
    public static GUIManager getGuiManager() {
        return guiManager;
    }

    public static void setControllers() {
        globalVariables = new GlobalVariables();
        Constants.customerController = new CustomerController();
        Constants.entryController = new EntryController();
        Constants.managerController = new ManagerController();
        Constants.offController = new OffController();
        Constants.sellerController = new SellerController();
        Constants.userController = new UserController();
        Constants.productsController = new ProductsController(globalVariables);
    }

}
