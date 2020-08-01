package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class FileServerRead implements Runnable {
    private ServerSocket serverSocket;

    public FileServerRead() throws IOException {
        this.serverSocket = new ServerSocket(8282);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.err.println("client connected");
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                read(socket,dataOutputStream,dataInputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void read(Socket socket,DataOutputStream dataOutputStream,DataInputStream dataInputStream){
        new Thread(() -> {
            String path = "";
            try {
                path = dataInputStream.readUTF();
                if(path.equals(""))
                    return;
                    System.err.println(path);
                    File file = new File(path);
                    dataOutputStream.writeInt(Files.readAllBytes(file.toPath()).length);
                    dataOutputStream.write(Files.readAllBytes(file.toPath()));
                    dataOutputStream.close();
                    socket.close();
                    System.err.println("client disconnected");
            } catch (EOFException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
