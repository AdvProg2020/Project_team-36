package Client.GUI;

import Client.Controllers.CategoryController;
import Client.Models.Category;
import Client.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.List;

public class ShowCategoriesController extends SellerProfileController implements Initializable {

    @FXML
    private ImageView profilePicture;
    @FXML
    private TreeTableColumn<?, ?> nameColumn;
    @FXML
    private TreeTableView<Category> allCategoriesTable;
    @FXML
    private Label usernameLabel;

    @Override
    public void initialize(int id) throws IOException {
        User user;
        if (Constants.globalVariables.getLoggedInUser() == null) {
            Constants.getGuiManager().back();
            return;
        } else if (Constants.globalVariables.getLoggedInUser().getUserId() != id) {
            Constants.getGuiManager().back();
            return;
        } else {
            user = Constants.globalVariables.getLoggedInUser();
        }

        usernameLabel.setText(user.getUsername());
        profilePicture.setImage(user.getProfilePicture(150,150).getImage());

        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));

        CategoryController categoryController = new CategoryController();
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

}
