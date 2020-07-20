package Bank;

import Models.User;

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
        System.out.println("Port number: "+port);
        System.out.println("IP Address: "+ InetAddress.getLocalHost().getHostAddress());
        BankDatabase bankDatabase = BankDatabase.getInstance();
        ClientHandler.setBankDatabase(bankDatabase);
        while (true) {
            Socket socket = serverSocket.accept();
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            new ClientHandler(socket,dataInputStream,dataOutputStream);
        }
    }

}

class ClientHandler extends Thread {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private static BankDatabase bankDatabase;

    public ClientHandler(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream ) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.socket = socket;
    }

    public static void setBankDatabase(BankDatabase bankDatabase) {
        ClientHandler.bankDatabase = bankDatabase;
    }

    @Override
    public void run(){
       while(true) {
            String input = "";
            try {
                input = dataInputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String [] splited = input.split(" ");
            if (input.startsWith("create_account")) {
                createAccount(splited[1],splited[2],splited[3],splited[4],splited[5]);
            }else if(input.startsWith("get_token")){
                getToken(splited[1],splited[2]);
            }else if(input.startsWith("create_receipt")){
                createReceipt(splited);
            }else if(input.startsWith("get_transactions")){
                getTransactions(splited[1],splited[2]);
            }else if(input.startsWith("pay")){
                pay(splited[1]);
            }else if(input.startsWith("get_balance")){
                getBalance(splited[1]);
            }else if(input.startsWith("exit")){
                exit();
                break;
            } else{
                invalidInput();
            }
        }
    }

    private void sendResponse(String response){
        try {
            dataOutputStream.writeUTF(response);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAccount(String firstname,String lastname,String username,String password,String rePassword){
        if(!password.equals(rePassword)){
            sendResponse("passwords do not match");
            return;
        }
        BankUser bankUser = new BankUser(username,password,firstname,lastname);
        try {
            bankDatabase.addNewUser(bankUser);
            sendResponse(Integer.toString(bankUser.getAccountId()));
        } catch (BankDatabase.ThereIsUserException e) {
            sendResponse("username is not available");
        }
    }

    private void getToken(String username,String password){
        BankUser bankUser = null;
        try {
             bankUser = bankDatabase.getUser(username);
            if(!bankUser.getPassword().equals(password))
                throw new BankDatabase.NoUserWithUsername();
        } catch (BankDatabase.NoUserWithUsername noUserWithUsername) {
            sendResponse("username or password is invalid");
        }
        bankDatabase.addToken(bankUser,new Token(bankUser.getUsername()));

    }

    private void createReceipt(String [] input){

    }

    private void getTransactions(String token,String type){

    }

    private void pay(String receiptId){

    }

    private void getBalance(String token){

    }

    private void exit(){

    }

    private void invalidInput(){
        sendResponse("invalid input");
    }

}
