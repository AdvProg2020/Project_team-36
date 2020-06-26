package GUI;

import Controllers.ProductsController;
import Models.Field;
import Models.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class CompareBoxController implements Initializable {

    @FXML
    private TextField secondProductId;
    @FXML
    private Label alertLabel;
    @FXML
    private GridPane chart;
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
        secondProductName.setText(secondProduct.getName());

        addLabel(0,0,"product id");
        addLabel(1,0,Integer.toString(firstProduct.getProductId()));
        addLabel(2,0,Integer.toString(firstProduct.getProductId()));

        addLabel(0,1,"company");
        addLabel(1,1,firstProduct.getCompany());
        addLabel(2,1,secondProduct.getCompany());

        addLabel(0,2,"score");
        addLabel(1,2,Double.toString(firstProduct.getScore()));
        addLabel(2,2,Double.toString(secondProduct.getScore()));

        addLabel(0,3,"production date");
        addLabel(1,3,firstProduct.getProductionDate().toLocaleString());
        addLabel(2,3,secondProduct.getProductionDate().toLocaleString());

        addLabel(0,4,"seen number");
        addLabel(1,4,Integer.toString(firstProduct.getSeenNumber()));
        addLabel(2,4,Integer.toString(secondProduct.getSeenNumber()));

        addLabel(0,5,"best sale percent");
        try {
            addLabel(1,5,firstProduct.getBestSale().getSale().getSalePercent()*100+"%");
        } catch (Product.NoSaleForProduct noSaleForProduct) {
            addLabel(1,5,"-");
        }
        try {
            addLabel(2,5,firstProduct.getBestSale().getSale().getSalePercent()*100+"%");
        } catch (Product.NoSaleForProduct noSaleForProduct) {
            addLabel(2,5,"-");
        }

        addLabel(0,6,"lowest current price");
        addLabel(1,6,Long.toString(firstProduct.getLowestCurrentPrice()));
        addLabel(2,6,Long.toString(secondProduct.getLowestCurrentPrice()));

        addCategoryFields();


    }

    private void addCategoryFields(){
        int row = 7;
        for (Field field : firstProduct.getFieldsOfCategory()) {
            addLabel(0,row,field.getName());
            addLabel(1,row,field.getQuantityString());
            addLabel(2,row,secondProduct.getField(field.getName()).getQuantityString());
            row++;
        }
    }

    private void addLabel(int j, int i, String text){
        if(text ==null||text.isEmpty()||text.isBlank())
            text = "-";
        Label label = new Label(text);
        chart.add(label,j,i);
    }
}
