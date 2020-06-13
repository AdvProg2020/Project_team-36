package GUI;

import Models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ProductController implements Initializable {
    @FXML
    ImageView imageView;
    @FXML
    Label name;
    @FXML
    Label company;
    @FXML
    Label rate;
    @FXML
    Label numberseen;
    @FXML
    ScrollPane fieldsOfCategory;
    @FXML
    ScrollPane comments;
    @FXML
    ScrollPane productFields;
    @FXML
    Button back;
    @FXML
    Button login;
    @FXML
    Button addToCart;


    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    @Override
    public void initialize(int id) {
        Product product = Product.getProduct(id);

    }
}

