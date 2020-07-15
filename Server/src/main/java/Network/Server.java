package Network;

import Controllers.GlobalVariables;
import Models.Query;
import Models.Response;
import Models.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private Map<String, Session> clients;
    private ServerSocket serverSocket;

    public Server() throws IOException {
        this.clients = new HashMap<>();
        this.serverSocket = new ServerSocket(8080);
    }

    public void run() throws IOException {
        Socket socket = serverSocket.accept();
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));
        String input = dataInputStream.readUTF();
        Response response = process(input);
        Gson gson = new GsonBuilder().create();
        String output = gson.toJson(response);
        dataOutputStream.writeUTF(output);
    }

    private Response process(String data) {
        Gson gson = new Gson();
        Query query = gson.fromJson(data, Query.class);

        if (query.getControllerName().equals("SessionController")){
            return new Response("String",addSession());
        }

        if (!clients.containsKey(query.getToken())){
            return new Response("Error","TokenError");
        }

        Session currentSession = clients.get(query.getToken());

        switch (query.getControllerName()) {
            case "CategoryController":
                return currentSession.getCategoryController().processQuery(query);
            default:
                return new Response("Error","");
        }
    }

    private String addSession() {
        Session session = new Session(new GlobalVariables());
        Date date = new Date();
        String token = String.valueOf(date.getTime());
        this.clients.put(token, session);
        return token;
    }
}
