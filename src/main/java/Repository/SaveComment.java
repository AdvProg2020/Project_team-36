package Repository;

import Models.Comment;
import Models.Status;

public class SaveComment {
    private int userId;
    private int productId;
    private String title;
    private String comment;
    private Status ConfirmationStatus;
    private boolean hasBought;

    public SaveComment(Comment comment) {
        this.userId = comment.getUser().getUserId();
        this.productId = comment.getProduct().getProductId();
        this.title = comment.getTitle();
        this.comment = comment.getComment();
        this.ConfirmationStatus = (Status) comment.getConfirmationStatus();
        this.hasBought = comment.isHasBought();
    }

    public Comment generateComment(){
        return new Comment(SaveUser.load(userId),SaveProduct.load(productId),title,comment,ConfirmationStatus,hasBought);
    }

    public int getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public Status getConfirmationStatus() {
        return ConfirmationStatus;
    }

    public boolean isHasBought() {
        return hasBought;
    }
}
