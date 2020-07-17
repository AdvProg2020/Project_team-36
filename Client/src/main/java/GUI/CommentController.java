package GUI;

import Models.Comment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class CommentController {
    @FXML
    private Label name;
    @FXML
    private Label title;
    @FXML
    private Label hasBought;
    @FXML
    private Text comment;

    public void fill(Comment comment){
        name.setText(comment.getUser().getFirstname() + " " + comment.getUser().getLastname());
        title.setText(comment.getTitle());
        this.comment.setText(comment.getComment());
        if (comment.isHasBought()){
            hasBought.setText("This user has purchased this product");
        }else {
            hasBought.setText("This user has not purchased this product");
        }
    }
}
