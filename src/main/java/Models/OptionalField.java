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

    @Override
    public String getQuantityString() {
        return this.quality;
    }

    public String getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return name+": "+quality;
    }
}