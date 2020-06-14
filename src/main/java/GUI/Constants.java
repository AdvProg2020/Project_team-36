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
    public static GlobalVariables loggedInUser;

    private static GUIManager guiManager = new GUIManager();

    public static GUIManager getGuiManager() {
        return guiManager;
    }

    public static void setControllers() {
        loggedInUser = new GlobalVariables();
        Constants.customerController = new CustomerController(loggedInUser);
        Constants.entryController = new EntryController(loggedInUser);
        Constants.managerController = new ManagerController(loggedInUser);
        Constants.offController = new OffController(loggedInUser);
        Constants.sellerController = new SellerController(loggedInUser);
        Constants.userController = new UserController(loggedInUser);
        Constants.productsController = new ProductsController(loggedInUser);
    }

}
