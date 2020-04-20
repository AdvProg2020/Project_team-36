package Models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Data {
    private String className;
    private ArrayList<Object> fields;

    public Data(String className) {
        this.className = className;
    }

    public Data addField(Object Field){
        this.fields.add(Field);
        return this;
    }

    public List<Object> getFields() {
        return Collections.unmodifiableList(this.fields);
    }

}
