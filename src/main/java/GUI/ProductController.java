package GUI;

import Models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;




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
    private Button back;
    @FXML
    private Button login;
    @FXML
    private Button logout;
    @FXML
    private Button cart;
    @FXML
    private Button account;
    @FXML
    private VBox productFieldsVBox;
    @FXML
    private Text info;
    @FXML
    private VBox fieldsOfCategoryVBox;
    @FXML
    private CommentsController commentsController;
    @FXML
    private HBox header;

    public void back() throws IOException {
        Constants.getGuiManager().back();
    }

    @Override
    public void initialize(int id) throws IOException {
        Product product = Product.getProduct(id);
        reloadHeader();
        imageView .setImage( product.getProductImage().getImage());
        name.setText(product.getName());
        company.setText(product.getCompany());
        numberseen.setText("" + product.getSeenNumber());
        info.setText(product.getInformation());
        fillFieldsOfCategory(product.getFieldsOfCategory(), fieldsOfCategoryVBox);
        fillProductFields(product.getProductFields(),productFieldsVBox);
        commentsController.fill(product);
    }

    private void fillProductFields(ArrayList<ProductField> productFields, VBox vBox) throws IOException {
        for (ProductField productField : productFields) {
            if (productField.getSupply() > 0){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ProductField.fxml"));
                Parent parent = fxmlLoader.load();
                ((ProductFieldController) fxmlLoader.getController()).fill(productField);
                vBox.getChildren().add(parent);
            }
        }
    }

    private void fillFieldsOfCategory(ArrayList<Field> fields, VBox vBox) {
        for (Field field : fields) {
            HBox hBox = new HBox();
            Label name = new Label();
            Label value = new Label();
            Region region = new Region();
            hBox.getChildren().addAll(name, region, value);
            hBox.setPadding(new Insets(8));
            hBox.setHgrow(region, Priority.ALWAYS);
            if (field instanceof IntegerField) {
                IntegerField integerField = (IntegerField) field;
                name.setText(integerField.getName() + ":");
                value.setText("" + integerField.getQuantity());
            } else {
                OptionalField optionalField = (OptionalField) field;
                name.setText(optionalField.getName() + ":");
                value.setText(optionalField.getQuality());
            }
            vBox.getChildren().add(hBox);
        }
    }

    private void reloadHeader(){
        if(Constants.globalVariables.getLoggedInUser() == null){
            header.getChildren().remove(logout);
            header.getChildren().remove(account);
        }else {
            header.getChildren().remove(login);
        }
    }

    public void login(){
        Constants.getGuiManager().login();
    }

    public void logout(){
        Constants.getGuiManager().logout();
    }

    public void goToCart() throws IOException {
        Constants.getGuiManager().open("Cart",1);
    }

    public void goToAccount() throws IOException {
        Constants.getGuiManager().open("Account",1);
    }
}

