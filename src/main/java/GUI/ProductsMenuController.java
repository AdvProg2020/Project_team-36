package GUI;

import Controllers.ProductsController;
import Models.Product;
import Models.ProductField;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class ProductsMenuController implements Initializable {

    public CheckBox ascendingSort;
    private ProductsController productsController;
    private int page = 1;
    public ComboBox sortBox;
    public HBox bottomPane;
    public ScrollPane scrollPane;
    private ArrayList<Button> pageButtons = new ArrayList<>();

    public ProductsMenuController() {
        this.productsController = Constants.productsController;
    }

    @Override
    public void initialize(int id) {
        showAllProducts(productsController.getFinalProductsList());
    }

    private void showAllProducts(ArrayList<Product> allProducts) {
        if (allProducts.isEmpty()) {
            ImageView imageView = new ImageView(new Image("images/noProduct.png"));
            scrollPane.setCenterShape(true);
            scrollPane.setContent(imageView);
            return;
        }
        if (allProducts.size() > 16) {
            ArrayList<Product> temp = new ArrayList<>();
            int beginIndex = (page - 1) * 16;
            int endIndex = page * 16;
            for (int i = beginIndex; i < endIndex && i < allProducts.size(); i++) {
                temp.add(allProducts.get(i));
            }
            setPageButtons(allProducts.size() % 16 == 0 ? allProducts.size() / 16 : allProducts.size() / 16 + 1);
            showProducts(temp);
        } else {
            showProducts(allProducts);
            setPageButtons(1);
        }
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

        showAllProducts(productsController.getFinalProductsList());

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
        showAllProducts(productsController.getFinalProductsList());
    }

    private void showProducts(ArrayList<Product> allProducts) {
        int size = allProducts.size();
        GridPane gridPane = new GridPane();
        int rowCount;
        rowCount = size % 4 == 0 ? size / 4 : size / 4 + 1;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < 4&& 4 * i + j < size; j++) {
                gridPane.add(productView(allProducts.get(i * 4 + j)), j, i);//third parameter: column,forth parameter: row
            }
        }
        scrollPane.setContent(gridPane);
    }

    private void setPageButtons(int count) {
        pageButtons.clear();
        bottomPane.getChildren().clear();
        for (int i = 1; i <= count; i++) {
            Button button = new Button();
            button.setPrefSize(20, 20);
            button.setTextAlignment(TextAlignment.CENTER);
            button.setText(Integer.toString(i));
            bottomPane.getChildren().add(button);
            button.setOnMouseClicked(mouseEvent -> changePage(Integer.parseInt(button.getText())));
            pageButtons.add(button);
        }
    }

    private void changePage(int pageNumber) {
        page = pageNumber;
        for (Button button : pageButtons) {
            if (pageButtons.indexOf(button) == pageNumber - 1) {
                button.setStyle("-fx-border-width: 4px;-fx-border-color: red");
            } else
                button.setStyle("-fx-border-width: 4px;-fx-border-color: white");
        }
        showAllProducts(productsController.getFinalProductsList());
    }

    private VBox productView(Product product) {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: white");
        vBox.setOnMouseClicked(mouseEvent -> {
            int productId = Integer.parseInt(((Label) vBox.getChildren().get(4)).getText().substring(4));
            System.out.println(productId);
            //TODO go to product page NAZANIN
        });
        vBox.setPrefSize(265, 265);
        vBox.setAlignment(Pos.CENTER);
        ImageView imageView = product.getProductImage();
        imageView.setFitHeight(180);
        imageView.setFitWidth(180);
        Label sale = new Label();
        Label name = new Label(product.getName());
        Label price = new Label("lowest price: " + Double.toString(product.getLowestCurrentPrice()));
        Label id = new Label("id: " + product.getProductId());
        id.setOpacity(0.3);
        HBox score = createProductScore(product.getScore());
        setProductImageEffect(product, imageView, sale);
        vBox.getChildren().addAll(sale, imageView, name, price, id, score);
        return vBox;
    }

    private void setProductImageEffect(Product product, ImageView imageView, Label sale) {
        if (product.getTotalSupply() <= 0) {
            ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-0.7);
            imageView.setEffect(monochrome);
            return;
        }
        try {
            ProductField saleField = product.getBestSale();
            sale.setText(saleField.getSale().getSalePercent() * 100 + "%");
            sale.setStyle("-fx-text-fill: red;-fx-font-style: italic;-fx-font-weight: bold");
        } catch (Product.NoSaleForProduct noSaleForProduct) {
            return;
        }
    }

    private HBox createProductScore(double score) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        int completeStars = (int) score;
        double halfStar = score - completeStars;
        for (int i = 0; i < completeStars; i++) {
            ImageView star = new ImageView(new Image("images/star.png"));
            star.setFitWidth(20);
            star.setFitHeight(20);
            hBox.getChildren().add(star);
        }
        ImageView star = new ImageView();
        star.setFitHeight(20);
        star.setFitWidth(20);
        if (halfStar > 0.6) {
            star.setImage(new Image("images/starPrim.png"));
            hBox.getChildren().add(star);
        } else if (halfStar < 0.4 && halfStar > 0) {
            star.setImage(new Image("images/starPrimPrim.png"));
        } else if (halfStar != 0) {
            star.setImage(new Image("images/halfStar.png"));
        } else
            return hBox;
        hBox.getChildren().add(star);
        return hBox;
    }
}
