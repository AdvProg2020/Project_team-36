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

    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(
                        new BufferedOutputStream(socket.getOutputStream()));
                String input = dataInputStream.readUTF();
                Response response = process(input);
                Gson gson = new GsonBuilder().create();
                String output = gson.toJson(response);
                dataOutputStream.writeUTF(output);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response process(String data) {
        Gson gson = new Gson();
        Query query = gson.fromJson(data, Query.class);

        if (query.getControllerName().equals("SessionController")) {
            return new Response("String", addSession());
        }

        if (!clients.containsKey(query.getToken())) {
            return new Response("Error", "TokenError");
        }

        Session currentSession = clients.get(query.getToken());

        switch (query.getControllerName()) {
            case "ProductsController":
                return currentSession.getProductsController().processQuery(query);

            case "OffController":
                return currentSession.getOffController().processQuery(query);

            case "EntryController":
                return currentSession.getEntryController().processQuery(query);

            case "SellerController":
                return currentSession.getSellerController().processQuery(query);

            case "CustomerController":
                return currentSession.getCustomerController().processQuery(query);

            case "ManagerController":
                return currentSession.getManagerController().processQuery(query);

            case "UserController":
                return currentSession.getUserController().processQuery(query);

            case "CategoryController":
                return processCategoryController(query, currentSession);

            case "DiscountController":
                return processDiscountController(query, currentSession);

            case "EditProductController":
                return processEditProductController(query, currentSession);

            case "NewManagerController":
                return processNewManagerController(query, currentSession);

            case "NewOffController":
                return processNewOffController(query, currentSession);

            case "NewProductController":
                return processNewProductController(query, currentSession);

            default:
                return new Response("Error", "");
        }
    }

    private Response processNewProductController(Query query, Session currentSession) {
        if (query.getMethodName().equals("new")) {
            currentSession.setNewProductController();
            return new Response("String", "Constructor is called");
        }

        if (currentSession.getNewProductController() == null) {
            return new Response("Error", "Constructor has not been called");
        }
        return currentSession.getNewProductController().processQuery(query);
    }

    private Response processNewOffController(Query query, Session currentSession) {
        if (query.getMethodName().equals("new")) {
            currentSession.setNewOffController();
            return new Response("String", "Constructor is called");
        }

        if (currentSession.getNewOffController() == null) {
            return new Response("Error", "Constructor has not been called");
        }
        return currentSession.getNewOffController().processQuery(query);
    }

    private Response processNewManagerController(Query query, Session currentSession) {
        if (query.getMethodName().equals("new")) {
            currentSession.setNewManagerController();
            return new Response("String", "Constructor is called");
        }

        if (currentSession.getNewManagerController() == null) {
            return new Response("Error", "Constructor has not been called");
        }
        return currentSession.getNewManagerController().processQuery(query);
    }

    private Response processEditProductController(Query query, Session currentSession) {
        if (query.getMethodName().equals("new")) {
            currentSession.setEditProductController();
            return new Response("String", "Constructor is called");
        }

        if (currentSession.getEditProductController() == null) {
            return new Response("Error", "Constructor has not been called");
        }
        return currentSession.getEditProductController().processQuery(query);
    }

    private Response processDiscountController(Query query, Session currentSession) {
        if (query.getMethodName().equals("new")) {
            currentSession.setDiscountController();
            return new Response("String", "Constructor is called");
        }

        if (currentSession.getDiscountController() == null) {
            return new Response("Error", "Constructor has not been called");
        }
        return currentSession.getDiscountController().processQuery(query);
    }

    private Response processCategoryController(Query query, Session currentSession) {
        if (query.getMethodName().equals("new")) {
            currentSession.setCategoryController();
            return new Response("String", "Constructor is called");
        }

        if (currentSession.getCategoryController() == null) {
            return new Response("Error", "Constructor has not been called");
        }
        return currentSession.getCategoryController().processQuery(query);
    }

    private String addSession() {
        Session session = new Session(new GlobalVariables());
        Date date = new Date();
        String token = String.valueOf(date.getTime());
        this.clients.put(token, session);
        return token;
    }
}
