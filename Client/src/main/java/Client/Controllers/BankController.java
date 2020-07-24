package Client.Controllers;

import Client.GUI.Constants;
import Client.Network.Client;
import Models.Query;
import Models.Response;

public class BankController {
    private String controllerName = "BankController";
    private String token = Constants.globalVariables.getToken();


    public String createAccount(String firstname, String lastname, String username, String password, String repeatPassword)  {
        Query query = new Query(token,controllerName,"createAccount");
        query.getMethodInputs().put("firstname",firstname);
        query.getMethodInputs().put("lastname",lastname);
        query.getMethodInputs().put("password",password);
        query.getMethodInputs().put("username",username);
        query.getMethodInputs().put("repeatPassword",repeatPassword);
        Response response = Client.process(query);
        return response.getData();
    }

    public String getToken(String username, String password) {
        Query query = new Query(token,controllerName,"getToken");
        query.getMethodInputs().put("password",password);
        query.getMethodInputs().put("username",username);
        Response response = Client.process(query);
        return response.getData();
    }

    public String createReceiptAndPay(String token,String receiptType,String money,String sourceID
            ,String destID,String description) {
        Query query = new Query(token,controllerName,"createReceiptAndPay");
        query.getMethodInputs().put("token",token);
        query.getMethodInputs().put("receiptType",receiptType);
        query.getMethodInputs().put("money",money);
        query.getMethodInputs().put("sourceID",sourceID);
        query.getMethodInputs().put("destID",destID);
        query.getMethodInputs().put("description",description);
        Response response = Client.process(query);
        return response.getData();
    }

    public String getTransactions(String token,String type) {
        Query query = new Query(token,controllerName,"getTransactions");
        query.getMethodInputs().put("token",token);
        query.getMethodInputs().put("type",type);
        Response response = Client.process(query);
        return response.getData();
    }

    public String  getBalance(String token) {
        Query query = new Query(token,controllerName,"getBalance");
        query.getMethodInputs().put("token",token);
        Response response = Client.process(query);
        return response.getData();
    }

    public void exit(){
        // TODO: bebinim socket chi mishe
    }
}
