package Models;

public class IntegerField implements Field{
    private String name;
    private int quantity;

    public IntegerField(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }
}