package Models;

import java.math.BigDecimal;

public class IntegerField implements Field{
    private String name;
    private BigDecimal quantity;

    public IntegerField(String name){
        this.name = name;
    }

    @Override
    public void setValue(String quantity) {
        this.quantity = new BigDecimal(quantity);
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFieldInfo() {
        return "field: " + this.name + "  ->  quantity: " + this.quantity;
    }

    @Override
    public String getQuantityString() {
        return quantity.toString();
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name+": "+quantity;
    }
}