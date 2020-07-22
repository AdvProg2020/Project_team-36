package Models;

import java.util.ArrayList;
import java.util.Date;

public class FileProduct {
    private String fileName;
    private String fileType;
    private String filePath;

    public FileProduct(String filePath,String fileName) {
        this.filePath =filePath;
        int index = fileName.indexOf(".");
        this.fileType = fileName.substring(index+1);
        this.fileName = fileName.substring(0,index);
    }

}
