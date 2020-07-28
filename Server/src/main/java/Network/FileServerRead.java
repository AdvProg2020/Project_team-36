package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

public class FileServerRead implements Runnable {
    private ServerSocket serverSocket;

    public FileServerRead() throws IOException {
        this.serverSocket = new ServerSocket(8282);
    }

    @Override
    public void run() {
        while (true){
            try {
                Socket socket = serverSocket.accept();
                System.err.println("client connected");
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String path = dataInputStream.readUTF();
                System.err.println(path);
                File file = new File(path);
                dataOutputStream.writeInt(Files.readAllBytes(file.toPath()).length);
                dataOutputStream.write(Files.readAllBytes(file.toPath()));

                socket.close();
                System.err.println("client disconnected");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
