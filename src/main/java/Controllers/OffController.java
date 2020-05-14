package Controllers;

import Models.Product;

import java.lang.reflect.Method;
import java.util.HashMap;

public class OffController extends ProductsController {
    GlobalVariables userVariables;
    private HashMap<String, Method> offSortMethod;

    public OffController(GlobalVariables userVariables) {
        super(userVariables);
       this.userVariables = userVariables;
    }

    @Override
    public String getProductCurrentSortName() {
        return userVariables.getSortOff();
    }

    @Override
    public String getSortProductType() {
        return userVariables.getSortOffType();
    }

    @Override
    public void removeSortProduct() {
        userVariables.removeSortOff();
    }

    @Override
    public void setSort(String name, String type) throws NoSortException {
            for (String field : productSortMethod.keySet()) {
                if (field.equalsIgnoreCase(name)) {
                    userVariables.setSortOff(name, type);
                    return;
                }
            }
            throw new NoSortException();

    }
}
