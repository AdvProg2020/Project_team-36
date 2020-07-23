package Client.GUI;

import Client.Controllers.CategoryController;
import Client.Models.User;
import Models.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewSellerProductController extends SellerProfileController implements Initializable {

    @FXML private Label usernameLabel;
    @FXML private ImageView profilePicture;
    @FXML private TextField productName;
    @FXML private TextField productCompany;
    @FXML private TextField productInfo;
    @FXML private TreeTableView<Client.Models.Category> categoryTable;
    @FXML private TreeTableColumn<Client.Models.Category, String> categoriesColumn;
    @FXML private ImageView image;
    @FXML private TextField price;
    @FXML private TextField supply;
    @FXML private VBox fieldsVBox;
    private User user;
    private ArrayList<Field> fields;
    private Client.Models.Category category;
    private final Client.Models.Product productToView = Constants.productsController.getProductToView();

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

        category = productToView.getCategory();

        categoriesColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));

        CategoryController categoryController = new CategoryController();
        Client.Models.Category mainCategoryRoot = categoryController.getMainCategory();
        TreeItem<Client.Models.Category> tableMainRoot = new TreeItem<>(mainCategoryRoot);
        expandAllParents(tableMainRoot);
        tableMainRoot.setExpanded(true);
        categoryTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        categoryTable.setRoot(tableMainRoot);

        List<Client.Models.Category> mainCategories = mainCategoryRoot.getSubCategories();
        for (Client.Models.Category category : mainCategories) {
            TreeItem<Client.Models.Category> categoryItem = new TreeItem<>(category);
            setTheSubcategories(category, categoryItem, 0);
            tableMainRoot.getChildren().add(categoryItem);
            expandAllParents(categoryItem);
        }
        fields = new ArrayList<>(productToView.getFieldsOfCategory());
        setFields();
        image.setImage(productToView.getProductImage(100,100).getImage());
        setTextFields();
    }

    private void setTheSubcategories(Client.Models.Category mainCategory, TreeItem<Client.Models.Category> categoryItem, int indent) throws MalformedURLException {
        List<Client.Models.Category> subcategories = mainCategory.getSubCategories();
        if (subcategories.isEmpty() && indent != 0) {
            TreeItem<Client.Models.Category> subcategory = new TreeItem<>(mainCategory);
            categoryItem.getChildren().add(subcategory);
            expandAllParents(subcategory);
        } else if (!subcategories.isEmpty() && indent != 0) {
            TreeItem<Client.Models.Category> subItem = new TreeItem<>(mainCategory);
            for (Client.Models.Category subcategory : subcategories) {
                setTheSubcategories(subcategory, subItem, indent + 1);
            }
            categoryItem.getChildren().add(subItem);
            expandAllParents(subItem);
        } else if (!subcategories.isEmpty()) {
            for (Client.Models.Category subcategory : subcategories) {
                setTheSubcategories(subcategory, categoryItem, indent + 1);
            }
        }
    }

    private void setFields() {
        fieldsVBox.setVisible(true);
        for (Field field : fields) {
            HBox hBox = new HBox();
            Label name = new Label(field.getName());
            name.setPrefHeight(25);
            name.setPrefWidth(100);
            name.setMinWidth(100);
            hBox.getChildren().add(name);
            TextField value = new TextField();
            value.setPrefHeight(25);
            value.setText(field.getQuantityString());
            value.setId(field.getName());
            value.setEditable(false);
            hBox.getChildren().add(value);
            fieldsVBox.getChildren().add(hBox);
        }
    }

    private void expandAllParents(TreeItem<Client.Models.Category> productCategory) throws MalformedURLException {
        if (productCategory.getValue().equals(category)) {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("/images/happy.png");
            if (resource == null) {
                throw new IllegalArgumentException("file is not found!");
            } else {
                File file = new File(resource.getFile());
                String path = file.toURI().toURL().toString();
                Node rootIcon = new ImageView(new Image(path, 10, 10, false, false));
                productCategory.setGraphic(rootIcon);
            }
            productCategory.setExpanded(false);
            productCategory = productCategory.getParent();
            while (productCategory != null) {
                productCategory.setExpanded(true);
                productCategory = productCategory.getParent();
            }
        }
    }

    private void setTextFields(){
        productName.setText(productToView.getName());
        productCompany.setText(productToView.getCompany());
        productInfo.setText(productToView.getInformation());
        price.setText(Long.toString(productToView.getProductFieldBySeller(user.getUserId()).getPrice()));
        supply.setText(Integer.toString(productToView.getProductFieldBySeller(user.getUserId()).getSupply()));
    }

}