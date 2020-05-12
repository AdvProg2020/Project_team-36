package Models;

public class OptionalField implements Field {
    private String name;
    private String option;

    public OptionalField(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getOption() {
        return option;
    }
}
