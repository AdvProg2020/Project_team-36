package Client.GUI;

import Client.Controllers.EntryController;
import Client.Models.Comment;
import Client.Models.Product;
import Models.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CommentsController {
    @FXML
    private TextField title;
    @FXML
    private TextArea comment;
    @FXML
    private VBox vBox;
    @FXML
    private HBox submitBar;
    @FXML
    private VBox submitBox;

    public void fill(Product product) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null){
            title.setEditable(false);
            comment.setEditable(false);
            submitBox.getChildren().remove(submitBar);
        }
        List<Comment> comments = product.getAllComments();
        vBox.getChildren().clear();
        for (Comment comment : comments) {
            if (comment.getConfirmationStatus() == Status.CONFIRMED){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Comment.fxml"));
                Parent parent = fxmlLoader.load();
                ((CommentController) fxmlLoader.getController()).fill(comment);
                vBox.getChildren().add(parent);
            }
        }
    }

    public void submit(){
        try {
            Constants.productsController.addComment(title.getText(),comment.getText());
            AlertBox.display("Comment","Your comment will be reviewed by the manager :)))");
        } catch (EntryController.NotLoggedInException e) {
            AlertBox.display("Error","You have to login in order to add comment!");
        }
    }
}
