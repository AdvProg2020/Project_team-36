import Network.FileServerRead;
import Network.FileServerWrite;
import Network.Server;
import Repository.RepositoryManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            RepositoryManager.loadData();
            Thread fileReaderThread = new Thread(new FileServerRead());
            Thread fileWriterThread = new Thread(new FileServerWrite());
            fileReaderThread.start();
            fileWriterThread.start();

            Server server = new Server();
            server.run();
            RepositoryManager.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
