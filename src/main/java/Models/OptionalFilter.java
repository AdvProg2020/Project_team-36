package Models;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

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
        CollectionUtils.filter(toBeFiltered, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                for (String option : options) {
                    try {
                        if(option.equalsIgnoreCase((String)method.invoke(object)))
                            return true;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.exit(1);
                    }
                }
                return false;
            }
        });
        return toBeFiltered;
    }

    private ArrayList<Product> proprietaryFilter(ArrayList<Product> toBeFiltered){
        CollectionUtils.filter(toBeFiltered, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                for (String option : options) {
                    OptionalField optionalField = (OptionalField) ((Product)object).getField(name);
                    if(option.equalsIgnoreCase(optionalField.getQuality()))
                        return true;
                }
                return false;
            }
        });
        return toBeFiltered;
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
