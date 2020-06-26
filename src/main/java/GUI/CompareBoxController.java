package GUI;

import Controllers.ProductsController;
import Models.Field;
import Models.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

public class CompareBoxController implements Initializable {

    public GridPane title;
    @FXML
    private VBox mainVbox;
    @FXML
    private TextField secondProductId;
    @FXML
    private Label alertLabel;
    @FXML
    private VBox chart;
    @FXML
    private Label firstProductName;
    @FXML
    private Label secondProductName;

    private Product firstProduct;
    private Product secondProduct;

    @Override
    public void initialize(int id) throws IOException {
        firstProduct = Product.getProduct(id);
        addTextFieldListener();
        firstProductName.setText(firstProduct.getName());
    }

    public void compare(ActionEvent actionEvent) {
        chart.getChildren().clear();
        chart.getChildren().add(title);
        int id;
        try {
            id = Integer.parseInt(secondProductId.getText());
        } catch (NumberFormatException e) {
            alertLabel.setText("Invalid id!");
            return;
        }
        try {
            secondProduct = Constants.productsController.compare(id);
            fillChart();
            chart.setVisible(true);
        } catch (ProductsController.NoProductWithId noProductWithId) {
            alertLabel.setText("No product with this id!Try again");
        } catch (ProductsController.NotInTheSameCategory notInTheSameCategory) {
            alertLabel.setText("The products are not in same category!");
        }
    }

    private void addTextFieldListener() {
        secondProductId.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (secondProductId.getText().contains("[^\\d]"))
                secondProductId.setText(secondProductId.getText().replaceAll("[^\\d]", ""));
        });
    }

    private void fillChart() {
        alertLabel.setText("");
        secondProductName.setText(secondProduct.getName());
        chart.setPrefHeight((7+firstProduct.getFieldsOfCategory().size())*38+30);
       mainVbox.setPrefHeight(mainVbox.getPrefHeight()+(7+firstProduct.getFieldsOfCategory().size())*38+30);

        addLabel("product id",Integer.toString(firstProduct.getProductId()),Integer.toString(firstProduct.getProductId()));

        addLabel("company:",firstProduct.getCompany(),secondProduct.getCompany());

        addLabel("score",Double.toString(firstProduct.getScore()),Double.toString(secondProduct.getScore()));

        addLabel("production date",firstProduct.getProductionDate().toLocaleString(),secondProduct.getProductionDate().toLocaleString());

        addLabel("seen number",Integer.toString(firstProduct.getSeenNumber()),Integer.toString(secondProduct.getSeenNumber()));


       String first="";
       String second = "";
        try {
            first = firstProduct.getBestSale().getSale().getSalePercent()*100+"%";
        } catch (Product.NoSaleForProduct noSaleForProduct) {
           first="-";
        }
        try {
          second=firstProduct.getBestSale().getSale().getSalePercent()*100+"%";
        } catch (Product.NoSaleForProduct noSaleForProduct) {
            second = "-";
        }
        addLabel("best sale percent",first,second);

        addLabel("lowest current price",Long.toString(firstProduct.getLowestCurrentPrice()),Long.toString(secondProduct.getLowestCurrentPrice()));




        addCategoryFields();


    }

    private void addCategoryFields(){
        for (Field field : firstProduct.getFieldsOfCategory()) {
            addLabel(field.getName(),field.getQuantityString(),secondProduct.getField(field.getName()).getQuantityString());
        }
    }

    private void addLabel(String field,String first,String second){
        if(first ==null||first.isEmpty()||first.isBlank())
            first = "-";
        if(second ==null||second.isEmpty()||second.isBlank())
            second = "-";
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(484,38);

        StackPane pane =new StackPane();
        pane.setPrefSize(160,38);
        pane.setStyle("-fx-text-alignment: center;");
        Label label = new Label(field);
        label.setStyle("-fx-text-alignment: center;-fx-font-size: 13;-fx-content-display: center");
        pane.getChildren().add(label);
        gridPane.add(pane,0,0);

        pane=new StackPane();
        pane.setStyle("-fx-text-alignment: center");
        pane.setPrefSize(160,38);
        label = new Label(first);
        label.setStyle("-fx-text-alignment: center;-fx-font-size: 13;-fx-content-display: center");
        pane.getChildren().add(label);
        gridPane.add(pane,1,0);

        pane=new StackPane();
        pane.setStyle("-fx-text-alignment: center");
        pane.setPrefSize(160,38);
        label = new Label(second);
        label.setStyle("-fx-text-alignment: center;-fx-font-size: 13;-fx-content-display: center");
        pane.getChildren().add(label);
        gridPane.add(pane,2,0);
        gridPane.setGridLinesVisible(true);
        gridPane.setAlignment(Pos.CENTER);
        chart.getChildren().add(gridPane);
    }
}
