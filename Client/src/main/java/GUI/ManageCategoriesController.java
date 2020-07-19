package GUI;

import Controllers.CategoryController;
import Models.Category;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesController extends ManagerProfileController implements Initializable {
    @FXML
    private ImageView profilePicture;
    @FXML
    private TreeTableColumn<? extends Object, ? extends Object> nameColumn;
    @FXML
    private TreeTableView<Category> allCategoriesTable;
    @FXML
    private Label usernameLabel;
    private User user;
    private CategoryController categoryController = new CategoryController();

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

        Category.setManageCategoriesController(this);
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        Category mainCategoryRoot = categoryController.getMainCategory();
        TreeItem<Category> tableMainRoot = new TreeItem<>(mainCategoryRoot);
        allCategoriesTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        allCategoriesTable.setRoot(tableMainRoot);

        List<Category> mainCategories = mainCategoryRoot.getSubCategories();
        for (Category category : mainCategories) {
            TreeItem<Category> categoryItem = new TreeItem<>(category);
            setTheSubcategories(category, categoryItem, 0);
            tableMainRoot.getChildren().add(categoryItem);
        }
    }


    private void setTheSubcategories(Category mainCategory, TreeItem<Category> categoryItem, int indent) {

        List<Category> subcategories = mainCategory.getSubCategories();
        if (subcategories.isEmpty() && indent != 0) {
            TreeItem<Category> subcategory = new TreeItem<>(mainCategory);
            categoryItem.getChildren().add(subcategory);
        } else if (!subcategories.isEmpty() && indent != 0) {
            TreeItem<Category> subItem = new TreeItem<>(mainCategory);
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, subItem, indent + 1);
            }
            categoryItem.getChildren().add(subItem);
        } else if (!subcategories.isEmpty()) {
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, categoryItem, indent + 1);
            }
        }
    }

    public void openAddNewCategory(ActionEvent actionEvent) throws IOException {
        Constants.getGuiManager().open("AddCategory", user.getUserId());
    }

    public void removeAction() {
        TreeTableView.TreeTableViewSelectionModel<Category> selectedCategory = allCategoriesTable.getSelectionModel();

        if (selectedCategory.isEmpty()) {
            return;
        }

        int rowIndex = selectedCategory.getSelectedIndex();
        TreeItem<Category> selectedItem = selectedCategory.getModelItem(rowIndex);
        TreeItem<Category> parent = selectedItem.getParent();
        selectedItem.getValue().removeCategory();
        if (parent != null) {
            parent.getChildren().remove(selectedItem);
        } else {
            allCategoriesTable.setRoot(null);
        }
    }


    public void editAction() throws IOException {
        TreeTableView.TreeTableViewSelectionModel<Category> selectedCategory = allCategoriesTable.getSelectionModel();
        if (selectedCategory.isEmpty()) {
            return;
        }
        int rowIndex = selectedCategory.getSelectedIndex();
        TreeItem<Category> selectedItem = selectedCategory.getModelItem(rowIndex);
        Category categoryToEdit = selectedItem.getValue();

        categoryController.setCategoryToEdit(categoryToEdit.getId);
        Constants.getGuiManager().open("EditCategory", user.getUserId());
    }
}



