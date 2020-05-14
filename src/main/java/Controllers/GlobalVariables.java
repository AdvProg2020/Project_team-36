package Controllers;
import Models.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GlobalVariables {
    private ArrayList<Field> allFiltersOffs;
    private String sortOff;
    private String sortOffType;
    private ArrayList<Field> allFiltersProducts;
    private String sortProduct;
    private String sortProductType;
    private Product product;
    private User loggedInUser;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setSortProduct(String name,String type){
        this.sortProduct = name;
        this.sortProductType = type;
    }

    public String getSortProductType() {
        return sortProductType;
    }

    public String getSortOffType() {
        return sortOffType;
    }

    public void removeSortProduct(){
        this.sortProduct = "seen count";
        this.sortProductType = "ascending";
    }

    public void removeSortOff(){
        this.sortOff ="seen count";
        this.sortOffType = "ascending";
    }

    public String getSortProduct(){
        return this.sortProduct;
    }

    public void setSortOff(String name,String type) {
        this.sortOffType = type;
        this.sortOff = name;
    }

    public String getSortOff() {
        return sortOff;
    }
}
