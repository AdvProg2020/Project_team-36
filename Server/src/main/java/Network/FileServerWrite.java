package Network;

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
                int length = dataInputStream.readInt();
                byte[] fileBytes = new byte[length];
                dataInputStream.readFully(fileBytes);
                String path = "src/main/resources/files/" + new Date().getTime()+".dat";
                File file = new File(path);
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(fileBytes);
                outputStream.close();
                dataOutputStream.writeUTF(path);
                dataOutputStream.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
