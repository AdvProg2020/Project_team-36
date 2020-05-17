package Controllers;

import Models.Category;
import Models.Customer;
import Models.Product;
import Models.Seller;

import java.util.ArrayList;
import java.util.HashSet;

public class SellerController extends UserController{

    public SellerController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public long getLoggedInSellerBalance() {
        return ((Seller)userVariables.getLoggedInUser()).getCredit();
    }

    public String getLoggedInSellerCompanyInformation(){
        String output = "";
        output += "company Name:" + ((Seller) userVariables.getLoggedInUser()).getCompanyName();
        if (!((Seller)userVariables.getLoggedInUser()).getCompanyInfo().equals("")){
            output += "\n" + "Info:" + ((Seller) userVariables.getLoggedInUser()).getCompanyInfo();
        }
        return output;
    }

    public void removeSellerProduct(int productId) throws NoProductForSeller {
        Seller seller = ((Seller)userVariables.getLoggedInUser());
        if(!seller.isThereProduct(productId))
            throw new NoProductForSeller();
        Product.getProduct(productId).removeSeller(seller);
    }

    public Category getMainCategory(){
        return Category.getMainCategory();
    }

    public ArrayList<Product> getSellerProducts(){
        return ((Seller)userVariables.getLoggedInUser()).getAllProducts();
    }

    public Product getProductWithId(int id) throws InvalidProductIdException {
        if(Product.isThereProductWithId(id)){
            return Product.getProduct(id);
        } else {
            throw new InvalidProductIdException();
        }
    }

    public StringBuilder getSellerProductDetail(Product product){
        return product.printSellerProductDetails((Seller)userVariables.getLoggedInUser());
    }

    public HashSet<Customer> getAllBuyers(Product product){
        return product.getProductFieldBySeller((Seller)userVariables.getLoggedInUser()).getAllBuyers();
    }

    public static class InvalidProductIdException extends Exception {

    }

    public static class NoProductForSeller extends Exception{

    }
}
