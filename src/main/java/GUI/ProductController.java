package GUI;

import Controllers.CustomerController;
import Models.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;


public class ProductController implements Initializable {
    @FXML
    private HBox vote;
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
        product.seen();
        reloadHeader();
        imageView.setImage(product.getProductImage().getImage());
        name.setText(product.getName());
        company.setText(product.getCompany());
        numberseen.setText("" + product.getSeenNumber());
        info.setText(product.getInformation());
        fillFieldsOfCategory(product.getFieldsOfCategory(), fieldsOfCategoryVBox);
        fillProductFields(product.getProductFields(), productFieldsVBox);
        commentsController.fill(product);
        fillScore(product.getScore());
        enableVote(product);
    }

    private void fillProductFields(ArrayList<ProductField> productFields, VBox vBox) throws IOException {
        for (ProductField productField : productFields) {
            if (productField.getSupply() > 0) {
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

    private void reloadHeader() {
        if (Constants.globalVariables.getLoggedInUser() == null) {
            header.getChildren().remove(logout);
            header.getChildren().remove(account);
            header.getChildren().remove(cart);
        } else {
            header.getChildren().remove(login);
        }
    }

    private void fillScore(double score) {
        rate.getChildren().clear();
        int completeStars = (int) score;
        double halfStar = score - completeStars;
        for (int i = 0; i < completeStars; i++) {
            ImageView star = new ImageView(new Image("images/star.png"));
            star.setFitWidth(70);
            star.setFitHeight(56);
            rate.getChildren().add(star);
        }
        ImageView star = new ImageView();
        star.setFitHeight(56);
        star.setFitWidth(70);
        if (halfStar > 0.6) {
            star.setImage(new Image("images/starPrim.png"));
            rate.getChildren().add(star);
        } else if (halfStar < 0.4 && halfStar > 0) {
            star.setImage(new Image("images/starPrimPrim.png"));
            rate.getChildren().add(star);
        } else if (halfStar != 0) {
            star.setImage(new Image("images/halfStar.png"));
            rate.getChildren().add(star);
        }
    }

    private void enableVote(Product product) {
        if (!Constants.productsController.canRate(product, Constants.globalVariables.getLoggedInUser())) {
            return;
        }
        for(int i=1;i<6;i++){
            ImageView imageView = new ImageView();
            imageView.setFitWidth(70);
            imageView.setFitHeight(56);
            setVotePiece(i, imageView,product);
            vote.getChildren().add(imageView);
        }
    }

    private void setVotePiece(int number, ImageView imageView,Product product){
        imageView.setOnMouseEntered(mouseEvent -> Constants.getGuiManager().getStage().getScene().setCursor(new ImageCursor(new Image("clickMe.png"))));
        imageView.setOnMouseClicked(mouseEvent -> {
            try {
                Constants.customerController.rateProduct(product.getProductId(),number);
                vote.getChildren().clear();
                Label label = new Label("Thanks for your vote!");
                label.setStyle("-fx-font-weight: bold;-fx-font-size: 30");
                vote.getChildren().add(label);
                fillScore(product.getScore());
            } catch (CustomerController.NoProductWithIdInLog noProductWithIdInLog) {
                noProductWithIdInLog.printStackTrace();
            }
        });
        Image image;
        if(number==1)
            image = new Image("images/angry.png");
        else if(number==2)
            image = new Image("images/sad.png");
        else if(number==3)
            image = new Image("images/poker.png");
        else if(number==4)
            image = new Image("images/nimHappy.png");
        else
            image = new Image("images/happy.png");
        imageView.setImage(image);

    }

    public void login() {
        Constants.getGuiManager().login();
    }

    public void logout() {
        Constants.getGuiManager().logout();
    }

    public void goToCart() throws IOException {
        Constants.getGuiManager().open("Cart", Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void goToAccount() throws IOException {
        User user = Constants.globalVariables.getLoggedInUser();
        if (user instanceof Manager) {
            Constants.getGuiManager().open("ManagerTemplate", user.getUserId());
        } else if (user instanceof Seller) {
            Constants.getGuiManager().open("SellerTemplate", user.getUserId());
        } else if (user instanceof Customer) {
            Constants.getGuiManager().open("CustomerTemplate", user.getUserId());
        }
    }
}

