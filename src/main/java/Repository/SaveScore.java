package Repository;

import Models.Customer;
import Models.Score;
import Models.User;

public class SaveScore {
    private int customerId;
    private int score;

    public SaveScore(Score score) {
        this.customerId = score.getCustomer().getUserId();
        this.score = score.getScore();
    }

    public Score generateScore(){
        return new Score(SaveCustomer.load(customerId),score);
    }
}
