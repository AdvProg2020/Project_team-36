package Repository;

import Models.Customer;
import Models.Score;

public class SaveScore {
    private int customerId;
    private int score;

    public SaveScore(Score score) {
        this.customerId = score.getCustomer().getUserId();
        this.score = score.getScore();
    }
}
