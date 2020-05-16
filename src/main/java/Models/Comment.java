package Models;

public class Comment {
    private User user;
    private Product product;
    private String comment;
    private Enum<Status> ConfirmationStatus;
    private boolean hasBought;

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public String getComment() {
        return comment;
    }

    public Enum<Status> getConfirmationStatus() {
        return ConfirmationStatus;
    }

    public boolean isHasBought() {
        return hasBought;
    }

    //-..-
}
