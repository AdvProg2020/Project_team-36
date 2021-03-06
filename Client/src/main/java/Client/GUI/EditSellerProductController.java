package Client.GUI;

import Client.Controllers.CategoryController;
import Client.Controllers.EditProductController;
import Client.Models.Category;
import Client.Models.Product;
import Client.Models.User;
import Client.Network.Client;
import Models.Field;
import Models.IntegerField;
import Models.OptionalField;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class EditSellerProductController extends SellerProfileController implements Initializable {


    @FXML private ImageView profilePicture;
    @FXML private Label usernameLabel;
    @FXML private ImageView image;
    @FXML private TextField price;
    @FXML private TextField supply;
    @FXML private Label emptyFieldsError;
    @FXML private Label priceError;
    @FXML private Label supplyError;
    @FXML private TreeTableColumn<Category, String> categoriesColumn;
    @FXML private TreeTableView<Category> categoryTable;
    @FXML private TextArea productInfo;
    @FXML private TextField productCompany;
    @FXML private TextField productName;
    @FXML private VBox fieldsVBox;
    @FXML private Label fieldError;
    private User user;
    private Category category;
    private final Product productToEdit = Constants.productsController.getProductToEdit();
    private File imageFile =null;
    private ArrayList<Field> fields;
    private EditProductController editProductController;
    private Product editingProduct;
    private ArrayList<TextField> fieldsTextFields = new ArrayList<>();
    private boolean sameCategory;

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
        profilePicture.setImage(user.getProfilePicture(150, 150).getImage());

        category = productToEdit.getCategory();

        categoriesColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));

        CategoryController categoryController = new CategoryController();
        Category mainCategoryRoot = categoryController.getMainCategory();
        TreeItem<Category> tableMainRoot = new TreeItem<>(mainCategoryRoot);
        expandAllParents(tableMainRoot);
        tableMainRoot.setExpanded(true);
        categoryTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        categoryTable.setRoot(tableMainRoot);

        List<Category> mainCategories = mainCategoryRoot.getSubCategories();
        for (Category category : mainCategories) {
            TreeItem<Category> categoryItem = new TreeItem<>(category);
            setTheSubcategories(category, categoryItem, 0);
            tableMainRoot.getChildren().add(categoryItem);
            expandAllParents(categoryItem);
        }

        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            fields.clear();
            category = newValue.getValue();
            sameCategory = productToEdit.getCategory().equals(category);
            try {
                editProductController.editProductCategory(category.getName());
            } catch (EditProductController.InvalidCategoryException e) {
                //
            }
            for (Field field : category.getAllFields()) {
                if (field instanceof IntegerField)
                    fields.add(new IntegerField(field.getName()));
                else
                    fields.add(new OptionalField(field.getName()));
            }
            setNewFields();
        });

        price.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue.matches(".*[^\\d].*"))
                        price.setText(price.getText().replaceAll("[^\\d]", ""));
        });

        supply.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue.matches(".*[^\\d].*"))
                        supply.setText(supply.getText().replaceAll("[^\\d]", ""));
        });

        editProductController = new EditProductController();
        editingProduct = editProductController.getProductCopy(productToEdit);
        fields = new ArrayList<>(productToEdit.getFieldsOfCategory());
        setNewFields();
        image.setImage(productToEdit.getProductImage(100, 100).getImage());
        setTextFields();
    }

    private void setNewFields() {
        fieldsTextFields.clear();
        fieldsVBox.getChildren().clear();
        ArrayList<Field> oldFields = new ArrayList<>(productToEdit.getFieldsOfCategory());
        ArrayList<String> oldFieldsNames = new ArrayList<>();
        if (sameCategory) {
            oldFields.forEach(field -> oldFieldsNames.add(field.getName()));
        }
        for (Field field : fields) {
            HBox hBox = new HBox(5);
            Label name = new Label(field.getName());
            name.setPrefWidth(100);
            name.setMinWidth(100);
            name.setPrefHeight(25);
            hBox.getChildren().add(name);
            TextField value = new TextField();
            value.setPrefHeight(25);
            fieldsTextFields.add(value);
            if (sameCategory) {
                if (oldFieldsNames.contains(field.getName())) {
                    for (Field oldField : oldFields) {
                        if (oldField.getName().equals(field.getName())) {
                            value.setText(oldField.getName());
                            try {
                                editProductController.setEachCategoryField(value.getText(), field);
                            } catch (EditProductController.InvalidFieldValue invalidFieldValue) {
                                //
                            }
                        }
                    }
                }
            }
            if (field instanceof IntegerField) {
                value.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue.matches(".*[^(\\d|\\.)].*"))
                        value.setText(value.getText().replaceAll("[^\\d]", ""));
                    try {
                        editProductController.setEachCategoryField(value.getText(), field);
                    } catch (EditProductController.InvalidFieldValue invalidFieldValue) {
                        value.setStyle("-fx-border-color: red");
                        fieldError.setVisible(true);
                    }
                });
            } else {
                value.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    try {
                        editProductController.setEachCategoryField(value.getText(), field);
                    } catch (EditProductController.InvalidFieldValue invalidFieldValue) {
                        //
                    }
                });
            }
            value.setId(field.getName());
            hBox.getChildren().add(value);
            fieldsVBox.getChildren().add(hBox);
        }

    }

    private void setTheSubcategories(Category mainCategory, TreeItem<Category> categoryItem, int indent) throws MalformedURLException {
        List<Category> subcategories = mainCategory.getSubCategories();
        if (subcategories.isEmpty() && indent != 0) {
            TreeItem<Category> subcategory = new TreeItem<>(mainCategory);
            categoryItem.getChildren().add(subcategory);
            expandAllParents(subcategory);
        } else if (!subcategories.isEmpty() && indent != 0) {
            TreeItem<Category> subItem = new TreeItem<>(mainCategory);
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, subItem, indent + 1);
            }
            categoryItem.getChildren().add(subItem);
            expandAllParents(subItem);
        } else if (!subcategories.isEmpty()) {
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, categoryItem, indent + 1);
            }
        }
    }

    private void expandAllParents(TreeItem<Category> productCategory) throws MalformedURLException {
        if (productCategory.getValue().equals(category)) {
            File file = new File("D:\\myprj\\project\\AP_Project\\src\\main\\resources\\images\\happy.png");
            String path = file.toURI().toURL().toString();
            Node rootIcon = new ImageView(new Image(path, 10, 10, false, false));
            productCategory.setGraphic(rootIcon);
            productCategory.setExpanded(false);
            productCategory = productCategory.getParent();
            while (productCategory != null) {
                productCategory.setExpanded(true);
                productCategory = productCategory.getParent();
            }
        }
    }

    private void setTextFields() {
        productName.setText(productToEdit.getName());
        productCompany.setText(productToEdit.getCompany());
        productInfo.setText(productToEdit.getInformation());
        price.setText(Long.toString(productToEdit.getProductFieldBySeller(user.getUserId()).getPrice()));
        supply.setText(Integer.toString(productToEdit.getProductFieldBySeller(user.getUserId()).getSupply()));
    }

    public void editImage() {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Profile");
        File file = fileChooser.showOpenDialog(Constants.getGuiManager().getLoginStage());
        if (file != null) {
            try {
                imageFile = file;
                image.setImage(new Image(new FileInputStream(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean setImage() {
        if(imageFile!= null){
            String path = "";
            try {
                path = Client.writeFile(Files.readAllBytes(imageFile.toPath()));
                editProductController.editImage(path);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            return true;
        }
        return false;
    }

    private boolean getStringInput(TextField textField) {
        if (textField.getText().isEmpty()) {
            emptyFieldsError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean getStringInput(TextArea textArea) {
        if (textArea.getText().isEmpty()) {
            emptyFieldsError.setVisible(true);
            return false;
        }

        return true;
    }

    public void editProduct() throws IOException {
        setImage();
        editProductController.setFieldsOfCategory();
        if (getStringInput(productName)) {
            editProductController.editProductName(productName.getText());
        } else {
            return;
        }
        if (getStringInput(productCompany)) {
            editProductController.editProductCompany(productCompany.getText());
        } else {
            return;
        }
        if (getStringInput(productInfo)) {
            editProductController.editProductInformation(productInfo.getText());
        } else {
            return;
        }
        if (getStringInput(price)) {
            try {
                editProductController.editProductPrice(price.getText());
            } catch (Exception e) {
                price.clear();
                priceError.setVisible(true);
                return;
            }
        }
        if (getStringInput(supply)) {
            try {
                editProductController.editProductSupply(supply.getText());
            } catch (Exception e) {
                supply.clear();
                supplyError.setVisible(true);
                return;
            }
        }
        boolean isEmpty = false;
        for (TextField textField : fieldsTextFields) {
            isEmpty = textField.getText().isEmpty();
        }
        if (isEmpty) {
            emptyFieldsError.setVisible(true);
            return;
        }
        if (editingProduct.getProductFieldBySeller(user.getUserId()).getSupply() !=
                productToEdit.getProductFieldBySeller(user.getUserId()).getSupply() ||
                editingProduct.getProductFieldBySeller(user.getUserId()).getPrice() !=
                        productToEdit.getProductFieldBySeller(user.getUserId()).getPrice())
            editProductController.sendEditProductFieldRequest();

        editProductController.sendEditProductRequest();
        AlertBox.display("Done!", "Request to edit the product was sent.");
        Constants.getGuiManager().reopen();
    }
}