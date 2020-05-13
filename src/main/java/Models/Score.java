package Models;

public class Score {
    private Customer customer;
    private int score;

    public Score(Customer customer, int score) {
        this.customer = customer;
        this.score = score;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getScore() {
        return score;
    }

    //-..-
}
