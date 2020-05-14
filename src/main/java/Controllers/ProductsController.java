package Controllers;

import Models.Category;
import Models.Product;
import Models.Sort;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductsController {
    private GlobalVariables userVariables;
    private HashMap<String, Method> productSortMethod;
    public ProductsController(GlobalVariables userVariables) {
        this.userVariables = userVariables;
        this.productSortMethod = new HashMap<>();
        setSortMethodsProducts(productSortMethod);
    }

    private void setSortMethodsProducts(HashMap<String, Method> sortHashmap){
        try {
            Method method = Product.class.getDeclaredMethod("getProductionDate");
            sortHashmap.put("production time",method);
            method = Product.class.getDeclaredMethod("getSeenNumber");
            sortHashmap.put("seen count",method);
            method = Product.class.getDeclaredMethod("getName");
            sortHashmap.put("name",method);
            method = Product.class.getDeclaredMethod("getScore");
            sortHashmap.put("score",method);
            method = Product.class.getDeclaredMethod("getHighestCurrentPrice");
            sortHashmap.put("Maximum current price",method);
            method = Product.class.getDeclaredMethod("getLowestCurrentPrice");
            sortHashmap.put("Minimum current price",method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
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

    public ArrayList<Product> sortAllProducts(String name,String type)throws NoSortException{
        boolean isNameValid = false;
        Method method = null;
        for (String field : productSortMethod.keySet()) {
            if(field.equalsIgnoreCase(name)){
                isNameValid =true;
                method = productSortMethod.get(field);
            }
        }
        if(!isNameValid)
            throw new NoSortException();
        userVariables.setSortProduct(name);
        ArrayList<Product> products = Product.getAllProducts();
        sort(products,type,method);
        return products;
    }

    private void sort(ArrayList<Product> toBeSorted, String type,Method method)  {
        if(type.equalsIgnoreCase("ascending"))
            new Sort().sort(toBeSorted,method,true);
        else
            new Sort().sort(toBeSorted,method,false);
    }

public static class NoProductWithId extends Exception{}

public static class NoSortException extends Exception{}
}


