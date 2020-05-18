package Repository;

import Models.Comment;
import Models.Product;
import Models.Status;
import Models.User;

public class SaveComment {
    private int userId;
    private int productId;
    private String title;
    private String comment;
    private Enum<Status> ConfirmationStatus;
    private boolean hasBought;

    public SaveComment(Comment comment) {
        this.userId = comment.getUser().getUserId();
        this.productId = comment.getProduct().getProductId();
        this.title = comment.getTitle();
        this.comment = comment.getComment();
        this.ConfirmationStatus = comment.getConfirmationStatus();
        this.hasBought = comment.isHasBought();
    }

    public Comment generateComment(){
        return null;
    }
}
