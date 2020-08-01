package Bank;

import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class BankServer {


    public static void main(String[] args) throws IOException {
        System.out.println("enter port number");
        Scanner scanner = new Scanner(System.in);
        ServerSocket serverSocket = null;
        int port = scanner.nextInt();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Port number: " + port);
        System.out.println("IP Address: " + InetAddress.getLocalHost().getHostAddress());
        BankDatabase bankDatabase = BankDatabase.getInstance();
        ClientHandler.setBankDatabase(bankDatabase);
        while (true) {
            Socket socket = serverSocket.accept();
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            new ClientHandler(socket, dataInputStream, dataOutputStream).start();
        }
    }

}

class ClientHandler extends Thread {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private static BankDatabase bankDatabase;

    public ClientHandler(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.socket = socket;
    }

    public static void setBankDatabase(BankDatabase bankDatabase) {
        ClientHandler.bankDatabase = bankDatabase;
    }

    @Override
    public void run() {
        while (true) {
            String input = "";
            try {
                input = dataInputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] splited = input.split(" ");
            if (input.startsWith("create_account")) {
                createAccount(splited[1], splited[2], splited[3], splited[4], splited[5]);
            } else if (input.startsWith("get_token")) {
                getToken(splited[1], splited[2]);
            } else if (input.startsWith("create_receipt")) {
                createReceipt(splited);
            } else if (input.startsWith("get_transactions")) {
                getTransactions(splited[1], splited[2]);
            } else if (input.startsWith("pay")) {
                pay(splited[1]);
            } else if (input.startsWith("get_balance")) {
                getBalance(splited[1]);
            } else if (input.startsWith("exit")) {
                exit();
                break;
            } else {
                invalidInput();
            }
        }
    }

    private void sendResponse(String response) {
        try {
            dataOutputStream.writeUTF(response);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAccount(String firstname, String lastname, String username, String password, String rePassword) {
        if (!password.equals(rePassword)) {
            sendResponse("passwords do not match");
            return;
        }
        BankUser bankUser = new BankUser(username, password, firstname, lastname);
        try {
            bankDatabase.addNewUser(bankUser);
            sendResponse(Integer.toString(bankUser.getAccountId()));
        } catch (BankDatabase.ThereIsUserException e) {
            sendResponse("username is not available");
        }
    }

    private void getToken(String username, String password) {
        BankUser bankUser = null;
        try {
            bankUser = bankDatabase.getUser(username);
            if (!bankUser.getPassword().equals(password))
                throw new BankDatabase.NoUserWithUsername();
            Token token = new Token(bankUser.getUsername());
            bankDatabase.addToken(bankUser, token);
            Gson gson = new Gson();
            sendResponse(gson.toJson(token));
        } catch (BankDatabase.NoUserWithUsername noUserWithUsername) {
            sendResponse("username or password is invalid");
        }
    }

    private void createReceipt(String[] input) {
        String type = input[2];
        if (!type.matches("^deposit|withdraw|move$")) {
            sendResponse("invalid receipt type");
            return;
        }
        String money = input[3];
        if (!money.matches("^\\d{1,18}$")) {
            sendResponse("invalid money");
            return;
        }
        if (input.length != 7 && input.length != 6) {
            sendResponse("invalid parameters passed");
            return;
        }
        if (!input[4].matches("^\\d+|-1") || !input[5].matches("^\\d+|-1")) {
            sendResponse("invalid parameters passed");
            return;
        }
        BankUser bankUser;
        Gson gson = new Gson();
        Token token;
        try {
            token = gson.fromJson(input[1], Token.class);
            bankUser = bankDatabase.validateToken(token);
        } catch (BankDatabase.TokenExpired tokenExpired) {
            sendResponse("token expired");
            return;
        } catch (Exception invalidToken) {
            sendResponse("token is invalid");
            return;
        }
        if (type.matches("withdraw|move") && bankUser.getAccountId() != Integer.parseInt(input[4])) {
            sendResponse("token is invalid");
            return;
        }
        if (type.matches("deposit")) {
            if (!validateDeposit(input[4], input[5]))
                return;
        } else if (type.matches("withdraw")) {
            if (!validateWithdraw(input[4], input[5]))
                return;
        } else if (type.matches("move")) {
            if (!validateMove(input[4], input[5]))
                return;
        }
        String description = "";
        if (input.length == 7) {
            description = input[6];
            if (!description.matches("^[A-Za-z1-9\\s.]+$")) {
                sendResponse("your input contains invalid characters");
                return;
            }
        }
        Transaction transaction = new Transaction(type, Long.parseLong(money), Integer.parseInt(input[4]), Integer.parseInt(input[5]), description);
        bankDatabase.addTransaction(transaction);
        sendResponse(Integer.toString(transaction.getTransactionId()));
    }

    private boolean validateDeposit(String source, String dest) {
        if (Integer.parseInt(source) != -1) {
            sendResponse("source account id is invalid");
            return false;
        } else if (Integer.parseInt(dest) == -1) {
            sendResponse("invalid account id");
            return false;
        } else if (!bankDatabase.isThereAccount(Integer.parseInt(dest))) {
            sendResponse("dest account id is invalid");
            return false;
        }
        return true;
    }

    private boolean validateWithdraw(String source, String dest) {
        if (Integer.parseInt(dest) != -1) {
            sendResponse("dest account id is invalid");
            return false;
        } else if (Integer.parseInt(source) == -1) {
            sendResponse("invalid account id");
            return false;
        } else if (!bankDatabase.isThereAccount(Integer.parseInt(source))) {
            sendResponse("source account id is invalid");
            return false;
        }
        return true;
    }

    private boolean validateMove(String source, String dest) {
        int sourceId = Integer.parseInt(source);
        int destId = Integer.parseInt(dest);
        if (sourceId == destId) {
            sendResponse("equal source and dest account");
            return false;
        }
        if (sourceId == -1 || destId == -1) {
            sendResponse("invalid account id");
            return false;
        }
        if (!bankDatabase.isThereAccount(sourceId)) {
            sendResponse("source account id is invalid");
            return false;
        }
        if (!bankDatabase.isThereAccount(destId)) {
            sendResponse("dest account id is invalid");
            return false;
        }
        return true;
    }

    private void getTransactions(String token, String type) {
        Gson gson = new Gson();
        Token validateToken;
        BankUser bankUser;
        try {
            validateToken = gson.fromJson(token, Token.class);
            bankUser = bankDatabase.validateToken(validateToken);
        } catch (BankDatabase.TokenExpired tokenExpired) {
            sendResponse("token expired");
            return;
        } catch (Exception invalidToken) {
            sendResponse("token is invalid");
            return;
        }
        Gson gsonBuilder = new GsonBuilder().create();
        if (type.equals("+")) {
            sendResponse(gsonBuilder.toJson(bankUser.getDeposits()));
        } else if (type.equals("-")) {
            sendResponse(gsonBuilder.toJson(bankUser.getWithdrawals()));
        } else if (type.equals("*")) {
            sendResponse(gsonBuilder.toJson(bankUser.getAllTransactions()));
        } else {
            try {
                sendResponse(gsonBuilder.toJson(bankUser.getTransaction(type)));
            } catch (BankUser.InvalidReceiptId invalidReceiptId) {
                sendResponse("invalid receipt id");
            }
        }
    }

    private void pay(String receiptId) {
        int id;
        Transaction transaction;
        try {
            id = Integer.parseInt(receiptId);
            transaction = bankDatabase.getTransaction(id);
        } catch (NumberFormatException | BankDatabase.NoTransaction numberFormatException) {
            sendResponse("invalid receipt id");
            return;
        }
        if (transaction.isPaid()) {
            sendResponse("receipt is paid before");
            return;
        }
        int sourceId = transaction.getSourceAccountId();
        int destId = transaction.getDestAccountId();
        BankUser sourceUser = null;
        BankUser destUser = null;
        try {
            if (sourceId != -1)
                sourceUser = bankDatabase.getUser(sourceId);
            if (destId != -1)
                destUser = bankDatabase.getUser(destId);
        } catch (BankDatabase.NoUserWithID noUserWithID) {
            sendResponse("invalid account id");
            return;
        }
        synchronized (bankDatabase) {
            if (sourceId == -1) {
                destUser.addMoney(transaction.getMoney());
                destUser.addTransaction(transaction);
            } else {
                try {
                    sourceUser.withdrawMoney(transaction.getMoney());
                    sourceUser.addTransaction(transaction);
                    if (destId != -1) {
                        destUser.addMoney(transaction.getMoney());
                        destUser.addTransaction(transaction);
                    }
                } catch (BankUser.NotEnoughMoney notEnoughMoney) {
                    sendResponse("source account does not have enough money");
                    return;
                }
            }
        }
        transaction.paid();
        sendResponse("done successfully");

    }

    private void getBalance(String token) {
        BankUser bankUser;
        Gson gson = new Gson();
        Token validToken;
        try {
            validToken = gson.fromJson(token, Token.class);
            bankUser = bankDatabase.validateToken(validToken);
        } catch (BankDatabase.TokenExpired tokenExpired) {
            sendResponse("token expired");
            return;
        } catch (Exception invalidToken) {
            sendResponse("token is invalid");
            return;
        }
        sendResponse(Long.toString(bankUser.getBalance()));
    }

    private void exit() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void invalidInput() {
        sendResponse("invalid input");
    }

}
