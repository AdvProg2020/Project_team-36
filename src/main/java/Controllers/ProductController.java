package Controllers;

import Exceptions.NoProductForThisSellerException;
import Exceptions.NoProductWithThisIdException;
import Models.Product;
import Models.ProductField;
import Models.Seller;

public class ProductController {
    GlobalVariables userVariables;
    public ProductController(GlobalVariables userVariables) {
        this.userVariables = userVariables;
    }

    public Product getProductById(int productId) throws NoProductWithThisIdException {
        for (Product product : Product.getAllProducts()) {
            if(product.getProductId() == productId){
                return product;
            }
        }
        throw new NoProductWithThisIdException("We don't have any products with this Id");
    }

    public void removeSellerFromProduct(Product product, Seller seller) throws NoProductForThisSellerException {
        for (ProductField productField : product.getProductFields()) {
            if (productField.getSeller().equals(seller)){
                product.getProductFields().remove(productField);
            }
        }
        throw new NoProductForThisSellerException("This seller does not sell this product!");
    }
}
