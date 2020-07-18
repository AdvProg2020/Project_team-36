package Models;

import GUI.Constants;
import Network.Client;
import Repository.SaveCustomer;
import Repository.SaveScore;
import com.google.gson.Gson;

public class Score {
    private SaveScore saveScore;
    private int score;

    public Score(SaveScore saveScore) {
        this.saveScore = saveScore;
       this.score = saveScore.getScore();
    }

    public SaveScore getSaveScore() {
        return saveScore;
    }

    public Customer getCustomer() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Customer");
        query.getMethodInputs().put("id", "" + saveScore.getCustomerId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Customer")) {
            Gson gson = new Gson();
            SaveCustomer saveCustomer = gson.fromJson(response.getData(), SaveCustomer.class);
            return new Customer(saveCustomer);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public int getScore() {
        return score;
    }
}
