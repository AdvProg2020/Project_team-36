package Models;

public class Score {
    private Customer customer;
    private double score;

    public Score(Customer customer, double score) {
        this.customer = customer;
        this.score = score;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getScore() {
        return score;
    }
}
