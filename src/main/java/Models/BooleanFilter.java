package Models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class BooleanFilter implements Filter {
    Method method;
    ArrayList<String> options = new ArrayList<>();
    String name;

    public BooleanFilter(Method method){
        this.method = method;
        this.name = "available";
    }

    public BooleanFilter(Method method,ArrayList<String> options){
        this.method = method;
        this.options = options;
        this.name = "seller";
    }

    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        if(options.isEmpty())
            return booleanFilter(products);
        else
            return optionalFilter(products);
    }

    private ArrayList<Product> optionalFilter(ArrayList<Product> products){
        ArrayList<Product> toBeReturned = new ArrayList<>();
        products.stream().filter(product -> {
            for (String option : options) {
                try {
                    if((Boolean)method.invoke(product,option))
                    return true;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }).forEach(toBeReturned:: add);
        return toBeReturned;
    }

    private ArrayList<Product> booleanFilter(ArrayList<Product> products){
        ArrayList<Product> toBeReturned = new ArrayList<>();
        products.stream().filter(product -> {
            try {
                return (boolean)method.invoke(product);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        }).forEach(toBeReturned:: add);
        return toBeReturned;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void addOption(String name){
        options.add(name);
    }

    public void removeOption(String option){
        for (String s : options) {
            if(s.equalsIgnoreCase(option))
            {
                options.remove(s);
                return;
            }
        }
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
