package Models;

import static Models.Status.TO_BE_CONFIRMED;

public class Comment implements Pendable {
    private User user;
    private Product product;
    private String title;
    private String comment;
    private Enum<Status> ConfirmationStatus;
    private boolean hasBought;

    public Comment(User user, Product product,String title, String comment,boolean hasBought) {
        this.user = user;
        this.product = product;
        this.title = title;
        this.comment = comment;
        ConfirmationStatus = TO_BE_CONFIRMED;
        this.hasBought = hasBought;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public String getTitle() {
        return title;
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


    @Override
    public String getPendingRequestType() {
        return "comment";
    }

    public Comment(User user, Product product, String title, String comment, Enum<Status> confirmationStatus, boolean hasBought) {
        this.user = user;
        this.product = product;
        this.title = title;
        this.comment = comment;
        ConfirmationStatus = confirmationStatus;
        this.hasBought = hasBought;
    }
}
