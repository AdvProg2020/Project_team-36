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
    private AuctionController auctionController;

    private CategoryController categoryController;
    private DiscountController discountController;
    private EditProductController editProductController;
    private NewManagerController newManagerController;
    private NewOffController newOffController;
    private NewProductController newProductController;

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
        this.auctionController = new AuctionController(globalVariables);
    }

    public void setCategoryController(){
        this.categoryController = new CategoryController();
    }

    public void setDiscountController(){
        this.discountController = new DiscountController();
    }

    public void setEditProductController(){
        this.editProductController = null;
        if (globalVariables.getLoggedInUser() instanceof Seller){
            this.editProductController = new EditProductController((Seller) globalVariables.getLoggedInUser());
        }
    }

    public void setNewManagerController(){
        this.newManagerController = new NewManagerController();
    }

    public void setNewOffController(){
        this.editProductController = null;
        if (globalVariables.getLoggedInUser() instanceof Seller){
            this.newOffController = new NewOffController((Seller) globalVariables.getLoggedInUser());
        }
    }

    public void setNewProductController(){
        this.newProductController = new NewProductController();
    }

    public DiscountController getDiscountController() {
        return discountController;
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

    public EditProductController getEditProductController() {
        return editProductController;
    }

    public NewManagerController getNewManagerController() {
        return newManagerController;
    }

    public NewOffController getNewOffController() {
        return newOffController;
    }

    public NewProductController getNewProductController() {
        return newProductController;
    }

    public AuctionController getAuctionController() {
        return auctionController;
    }
}
