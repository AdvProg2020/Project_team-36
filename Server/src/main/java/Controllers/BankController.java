package Controllers;

import Models.Query;
import Models.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class BankController {
    private String host = "localhost";
    private int port = 8383;
    private GlobalVariables  globalVariables;

    public BankController(GlobalVariables globalVariables) {
        this.globalVariables = globalVariables;
    }

    public Response processQuery(Query query) {
        switch (query.getMethodName()){
            case "createAccount" :
                return processCreateAccount(query);

            case "getToken" :
                return processGetToken(query);

            case "createReceiptAndPay":
                return processCreateReceiptAndPay(query);

            case "getTransactions":
                return processGetTransactions(query);

            case "getBalance":
                return processGetBalance(query);

            case "exit":
                return processExit(query);

            default:
                return new Response("Errror","");
        }
    }

    private String connect(String command)  {

        Socket socket = null;
        try {
            socket = new Socket(host, port);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(command);
            dataOutputStream.flush();
            return dataInputStream.readUTF();
        } catch (IOException e) {
            return "IOException";
        }

    }

    public Response processCreateAccount(Query query)  {
        String firstname = query.getMethodInputs().get("firstname");
        String lastname = query.getMethodInputs().get("lastname");
        String username = query.getMethodInputs().get("username");
        String password = query.getMethodInputs().get("password");
        String reapeat_password = query.getMethodInputs().get("repeat_password");

        String command = "create_account" + " " + firstname + " " + lastname + " " +
                username + " " + password + " " + reapeat_password;
        return new Response("String", connect(command));
    }

    public Response processGetToken(Query query) {
        String username = query.getMethodInputs().get("username");
        String password = query.getMethodInputs().get("password");

        String command = "get_token" + " " + username + " " + password;
        String output = connect(command);
        if (!output.equals("\"invalid username or password\"") ){
            globalVariables.setBankToken(output);
        }
        return new Response("String", output);
    }

    public Response processCreateReceiptAndPay(Query query) {
        String token = query.getMethodInputs().get("token");
        String receiptType = query.getMethodInputs().get("receiptType");
        String money = query.getMethodInputs().get("money");
        String sourceID = query.getMethodInputs().get("sourceID");
        String destID = query.getMethodInputs().get("destID");
        String description = query.getMethodInputs().get("description");

        String command = "create_receipt" + " " + token + " " + receiptType + " " + money + " "
                + sourceID + " " + destID + " " + description;
        String receiptId = connect(command);
        try {
            int receiptID = Integer.parseInt(receiptId);
            String payCommand = "pay" + " " + receiptID;
            return new Response("String", connect(payCommand));
        } catch (Exception e) {
            return new Response("String", receiptId);
        }
    }

    public Response processGetTransactions(Query query) {
        String token = query.getMethodInputs().get("token");
        String type = query.getMethodInputs().get("type");

        String command = "get_transactions" + " " + token + " " + type;
        return new Response("String",connect(command));
    }

    public Response processGetBalance(Query query) {
        String token = query.getMethodInputs().get("token");

        String command = "get_balance" + " " + token;
        return new Response("String",connect(command));
    }

    public Response processExit(Query query){
        // TODO: bebinim socket chi mishe
        return null;
    }

}