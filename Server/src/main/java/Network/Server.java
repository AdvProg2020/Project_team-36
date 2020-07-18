package Network;

import Controllers.GlobalVariables;
import Models.*;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

            case "GetById":
                return processGetById(query);

            case "GetAllById":
                return processGetAllById(query);

            default:
                return new Response("Error", "");
        }
    }

//    private Response processGetAllById(Query query) {
//        String returnType;
//        Gson gson1 = new GsonBuilder().create();
//        String output;
//        switch (query.getMethodName()) {
//            case "Category":
//                returnType = "Category";
//                output = gson1.toJson(Category.getCategoryById(id));
//                break;
//
//            case "Customer":
//                returnType = "Customer";
//                output = gson1.toJson(Customer.getCustomerById(id));
//                break;
//
//            case "Discount":
//                returnType = "Discount";
//                output = gson1.toJson(Discount.getDiscountById(id));
//                break;
//
//            case "Manager":
//                returnType = "Manager";
//                output = gson1.toJson(Manager.getManagerById(id));
//                break;
//
//            case "Product":
//                returnType = "Product";
//                output = gson1.toJson(Product.getProductById(id));
//                break;
//
//            case "Request":
//                returnType = "Request";
//                output = gson1.toJson(Request.getRequestById(id));
//                break;
//
//            case "Sale":
//                returnType = "Sale";
//                output = gson1.toJson(Sale.getSaleById(id));
//                break;
//
//            case "Seller":
//                returnType = "Seller";
//                output = gson1.toJson(Seller.getSellerById(id));
//                break;
//
//            case "SellerLog":
//                returnType = "SellerLog";
//                output = gson1.toJson(SellerLog.getSellerLogById(id));
//                break;
//
//            case "User":
//                returnType = "User";
//                output = gson1.toJson(User.getUserById(id));
//                break;
//
//            default:
//                returnType = "Error";
//                output = "";
//        }
//        return new Response(returnType,output);
//    }

    private Response processGetById(Query query) {
        String returnType;
        int id = Integer.parseInt(query.getMethodInputs().get("id"));
        Gson gson1 = new GsonBuilder().create();
        String output;
        switch (query.getMethodName()) {
            case "Category":
                returnType = "Category";
                output = gson1.toJson(new SaveCategory(Category.getCategoryById(id)));
                break;

            case "Customer":
                returnType = "Customer";
                output = gson1.toJson(new SaveCustomer(Customer.getCustomerById(id)));
                break;

            case "Discount":
                returnType = "Discount";
                output = gson1.toJson(new SaveDiscount(Discount.getDiscountById(id)));
                break;

            case "Manager":
                returnType = "Manager";
                output = gson1.toJson(new SaveManager(Manager.getManagerById(id)));
                break;

            case "Product":
                returnType = "Product";
                output = gson1.toJson(new SaveProduct(Product.getProductById(id)));
                break;

            case "Request":
                returnType = "Request";
                output = gson1.toJson(new SaveRequest(Request.getRequestById(id)));
                break;

            case "Sale":
                returnType = "Sale";
                output = gson1.toJson(new SaveSale(Sale.getSaleById(id)));
                break;

            case "Seller":
                returnType = "Seller";
                output = gson1.toJson(new SaveSeller(Seller.getSellerById(id)));
                break;

            case "SellerLog":
                returnType = "SellerLog";
                output = gson1.toJson(new SaveSellerLog(SellerLog.getSellerLogById(id)));
                break;

            case "User":
                returnType = "User";
                output = gson1.toJson(new SaveUser(User.getUserById(id)));
                break;

            default:
                returnType = "Error";
                output = "";
        }
        return new Response(returnType,output);
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
