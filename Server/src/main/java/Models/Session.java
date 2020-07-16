package Models;

import Controllers.*;

public class Session {
    private ManagerController managerController;
    private CustomerController customerController;
    private SellerController sellerController;
    private EntryController entryController;
    private ProductsController productsController;
    private OffController offController;
    private UserController userController;
    private CategoryController categoryController;

    private GlobalVariables globalVariables;


    public Session(GlobalVariables globalVariables) {
        this.globalVariables = globalVariables;
        this.managerController = new ManagerController(globalVariables);
        this.customerController = new CustomerController(globalVariables);
        this.sellerController = new SellerController(globalVariables);
        this.entryController = new EntryController(globalVariables);
        this.productsController = new ProductsController(globalVariables);
        this.offController = new OffController(globalVariables);
        this.userController = new UserController(globalVariables);
        this.categoryController = new CategoryController();
    }

    public ManagerController getManagerController() {
        return managerController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public SellerController getSellerController() {
        return sellerController;
    }

    public EntryController getEntryController() {
        return entryController;
    }

    public ProductsController getProductsController() {
        return productsController;
    }

    public OffController getOffController() {
        return offController;
    }

    public UserController getUserController() {
        return userController;
    }

    public GlobalVariables getGlobalVariables() {
        return globalVariables;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }
}
