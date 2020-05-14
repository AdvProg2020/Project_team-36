package Controllers;

import Models.Category;
import Models.Product;

public class ProductsController {
    GlobalVariables userVariables;
    public ProductsController(GlobalVariables userVariables) {
        this.userVariables = userVariables;
    }

    public Product getProduct(int id) throws NoProductWithId {
        for (Product product : Product.getAllProducts()) {
            if(product.getProductId()==(id)&&!product.isProductDeleted()){
                return product;}
        }
        throw new NoProductWithId();
    }

    public Category getMainCategory(){
        return Category.getMainCategory();
    }

public static class NoProductWithId extends Exception{}
}


