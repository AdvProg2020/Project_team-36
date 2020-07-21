package Network;

import Models.Query;
import Models.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static Response process(Query query) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8080);
            Gson gson = new GsonBuilder().create();
            String output = gson.toJson(query);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(output);
            String input = dataInputStream.readUTF();
            socket.close();
            Gson gson1 = new Gson();
            return gson.fromJson(input, Response.class);
        } catch (IOException e) {
            if (socket != null && !socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        }
    }

    public static String writeFile(byte[] fileBytes) throws IOException {
        Socket socket = new Socket("localhost",8181);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        dataOutputStream.write(fileBytes);
        String path = dataInputStream.readUTF();
        socket.close();
        return path;
    }

    public static byte[] readFile(String path) throws IOException {
        Socket socket = new Socket("localhost",8282);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        dataOutputStream.writeUTF(path);
        byte[] fileBytes = dataInputStream.readAllBytes();
        socket.close();
        return fileBytes;
    }
}
