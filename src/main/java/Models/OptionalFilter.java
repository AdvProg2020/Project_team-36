package Models;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class OptionalFilter implements Filter {
    private ArrayList<String> options;
    private Method method;
    private String name;

    public OptionalFilter(String name) {
        this.options = new ArrayList<>();
        this.method = null;
        this.name = name;
    }

    public OptionalFilter(Method method,String name) {
        this.options = new ArrayList<>();
        this.method = method;
        this.name = name;
    }

    public ArrayList<Product> filter(ArrayList<Product> products) {
        ArrayList<Product> toBeFiltered = new ArrayList<>();
        toBeFiltered.addAll(products);

        if(method!=null)
            return generalFilter(toBeFiltered);
        else
            return proprietaryFilter(toBeFiltered);
    }

    private ArrayList<Product> generalFilter(ArrayList<Product> toBeFiltered){
        ArrayList<Product> toBeReturned = new ArrayList<>();
        toBeFiltered.stream().filter(product -> {
            for (String option : options) {
                try {
                    if(option.equalsIgnoreCase((String)method.invoke(product)))
                        return true;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.exit(1);
                }
            }
            return false;
        }).forEach(toBeReturned::add);

        return toBeReturned;

    }

    private ArrayList<Product> proprietaryFilter(ArrayList<Product> toBeFiltered){
        ArrayList<Product> toBeReturned = new ArrayList<>();
        toBeFiltered.stream().filter(product -> {
            for (String option : options) {
                OptionalField optionalField = (OptionalField) product.getField(name);
                if(option.equalsIgnoreCase(optionalField.getQuality()))
                    return true;
            }
            return false;
        }).forEach(toBeReturned::add);
        return toBeReturned;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        String result = "type: optional filter\n";
        if(method==null)
            result+="general filter\nfilter based on: "+name+"\n";
        else
            result+= "proprietary filter\nfilter based on: "+name+"\n";
        result+="All chosen options are:\n";
        for (String option : options) {
            result= result+ option+"\n";
        }
        return result;
    }
}
