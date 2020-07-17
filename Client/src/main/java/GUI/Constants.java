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
    public static GlobalVariables globalVariables;

    private static GUIManager guiManager = new GUIManager();
    public static GUIManager getGuiManager() {
        return guiManager;
    }

    public static void setControllers() {
        globalVariables = new GlobalVariables();
        Constants.customerController = new CustomerController(globalVariables);
        Constants.entryController = new EntryController(globalVariables);
        Constants.managerController = new ManagerController(globalVariables);
        Constants.offController = new OffController(globalVariables);
        Constants.sellerController = new SellerController(globalVariables);
        Constants.userController = new UserController(globalVariables);
        Constants.productsController = new ProductsController(globalVariables);
    }

}
