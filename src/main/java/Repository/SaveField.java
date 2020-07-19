package Repository;

import Models.Field;
import Models.IntegerField;
import Models.OptionalField;

public class SaveField {
    private IntegerField integerField;
    private OptionalField optionalField;

    public SaveField(Field field) {
        if (field != null) {
            if (field instanceof IntegerField) {
                integerField = (IntegerField) field;
            } else if (field instanceof OptionalField) {
                optionalField = (OptionalField) field;
            }
        }
    }

    public Field generateField(){
        if (this.optionalField != null){
            return this.optionalField;
        }

        if (this.integerField != null){
            return this.integerField;
        }

        return null;
    }
}
