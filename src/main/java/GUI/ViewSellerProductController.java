package GUI;

import Controllers.CategoryController;
import Models.*;
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
import java.util.ArrayList;

public class ViewSellerProductController extends SellerProfileController implements Initializable {

    public Label usernameLabel;
    public ImageView profilePicture;
    public TextField productName;
    public TextField productCompany;
    public TextField productInfo;
    public TreeTableView<Category> categoryTable;
    public TreeTableColumn<Category, String> categoriesColumn;
    public ImageView image;
    public TextField price;
    public TextField supply;
    public VBox fieldsVBox;
    private User user;
    private ArrayList<Field> fields;
    private Category category;
    private final Product productToView = Product.getProductToView();

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
        Category mainCategoryRoot = categoryController.getMainCategory();
        TreeItem<Category> tableMainRoot = new TreeItem<>(mainCategoryRoot);
        expandAllParents(tableMainRoot);
        categoryTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        categoryTable.setRoot(tableMainRoot);

        ArrayList<Category> mainCategories = mainCategoryRoot.getSubCategories();
        for (Category category : mainCategories) {
            TreeItem<Category> categoryItem = new TreeItem<>(category);
            setTheSubcategories(category, categoryItem, 0);
            tableMainRoot.getChildren().add(categoryItem);
            expandAllParents(categoryItem);
        }
        fields = new ArrayList<>(productToView.getFieldsOfCategory());
        setFields();
        image.setImage(productToView.getProductImage(100,100).getImage());
        setTextFields();
    }

    private void setTheSubcategories(Category mainCategory, TreeItem<Category> categoryItem, int indent) throws MalformedURLException {
        ArrayList<Category> subcategories = mainCategory.getSubCategories();
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

    private void expandAllParents(TreeItem<Category> productCategory) throws MalformedURLException {
        if (productCategory.getValue().equals(category)) {
            File file = new File ("D:\\myprj\\project\\AP_Project\\src\\main\\resources\\images\\happy.png");
            String path = file.toURI().toURL().toString();
            Node rootIcon = new ImageView(new Image(path,10,10,false,false));
            productCategory.setGraphic(rootIcon);
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
        price.setText(Long.toString(productToView.getProductFieldBySeller((Seller)user).getPrice()));
        supply.setText(Integer.toString(productToView.getProductFieldBySeller((Seller)user).getSupply()));

    }

}