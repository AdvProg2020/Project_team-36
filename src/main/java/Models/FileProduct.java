package Models;

import java.util.ArrayList;
import java.util.Date;

public class FileProduct extends Product {
    private String fileName;
    private String fileType;
    private String filePath;

    public FileProduct(String name, String company, Category category, ArrayList<Field> fieldsOfCategory, String information, ProductField productField, Date productionDate, String productImageURL,String filePath,String fileName) {
        super(name, company, category, fieldsOfCategory, information, productField, productionDate, productImageURL);
        this.filePath =filePath;
        int index = fileName.indexOf(".");
        this.fileType = fileName.substring(index+1);
        this.fileName = fileName.substring(0,index);
    }

    @Override
    public String getPendingRequestType() {
        return "file product";
    }
}
