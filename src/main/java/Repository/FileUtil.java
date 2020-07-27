package Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class  FileUtil {
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
        try {
            output = new String(Files.readAllBytes(Paths.get(address)));
            return output;
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    public static String generateAddress(String className, int id){
        File file = new File("./Server/src/main/resources/"+className+"/");
        if(!file.exists()){
            file.mkdirs();
        }
        return "./Server/src/main/resources/"+className+"/"+id+".json";
    }
}
