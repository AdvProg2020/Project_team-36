package GUI;

import Models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ProductController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private Label name;
    @FXML
    private Label company;
    @FXML
    private HBox rate;
    @FXML
    private Label numberseen;
    @FXML
    private ScrollPane fieldsOfCategory;
    @FXML
    private ScrollPane comments;
    @FXML
    private ScrollPane productFields;
    @FXML
    private Button back;
    @FXML
    private Button login;
    @FXML
    private Button addToCart;
    @FXML
    private Button logout;
    @FXML
    private Button cart;
    @FXML
    private Button account;



    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    @Override
    public void initialize(int id) {
        Product product = Product.getProduct(id);

    }
}

