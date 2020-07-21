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
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String path = dataInputStream.readUTF();
                File file = new File(path);
                dataOutputStream.write(Files.readAllBytes(file.toPath()));
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
