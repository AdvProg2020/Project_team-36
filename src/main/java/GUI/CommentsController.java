package GUI;

import Controllers.EntryController;
import Models.Comment;
import Models.Product;
import Models.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class CommentsController {
    @FXML
    private TextField title;
    @FXML
    private TextArea comment;
    @FXML
    private VBox vBox;
    @FXML
    private HBox submitBar;

    public void fill(Product product) throws IOException {
        if (Constants.globalVariables.getLoggedInUser() == null){
            title.setEditable(false);
            comment.setEditable(false);
            vBox.getChildren().remove(submitBar);
        }
        ArrayList<Comment> comments = product.getAllComments();
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
        } catch (EntryController.NotLoggedInException e) {
            AlertBox.display("Error",e.getMessage());
        }
    }
}
