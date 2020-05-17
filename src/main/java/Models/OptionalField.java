package Models;

public class OptionalField implements Field {
    private String name;
    private String quality;

    public OptionalField(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getQuality() {
        return quality;
    }

    @Override
    public String getFieldInfo() {
        return "field: " + this.name + "  ->  quality: " + this.quality;
    }
}