package Network;

import Controllers.GlobalVariables;
import Models.*;
import Repository.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private Map<String, Session> clients;
    private ServerSocket serverSocket;

    public Server() throws IOException {
        this.clients = new HashMap<>();
        this.serverSocket = new ServerSocket(8080);
    }

    public void run() {
        int counter = 0;
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
                dataOutputStream.flush();
                socket.close();
                counter++;
                if (counter % 20 == 0) {
                    RepositoryManager.saveData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response process(String data) {
        Gson gson = new Gson();
        Query query = gson.fromJson(data, Query.class);

        if (query.getControllerName().equals("SessionController") && query.getMethodName().equals("addSession")) {
            return new Response("String", addSession());
        }

        if (!clients.containsKey(query.getToken())) {
            return new Response("Error", "TokenError");
        }

        Session currentSession = clients.get(query.getToken());

        switch (query.getControllerName()) {
            case "SessionController":
                return processSessionController(query, currentSession);
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

    private Response processSessionController(Query query, Session currentSession) {
        switch (query.getMethodName()) {
            case "getOnlineUsers":
                return processGetOnlineUsers(currentSession);

            case "getOnlineSupporters":
                return processGetOnlineSupporters();

            case "close":
                clients.remove(query.getToken(),currentSession);
                return new Response("String","Program is closed");

            default:
                return new Response("Error","");
        }
    }

    private Response processGetOnlineSupporters() {
        List<SaveSupporter> onlineSupporters = new ArrayList<>();
        for (String token : clients.keySet()) {
            Session session = clients.get(token);
            if (session.getGlobalVariables().getLoggedInUser() instanceof Supporter) {
                onlineSupporters.add(new SaveSupporter((Supporter) session.getGlobalVariables().getLoggedInUser()));
            }
        }
        Gson gson1 = new GsonBuilder().create();
        return new Response("List<Supporter>", gson1.toJson(onlineSupporters));
    }

    private Response processGetOnlineUsers(Session currentSession) {
        if (currentSession.getGlobalVariables().getLoggedInUser() instanceof Manager) {
            List<SaveUser> onlineUsers = new ArrayList<>();
            for (String token : clients.keySet()) {
                Session session = clients.get(token);
                if (session.getGlobalVariables().getLoggedInUser() != null) {
                    onlineUsers.add(new SaveUser(session.getGlobalVariables().getLoggedInUser()));
                }
            }
            Gson gson1 = new GsonBuilder().create();
            return new Response("List<User>", gson1.toJson(onlineUsers));
        } else {
            return new Response("Error", "Access denied");
        }
    }

    private Response processGetAllById(Query query) {
        String returnType;
        Gson gson1 = new GsonBuilder().create();
        String output;
        switch (query.getMethodName()) {
            case "Category":
                returnType = "List<Category>";
                List<SaveCategory> allCategories = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allCategories.add(new SaveCategory(Category.getCategoryById(Integer.parseInt(id)))));
                output = gson1.toJson(allCategories);
                break;


            case "Customer":
                returnType = "List<Customer>";
                List<SaveCustomer> allCustomers = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allCustomers.add(new SaveCustomer(Customer.getCustomerById(Integer.parseInt(id)))));
                output = gson1.toJson(allCustomers);
                break;

            case "Discount":
                returnType = "List<Discount>";
                List<SaveDiscount> allDiscounts = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allDiscounts.add(new SaveDiscount(Discount.getDiscountById(Integer.parseInt(id)))));
                output = gson1.toJson(allDiscounts);
                break;

            case "Manager":
                returnType = "List<Manager>";
                List<SaveManager> allManagers = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allManagers.add(new SaveManager(Manager.getManagerById(Integer.parseInt(id)))));
                output = gson1.toJson(allManagers);
                break;

            case "Product":
                returnType = "List<Product>";
                List<SaveProduct> allProducts = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allProducts.add(new SaveProduct(Product.getProductById(Integer.parseInt(id)))));
                output = gson1.toJson(allProducts);
                break;

            case "Request":
                returnType = "List<Request>";
                List<SaveRequest> allRequests = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allRequests.add(new SaveRequest(Request.getRequestById(Integer.parseInt(id)))));
                output = gson1.toJson(allRequests);
                break;

            case "Sale":
                returnType = "List<Sale>";
                List<SaveSale> allSales = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allSales.add(new SaveSale(Sale.getSaleById(Integer.parseInt(id)))));
                output = gson1.toJson(allSales);
                break;

            case "Seller":
                returnType = "List<Seller>";
                List<SaveSeller> allSellers = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allSellers.add(new SaveSeller(Seller.getSellerById(Integer.parseInt(id)))));
                output = gson1.toJson(allSellers);
                break;

            case "SellerLog":
                returnType = "List<SellerLog>";
                List<SaveSellerLog> allSellerLogs = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allSellerLogs.add(new SaveSellerLog(SellerLog.getSellerLogById(Integer.parseInt(id)))));
                output = gson1.toJson(allSellerLogs);
                break;

            case "User":
                returnType = "List<User>";
                List<SaveUser> allUsers = new ArrayList<>();
                query.getMethodInputs().keySet().forEach(id -> allUsers.add(new SaveUser(User.getUserById(Integer.parseInt(id)))));
                output = gson1.toJson(allUsers);
                break;

            default:
                returnType = "Error";
                output = "";
        }
        return new Response(returnType, output);
    }

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
        return new Response(returnType, output);
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
