package Models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.Predicate;

public class RangeFilter implements Filter {
    private BigDecimal min;
    private BigDecimal max;
    private Method method;
    private String name;

    public RangeFilter(String name) {
        this.method = null;
        this.name = name;
    }

    public RangeFilter( Method method,String name){
        this.method = method;
        this.name= name;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public ArrayList<Product> filter(ArrayList<Product> products) {
        ArrayList<Product> toBeFiltered = new ArrayList<>();
        toBeFiltered.addAll(products);
        if (method!=null)
            return generalFilter(toBeFiltered);
        else
            return proprietaryFilter(toBeFiltered);
    }

    private ArrayList<Product> generalFilter(ArrayList<Product> toBeFiltered) {
        ArrayList<Product> toBeReturned = new ArrayList<>();
        toBeFiltered.stream().filter(product -> {
            try {
               return (Long)method.invoke(product)>=min.doubleValue()&&((Long)method.invoke(product)<=max.doubleValue());
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.exit(1);
            }
            return false;
        }).forEach(toBeReturned::add);

        return toBeReturned;
    }

    private ArrayList<Product> proprietaryFilter(ArrayList<Product> toBeFiltered) {
        ArrayList<Product> toBeReturned = new ArrayList<>();
        toBeFiltered.stream().filter(product -> {
            IntegerField integerField = (IntegerField) product.getField(name);
            return min.compareTo(integerField.getQuantity()) != 1 && max.compareTo(integerField.getQuantity()) != -1;
        }).forEach(toBeReturned::add);

        return toBeReturned;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setMax(String max) {
        this.max = new BigDecimal(max);
    }

    public void setMin(String min) {
        this.min = new BigDecimal(min);
    }

    @Override
    public String toString() {
        String result = "type: range filter\n" +
                "minimum amount: "+min+"\n" +
                "maximum amount: "+max+"\n";
        if(method==null)
            result+="general filter\nfilter based on: "+name;
        else
            result+= "proprietary filter\nfilter based on: "+name;
        return result;
    }
}
