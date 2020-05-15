package Models;

import java.math.BigDecimal;

public class IntegerField implements Field{
    private String name;
    private BigDecimal quantity;

    public IntegerField(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(String quantity) {
        this.quantity = new BigDecimal(quantity);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}