package Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static void write(String address, String data){
        try {
            FileWriter myWriter = new FileWriter(address);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String read(String address){
        String output="";
        return output;
    }

    public static String generateAddress(String className, int id){
        File file = new File("./src/main/resources/"+className+"/");
        if(!file.exists()){
            file.mkdirs();
        }
        return "./src/main/resources/"+className+"/"+id+".json";
    }
}
