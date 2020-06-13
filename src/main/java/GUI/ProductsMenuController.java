package GUI;

import Controllers.ProductsController;
import Models.Product;
import View.Products.ProductsMenu;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ProductsMenuController implements Initializable {

    public CheckBox ascendingSort;
    private ProductsController productsController;
    private int pageNumber;
    public ComboBox sortBox;
    public HBox bottomPane;
    public ScrollPane scrollPane;

    public ProductsMenuController() {
        this.productsController = Constants.productsController;
    }

    @Override
    public void initialize(int id) {

    }


    public void sort(ActionEvent actionEvent) {
        ascendingSort.setDisable(false);
        String ascending = "descending";
        if (ascendingSort.isSelected()) {
            ascending = "ascending";
        }
        String name = "production date";
        if (!sortBox.getValue().toString().equals("--None--")) {
            name = sortBox.getValue().toString();
        }
        try {
            productsController.setSort(name, ascending);
        } catch (ProductsController.NoSortException e) {
            e.printStackTrace();
        }
        showProducts(productsController.geFinalProductsList());

    }

    public void ascendingSort(MouseEvent mouseEvent) {
        String ascending = "descending";
        if (ascendingSort.isSelected()) {
            ascending = "ascending";
        }
        String name = "production date";
        if (!sortBox.getValue().toString().equals("--None--")) {
            name = sortBox.getValue().toString();
        }
        try {
            productsController.setSort(name, ascending);
        } catch (ProductsController.NoSortException e) {
            e.printStackTrace();
        }
        showProducts(productsController.geFinalProductsList());
    }

    private void showProducts(ArrayList<Product> allProducts) {
        if (allProducts.isEmpty()) {
            ImageView imageView = new ImageView(new Image("images/noProduct.png"));
            scrollPane.setCenterShape(true);
            scrollPane.setContent(imageView);
            return;
        }
        int size = allProducts.size();
        GridPane gridPane = new GridPane();
        int rowCount = 0;
        if (size > 12) {
            rowCount = 4;
            if (size % 12 != 0) {
                addPageLayouts(size / 12 + 1);
            } else {
                addPageLayouts(size / 12);
            }
        } else {
            rowCount = size % 4 == 0 ? size / 4 : size / 4 + 1;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < rowCount && 4 * i + j < size; j++) {
                gridPane.add(productView(allProducts.get(i*4+j)),j,i);
            }
        }
        scrollPane.setContent(gridPane);
    }

    private void addPageLayouts(int count) {
    //TODO add pages
    }

    private VBox productView(Product product) {
        VBox vBox = new VBox();
        vBox.setPrefSize(265, 265);
        vBox.setAlignment(Pos.CENTER);
        ImageView imageView = product.getProductImage();
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        Label name = new Label(product.getName());
        Label price = new Label("lowest price: " + Double.toString(product.getLowestCurrentPrice()));
        HBox score = createProductScore(product.getScore());
        vBox.getChildren().addAll(imageView, name, price, score);
        return vBox;
    }

    private HBox createProductScore(double score) {
        HBox hBox = new HBox();
        int completeStars = (int) score;
        double halfStar = score - completeStars;
        for (int i = 0; i < completeStars; i++) {
            hBox.getChildren().add(new ImageView(new Image("star.png")));
        }
        if (halfStar > 0.6) {
            hBox.getChildren().add(new ImageView(new Image("starPrim.png")));
        } else if (halfStar < 0.4) {
            hBox.getChildren().add(new ImageView(new Image("starPrimPrim.png")));
        } else {
            hBox.getChildren().add(new ImageView(new Image("halfStar.png")));
        }
        hBox.setMaxSize(200, 20);
        return hBox;
    }
}
