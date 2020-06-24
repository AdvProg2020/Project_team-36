package GUI;

import Controllers.CategoryController;
import Models.Category;
import Models.Discount;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.control.skin.ComboBoxBaseSkin;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageCategoriesController extends ManagerProfileController implements Initializable {
    public ImageView profilePicture;
    public TreeTableColumn nameColumn;
    public TreeTableColumn viewColumn;
    public TreeTableColumn removeColumn;
    public TreeTableColumn addSubcategoryColumn;
    public TreeTableView allCategoriesTable;

    @Override
    public void initialize(int id) throws IOException {

//        manager = Constants.loggedInUser.getLoggedInUser();
//        Image profile = new Image(getClass().getResource(manager.getProfilePictureUrl()).toExternalForm(),150,150,false,false);
//        profilePicture.setImage(profile);
//        usernameLabel.setText(manager.getUsername());
        Category.setManageCategoriesController(this);
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        viewColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("viewHyperlink"));
        removeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("removeHyperlink"));
        addSubcategoryColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("addSubcategory"));
        CategoryController categoryController = new CategoryController();
        Category mainCategory = categoryController.getMainCategory();
        TreeItem tableMainRoot = new TreeItem(mainCategory);
        ArrayList<Category> mainCategories = mainCategory.getSubCategories();
        for (Category category : mainCategories) {
            TreeItem categoryItem = new TreeItem(category);
            setTheSubcategories(category, categoryItem, 0);
            tableMainRoot.getChildren().add(categoryItem);
        }
        allCategoriesTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        allCategoriesTable.setRoot(tableMainRoot);
    }

    private void setTheSubcategories(Category mainCategory, TreeItem categoryItem, int indent){

        ArrayList<Category> subcategories = mainCategory.getSubCategories();
        if(subcategories.isEmpty() && indent!=0){
            TreeItem subcategory = new TreeItem(mainCategory);
            categoryItem.getChildren().add(subcategory);
        } else if (!subcategories.isEmpty() && indent!=0){

            TreeItem subItem = new TreeItem(mainCategory);
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, subItem, indent+1);
            }
            categoryItem.getChildren().add(subItem);
        } else if (!subcategories.isEmpty()){
            TreeItem subItem = new TreeItem(mainCategory);
            for (Category subcategory : subcategories) {
                setTheSubcategories(subcategory, categoryItem, indent+1);
            }
            categoryItem.getChildren().add(subItem);
        }
    }

    public void openAddNewCategory(ActionEvent actionEvent) {
    }

    public void removeAction(Category category){

    }

    public void viewAction(Category category){

    }

    public void addSubcategoryAction(Category category){

    }
}

