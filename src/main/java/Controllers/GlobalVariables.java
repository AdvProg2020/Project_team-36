package Controllers;
import Models.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GlobalVariables {
    private ArrayList<Field> allFiltersOffs;
    private String SortOff;
    private ArrayList<Field> allFiltersProducts;
    private String sortProduct;
    private Product product;
    private User loggedInUser;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setSortProduct(String name){
        this.sortProduct = name;
    }

    public String getSortProduct(){
        return this.sortProduct;
    }

    public void setSortOff(String sortOff) {
        SortOff = sortOff;
    }

    public String getSortOff() {
        return SortOff;
    }
}
