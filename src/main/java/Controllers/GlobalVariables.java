package Controllers;
import Exceptions.NoLoggedInUserException;
import Models.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GlobalVariables {
    private ArrayList<Field> allFiltersOffs;
    private ArrayList<String> allSortsOffs;
    private ArrayList<Field> allFiltersProducts;
    private ArrayList<String> allSortsProducts;
    private Product product;
    private User loggedInUser;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() throws NoLoggedInUserException {
        if(loggedInUser == null){
            throw new NoLoggedInUserException("No user has logged in!");
        }
        return loggedInUser;
    }

    //-..-
}
