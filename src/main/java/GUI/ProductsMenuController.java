package GUI;

import Controllers.ProductsController;
import Models.Product;
import Models.ProductField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashSet;

public class ProductsMenuController implements Initializable {

    @FXML
    public CheckBox ascendingSort;
    public MenuButton companyFilterMenu;
    public ScrollPane companyFilterScroll;
    public VBox filterNamesBox;
    public TextField filterName;
    private final ProductsController productsController = Constants.productsController;
    public CheckBox onlyAvailables;
    public TextField filterSeller;
    public VBox filterSellersBox;
    public ComboBox categoryFilter;
    public TextField minimumPrice;
    public TextField maximumPrice;
    public VBox integerFields;
    public VBox optionalFields;
    private int page = 1;
    public ComboBox sortBox;
    public HBox bottomPane;
    public ScrollPane productsScrollPane;
    private final ArrayList<Button> pageButtons = new ArrayList<>();
    private final ArrayList<CheckBox> companyFilterCheckBox = new ArrayList<>();

    @Override
    public void initialize(int id) {
        showAllProducts(productsController.getFinalProductsList());
        setCompanyFilters();
        setCategoryFilters();
        setMinimumPriceFilter();
        setMaximumPriceFilter();
    }

    private void setCompanyFilters() {
        VBox vBox = new VBox();
        companyFilterScroll.setContent(vBox);
        HashSet<String> companyNames = productsController.getCompanyNamesForFilter();
        for (String name : companyNames) {
            CheckBox checkBox = new CheckBox(name);
            checkBox.setPrefSize(120, 25);
            checkBox.setDisable(false);
            vBox.getChildren().add(checkBox);
            companyFilterCheckBox.add(checkBox);
            checkBox.setOnAction(actionEvent -> {
                ArrayList<String> options = new ArrayList<>();
                for (CheckBox filterCheckBox : companyFilterCheckBox) {
                    if (filterCheckBox.isSelected()) {
                        options.add(filterCheckBox.getText());
                    }
                }
                productsController.setCompanyFilter(options);
                showAllProducts(productsController.getFinalProductsList());
            });
        }
    }

    private void setCategoryFilters() {
        ArrayList<String> names = productsController.getCategories();
        categoryFilter.getItems().add("none");
        for (String name : names) {
            categoryFilter.getItems().add(name);
        }


    }

    private void setMinimumPriceFilter() {
        minimumPrice.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d+")) {
                minimumPrice.setText(newValue.replaceAll("[^\\d]", ""));
            }
            try {
                productsController.setNewFilter("price");
            } catch (ProductsController.IntegerFieldException e) {
                String min = minimumPrice.getText().isEmpty() ? "0" : minimumPrice.getText().replaceAll("[^\\d]", "");
                String max = maximumPrice.getText().isEmpty() ? "99999999999999" : maximumPrice.getText().replaceAll("[^\\d]", "");
                productsController.setFilterRange(min, max);
                showAllProducts(productsController.getFinalProductsList());
            } catch (ProductsController.OptionalFieldException | ProductsController.NoFilterWithNameException e) {
                e.printStackTrace();
            }
        });

    }

    private void setMaximumPriceFilter() {
        maximumPrice.textProperty().addListener((observableValue, s, newValue) -> {
            if (!newValue.matches("\\d+")) {
                maximumPrice.setText(newValue.replaceAll("[^\\d]", ""));
            }
            try {
                productsController.setNewFilter("price");
            } catch (ProductsController.IntegerFieldException e) {
                String minimum = minimumPrice.getText().isEmpty() ? "0" : minimumPrice.getText().replaceAll("[^\\d]", "");
                String maximum = maximumPrice.getText().isEmpty() ? "99999999999999" : maximumPrice.getText().replaceAll("[^\\d]", "");
                System.out.println(minimum + " " + maximum);
                productsController.setFilterRange(minimum, maximum);
                showAllProducts(productsController.getFinalProductsList());
            } catch (ProductsController.OptionalFieldException | ProductsController.NoFilterWithNameException e) {
                e.printStackTrace();
            }
        });
    }

    private void showAllProducts(ArrayList<Product> allProducts) {
        if (allProducts.isEmpty()) {
            ImageView imageView = new ImageView(new Image("images/noProduct.png"));
            productsScrollPane.setCenterShape(true);
            productsScrollPane.setContent(imageView);
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
            for (int j = 0; j < 4 && 4 * i + j < size; j++) {
                gridPane.add(productView(allProducts.get(i * 4 + j)), j, i);//third parameter: column,forth parameter: row
            }
        }
        productsScrollPane.setContent(gridPane);
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
        Label price = new Label("lowest price: " + Double.toString(product.getLowestPrice()));
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

    public void addNameFilter(MouseEvent mouseEvent) {
        if (!filterName.getText().isEmpty()) {
            for (Node node : filterNamesBox.getChildren()) {
                if (((CheckBox) node).getText().equalsIgnoreCase(filterName.getText()))
                    return;
            }
            CheckBox names = new CheckBox(filterName.getText());
            filterNamesBox.getChildren().add(names);
            productsController.addNameFilter(filterName.getText());
            names.setOnAction(actionEvent -> removeNameFilter(names));
            filterName.setText("");
            showAllProducts(productsController.getFinalProductsList());
        }
    }

    private void removeNameFilter(CheckBox name) {
        productsController.removeNameFilter(name.getText());
        filterNamesBox.getChildren().remove(name);
        showAllProducts(productsController.getFinalProductsList());

    }

    public void changeAvailability(ActionEvent actionEvent) {
        if (onlyAvailables.isSelected()) {
            productsController.availabilityFilter();
        } else
            productsController.removeAvailabilityFilter();

        showAllProducts(productsController.getFinalProductsList());
    }

    public void addSellerFilter(MouseEvent mouseEvent) {
        if (!filterSeller.getText().isEmpty()) {
            for (Node node : filterSellersBox.getChildren()) {
                if (((CheckBox) node).getText().equalsIgnoreCase(filterName.getText()))
                    return;
            }
            CheckBox names = new CheckBox(filterSeller.getText());
            filterSellersBox.getChildren().add(names);
            productsController.addSellerFilter(filterSeller.getText());
            names.setOnAction(actionEvent -> removeSellerFilter(names));
            filterSeller.setText("");
            showAllProducts(productsController.getFinalProductsList());
        }

    }

    private void removeSellerFilter(CheckBox checkBox) {
        productsController.removeSellerFilter(checkBox.getText());
        filterSellersBox.getChildren().remove(checkBox);
        showAllProducts(productsController.getFinalProductsList());
    }

    public void filterCategory(ActionEvent actionEvent) {
        if (categoryFilter.getValue().toString().equals("none")) {
            try {
                productsController.removeFilter("category");
                removeSpecialIntegerFilters();
            } catch (ProductsController.NoFilterWithNameException e) {
                return;
            }
        } else {
            try {
                removeSpecialIntegerFilters();
                productsController.setCategoryFilter(categoryFilter.getValue().toString());
                addSpecialIntegerFilters();
                addSpecialOptionalFilter();
            } catch (ProductsController.NoCategoryWithName noCategoryWithName) {
                noCategoryWithName.printStackTrace();
            }
        }
        showAllProducts(productsController.getFinalProductsList());
    }

    private void addSpecialIntegerFilters() {
        for (String filterName : productsController.getSpecialIntegerFilter()) {
            integerFields.getChildren().add(getEachIntegerFilter(filterName));
        }
    }

    private MenuButton getEachIntegerFilter(String name) {
        MenuButton menuButton = new MenuButton();
        CheckBox checkBox = new CheckBox(name);
        menuButton.setGraphic(checkBox);
        CustomMenuItem customMenuItem = new CustomMenuItem();
        customMenuItem.setHideOnClick(false);
        menuButton.getItems().add(customMenuItem);
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPrefSize(integerFields.getPrefWidth()-10,integerFields.getPrefHeight());
        TextField min = new TextField();
        min.setPromptText("Minimum");
        TextField max = new TextField();
        addListenerIntegerFields(name, min, max, true);
        addListenerIntegerFields(name, max, min, false);
        vbox.getChildren().addAll(min,max);
        max.setPromptText("Maximum");
        customMenuItem.setContent(vbox);
        checkBox.setOnAction(actionEvent -> {
            if (!checkBox.isSelected()) {
                disableIntegerFilter(checkBox);
                min.setText("");
                max.setText("");
            } else {
                menuButton.setDisable(false);
            }
        });
        return menuButton;
    }

    private void addListenerIntegerFields(String field, TextField listenerAdder, TextField textField, boolean isMin) {
        listenerAdder.textProperty().addListener((observableValue, s, newValue) -> {
            if (!newValue.matches("\\d*\\.\\d*")) {
                listenerAdder.setText(newValue.replaceAll("[^\\d|^.]", ""));
            }
            try {
                productsController.setNewFilter(field);
            } catch (ProductsController.IntegerFieldException e) {
                if (isMin) {
                    String min = listenerAdder.getText().isEmpty() ? "0" : listenerAdder.getText().replaceAll("[^\\d|^.]", "");
                    String max = textField.getText().isEmpty() ? "9999999999999999" : textField.getText().replaceAll("[^\\d|.]", "");
                    productsController.setFilterRange(min, max);
                } else {
                    String max = listenerAdder.getText().isEmpty() ? "9999999999999" : listenerAdder.getText().replaceAll("[^\\d|^.]", "");
                    String min = textField.getText().isEmpty() ? "0" : textField.getText().replaceAll("[^\\d|.]", "");
                    productsController.setFilterRange(min, max);
                }
                showAllProducts(productsController.getFinalProductsList());
            } catch (ProductsController.OptionalFieldException | ProductsController.NoFilterWithNameException e) {
                e.printStackTrace();
            }

        });

    }

    private void removeSpecialIntegerFilters() {
        integerFields.getChildren().clear();
        optionalFields.getChildren().clear();
    }

    private void disableIntegerFilter(CheckBox checkBox) {
        try {
            productsController.removeFilter(checkBox.getText());
        } catch (ProductsController.NoFilterWithNameException e) {
            return;
        }
    }

    private void addSpecialOptionalFilter(){
        for (String option : productsController.getAllOptionalChoices().keySet()) {
            getEachOptionalFilter(option,productsController.getAllOptionalChoices().get(option));
        }
    }

    private MenuButton getEachOptionalFilter(String name,HashSet<String> options){
        MenuButton menuButton = new MenuButton();
        CheckBox checkBox = new CheckBox(name);
        menuButton.setGraphic(checkBox);
        CustomMenuItem customMenuItem = new CustomMenuItem();
        customMenuItem.setHideOnClick(false);
        menuButton.getItems().add(customMenuItem);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(menuButton.getPrefWidth(),menuButton.getPrefHeight());
        customMenuItem.setContent(scrollPane);
        VBox innerVBox = new VBox();
        scrollPane.setContent(innerVBox);
        innerVBox.setPrefSize(menuButton.getPrefWidth(),menuButton.getPrefHeight());
        checkBox.setOnAction(actionEvent -> {
            if (!checkBox.isSelected()) {
                try {
                    productsController.removeFilter(checkBox.getText());
                    for (Node child : innerVBox.getChildren()) {
                        if(child instanceof CheckBox){
                            child.setDisable(true);
                            ((CheckBox) child).setSelected(false);
                        }
                    }
                } catch (ProductsController.NoFilterWithNameException e) {
                    e.printStackTrace();
                }
            }
            else{
                for (Node child : innerVBox.getChildren()) {
                    if(child instanceof CheckBox){
                        child.setDisable(false);
                        ((CheckBox) child).setSelected(false);
                    }
                }
            }
        });
        for (String option : options) {
           CheckBox optionCheckBox = getOptionCheckBox(option,checkBox);
            innerVBox.getChildren().add(optionCheckBox);
        }
        return menuButton;
    }

    private CheckBox getOptionCheckBox(String option,CheckBox fieldCheckBox){
        CheckBox optionCheckBox = new CheckBox(option);
        optionCheckBox.setOnAction(actionEvent -> {
            if(optionCheckBox.isSelected()){
                productsController.addOptionalFilter(fieldCheckBox.getText(),optionCheckBox.getText());
            }else{
                productsController.removeOptionalFilter(fieldCheckBox.getText(),optionCheckBox.getText());
            }
            showAllProducts(productsController.getFinalProductsList());
        });
        return optionCheckBox;
    }

}
