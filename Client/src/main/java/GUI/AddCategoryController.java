package GUI;

import Controllers.CategoryController;
import Models.Category;
import Models.User;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class AddCategoryController extends ManagerProfileController implements Initializable {

    public TextField name;
    public ComboBox parentCategory;
    public TextField integerFieldName;
    public TextField optionalField;
    public Label notifLabel;
    public VBox integerChosen;
    public VBox optionalChosen;
    public ImageView profilePicture;
    public Label usernameLabel;
    private User user;
    private CategoryController categoryController;

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

        this.categoryController = new CategoryController();
        setParentCategories();
    }

    private void setParentCategories() {
        parentCategory.getItems().clear();
        ArrayList<Category> allCategories = categoryController.getAllCategories();
        parentCategory.getItems().add("none");
        for (Category category : allCategories) {
            parentCategory.getItems().add(category.getName());
        }
    }

    public void addIntegerField(ActionEvent actionEvent) {
        if (integerFieldName.getText().isEmpty())
            return;
        for (Node child : integerChosen.getChildren()) {
            if (child instanceof Label) {
                if (integerFieldName.getText().equalsIgnoreCase(((Label) child).getText()))
                    return;
            }
        }
        for (Node child : optionalChosen.getChildren()) {
            if (child instanceof Label) {
                if (integerFieldName.getText().equalsIgnoreCase(((Label) child).getText()))
                    return;
            }
        }
        integerChosen.getChildren().add(new Label(integerFieldName.getText()));
    }

    public void addOptionalField(ActionEvent actionEvent) {
        if (optionalField.getText().isEmpty())
            return;
        for (Node child : optionalChosen.getChildren()) {
            if (child instanceof Label) {
                if (optionalField.getText().equalsIgnoreCase(((Label) child).getText()))
                    return;
            }
        }
        for (Node child : integerChosen.getChildren()) {
            if (child instanceof Label) {
                if (optionalField.getText().equalsIgnoreCase(((Label) child).getText()))
                    return;
            }
        }
        optionalChosen.getChildren().add(new Label(optionalField.getText()));
    }

    public void addCategory(ActionEvent actionEvent) {
        if (setCategoryName() && setParentCategory() && setFields()){
            categoryController.acceptCategory();
            notifLabel.setStyle("-fx-text-fill: darkgreen");
            notifLabel.setText("category successfully added!");
            optionalChosen.getChildren().clear();
            integerChosen.getChildren().clear();
            name.setText("");
            optionalField.setText("");
            integerFieldName.setText("");
            setParentCategories();
        }

    }

    private boolean setCategoryName() {
        if (name.getText().isEmpty()) {
            setLabelStyle();
            notifLabel.setText("First choose category name!");
            return false;
        }
        try {
            categoryController.setCategoriesName(name.getText());
            return true;
        } catch (CategoryController.InvalidCategoryName invalidCategoryName) {
            setLabelStyle();
            notifLabel.setText("there is a category with this name!");
            return false;
        }
    }

    private boolean setParentCategory() {
        if (parentCategory.getSelectionModel().isEmpty()) {
            setLabelStyle();
            notifLabel.setText("you have to choose a category or select none!");
            return false;
        }
        categoryController.setParentCategory(parentCategory.getValue().toString());
        return true;

    }

    private boolean setFields() {
        for (Node child : integerChosen.getChildren()) {
            if (child instanceof Label) {
                try {
                    categoryController.setIntegerField(((Label) child).getText());
                } catch (CategoryController.ThereIsFieldWithNameException e) {
                    setLabelStyle();
                    notifLabel.setText("Duplication in fields name!");
                    return false;
                }
            }
        }
        for (Node child : optionalChosen.getChildren()) {
            if (child instanceof Label) {
                try {
                    categoryController.setOptionalField(((Label) child).getText());
                } catch (CategoryController.ThereIsFieldWithNameException e) {
                    setLabelStyle();
                    notifLabel.setText("Duplication in fields name!");
                    return false;
                }
            }
        }
        return true;
    }

    private void setLabelStyle(){
        notifLabel.setStyle("-fx-text-fill: red");
    }

}
