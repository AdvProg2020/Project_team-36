package Models;

public class Comment {
    private User user;
    private Product product;
    private String comment;
    private Enum<ConfirmationStatus> ConfirmationStatus;
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

    public Enum<Models.ConfirmationStatus> getConfirmationStatus() {
        return ConfirmationStatus;
    }

    public boolean isHasBought() {
        return hasBought;
    }

    //-..-
}
