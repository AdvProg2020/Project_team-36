package GUI;

import Controllers.CategoryController;
import Controllers.NewProductController;
import Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AddNewProductController extends SellerProductsController implements Initializable {


    @FXML
    private TreeTableColumn<Category,String> categoriesColumn;
    @FXML
    private ImageView image;
    @FXML
    private TextField productInfo;
    @FXML
    private TextField productCompany;
    @FXML
    private TextField productName;
    @FXML
    private TreeTableView<Category> categoryTable;
    @FXML
    private TextField price;
    @FXML
    private TextField supply;
    @FXML
    private Label emptyFieldsError;
    @FXML
    private Label profilePicError;
    @FXML
    private Label supplyError;
    @FXML
    private Label priceError;
    @FXML
    private VBox fieldsVBox;
    @FXML
    private ScrollPane fieldsScrollPane;
    @FXML
    private Label fieldError;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView profilePicture;
    private User user;
    private String imagePath = "";
    private ArrayList<Field> fields = new ArrayList<>();
    private Category category;
    private ArrayList<String> fieldsValue = new ArrayList<>();
    private NewProductController newProduct = new NewProductController();

    @Override
    public void initialize(int id) throws IOException {

        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        } else {
            this.user = Constants.globalVariables.getLoggedInUser();
        }
        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());
        newProduct.setSeller((Seller)user);

        categoriesColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));

        CategoryController categoryController = new CategoryController();
        Category mainCategoryRoot = categoryController.getMainCategory();
        TreeItem<Category> tableMainRoot = new TreeItem<>(mainCategoryRoot);
        categoryTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        categoryTable.setRoot(tableMainRoot);

        List<Category> mainCategories = mainCategoryRoot.getSubCategories();
        for (Category category : mainCategories) {
            TreeItem<Category> categoryItem = new TreeItem<>(category);
            setTheSubcategories(category, categoryItem, 0);
            tableMainRoot.getChildren().add(categoryItem);
        }

        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
            fields.clear();
            category = newValue.getValue();
            try {
                newProduct.setCategory(category.getName());
            } catch (NewProductController.InvalidCategoryName invalidCategoryName) {
                invalidCategoryName.printStackTrace();
            }
            for (Field field : category.getAllFields()) {
                if(field instanceof IntegerField)
                    fields.add(new IntegerField(field.getName()));
                else
                    fields.add(new OptionalField(field.getName()));
            }
            setFields();
        });

        price.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches(".*[^\\d].*"))
                price.setText(price.getText().replaceAll("[^\\d]", ""));
        });

        supply.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches(".*[^\\d].*"))
                supply.setText(supply.getText().replaceAll("[^\\d]", ""));
        });

    }

    private void setTheSubcategories(Category mainCategory, TreeItem<Category> categoryItem, int indent){
        List<Category> subcategories = mainCategory.getSubCategories();
        if(subcategories.isEmpty() && indent!=0){
            TreeItem<Category> subcategory = new TreeItem<>(mainCategory);
            categoryItem.getChildren().add(subcategory);
        } else if (!subcategories.isEmpty() && indent!=0){
            TreeItem<Category> subItem = new TreeItem<>(mainCategory);
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, subItem, indent+1);
            }
            categoryItem.getChildren().add(subItem);
        } else if (!subcategories.isEmpty()){
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, categoryItem, indent+1);
            }
        }
    }

    private void setFields(){
        fieldsValue.clear();
        fieldsVBox.getChildren().clear();
        fieldsScrollPane.setVisible(true);
        for (Field field : fields) {
            HBox hBox = new HBox(5);
            HBox.setMargin(hBox,new Insets(0,0,0,8));
            Label name = new Label(field.getName());
            name.setPrefWidth(100);
            name.setMinWidth(100);
            name.setPrefHeight(25);
            hBox.getChildren().add(name);
            TextField value = new TextField();
            value.setPrefHeight(25);
            if (field instanceof IntegerField){
                value.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue.matches(".*[^(\\d|\\.)].*"))
                        value.setText(value.getText().replaceAll("[^\\d]", ""));
                    try {
                        newProduct.setEachCategoryField(value.getText(),field);
                    } catch (NewProductController.InvalidFieldValue invalidFieldValue) {
                        value.setStyle("-fx-border-color: red");
                        fieldError.setVisible(true);
                    }
                    fieldsValue.remove(oldValue);
                    fieldsValue.add(value.getText());
                });
            } else {
                value.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    try {
                        newProduct.setEachCategoryField(value.getText(),field);
                    } catch (NewProductController.InvalidFieldValue invalidFieldValue) {
                        //
                    }
                    fieldsValue.remove(oldValue);
                    fieldsValue.add(value.getText());
                });
            }
            value.setId(field.getName());
            hBox.getChildren().add(value);
            fieldsVBox.getChildren().add(hBox);
        }

    }


    public void AddProduct(ActionEvent actionEvent) throws ParseException, IOException {

        if(getStringInput(productName)){
            newProduct.setName(productName.getText());
        } else {
            return;
        }
        if(getStringInput(productCompany)){
            newProduct.setCompany(productCompany.getText());
        } else {
            return;
        }
        if(getStringInput(productInfo)){
            newProduct.setInformation(productInfo.getText());
        } else {
            return;
        }
        if(getStringInput(price) && getStringInput(supply)){
            int intSupply;
            long longPrice;

            try {
                intSupply = Integer.parseInt(supply.getText());
            } catch (Exception e){
                supply.clear();
                supplyError.setVisible(true);
                return;
            }
            try {
                longPrice = Long.parseLong(price.getText());
            } catch (Exception e){
                price.clear();
                priceError.setVisible(true);
                return;
            }
            newProduct.setProductField(longPrice, intSupply);
        }
        if(category == null){
            System.out.println("cat");
            emptyFieldsError.setVisible(true);
            return;
        }

        if(fieldsValue.size() != fields.size()){
            System.out.println("field");
            System.out.println(fieldsValue.size()+"  "+fields.size());
            emptyFieldsError.setVisible(true);
            return;
        }

        if(!setImage()){
            return;
        }

        newProduct.sendNewProductRequest();
        AlertBox.display("Done!","Request for a new product was sent.");
        Constants.getGuiManager().reopen();
    }

    private boolean getStringInput(TextField textField){
        if (textField.getText().isEmpty()){
            emptyFieldsError.setVisible(true);
            return false;
        }

        return true;
    }

    public void addImage(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Profile");
        File file = fileChooser.showOpenDialog(Constants.getGuiManager().getLoginStage());
        if (file != null) {
            try {
                imagePath = file.getPath();
                image.setImage(new Image(new FileInputStream(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean setImage() {
        if (imagePath.isEmpty()) {
            profilePicError.setVisible(true);
            return false;
        }
        newProduct.setImage(imagePath);
        return true;
    }
}
