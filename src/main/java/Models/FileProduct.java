package Models;

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

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileType() {
        return fileType;
    }
}
