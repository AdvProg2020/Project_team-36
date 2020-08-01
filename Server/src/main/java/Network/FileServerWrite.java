package Network;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

public class FileServerWrite implements Runnable{
    private ServerSocket serverSocket;

    public FileServerWrite() throws IOException {
        this.serverSocket = new ServerSocket(8181);
    }

    @Override
    public void run() {
        while (true){
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
               write(socket,dataInputStream,dataOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(Socket socket, DataInputStream dataInputStream,DataOutputStream dataOutputStream){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int length = 0;
                try {
                    length = dataInputStream.readInt();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                byte[] fileBytes = new byte[length];
                try {
                    dataInputStream.readFully(fileBytes);
                    String path = "./Server/src/main/resources/files/" + new Date().getTime() + ".dat";
                    File file = new File(path);
                    OutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(fileBytes);
                    outputStream.close();
                    dataOutputStream.writeUTF(path);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    socket.close();
                }catch (IOException ioException){

                }
            }
        }).start();
    }
}
