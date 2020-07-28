package Client.Models;

import Client.GUI.Constants;
import Client.Network.Client;
import Models.*;


public class GlobalVariables {
    private User loggedInUser;
    private String token;
    private Product product;
    private Seller pendingSellerOfProduct;

    public String getToken() {
        return token;
    }

    public GlobalVariables() {
        this.loggedInUser = null;
    }

    public void setProduct(Product product) {
        this.product = product;
        Query query = new Query(Constants.globalVariables.getToken(), "ProductsController", "setProduct");
        query.getMethodInputs().put("productId",Integer.toString(product.getProductId()));
        Client.process(query);
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        if(loggedInUser==null)
            return;
        Constants.entryController.setLoggedInUser(loggedInUser.getUserId());
    }

    public User getLoggedInUser() {
        return Constants.entryController.getLoggedInUser();
    }

    public void setToken(String token) {
        this.token = token;
    }
}
