package GUI;

import Controllers.CategoryController;
import Models.Category;
import Models.Field;
import Models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class EditCategoryController extends ManagerProfileController implements Initializable {
    public Label welcomeLabel;
    public TextField newName;
    public Label nameLabel;
    public VBox fields;
    public Label editNotif;
    public TextField newOptionalField;
    public TextField newIntegerField;
    public Label integerAlertLabel;
    public Label optionalAlertLabel;
    public Label usernameLabel;
    public ImageView profilePicture;
    private User user;
    Category category ;
    CategoryController categoryController;
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
        this.category = Category.getCategoryToEdit();
        welcomeLabel.setText("category "+category.getName()+" editing!");
        try {
            categoryController.editCategory(category.getName());
        } catch (CategoryController.InvalidCategoryName invalidCategoryName) {
            invalidCategoryName.printStackTrace();
        }
        setFields();

    }

    private void setFields(){
        HashSet<Field> allFields = category.getCategoryOwnFields();
        for (Field field : allFields) {
           fields.getChildren().add(createHbox(field.getName()));
        }
    }

    private void addNewField(String name){
        fields.getChildren().add(createHbox(name));
    }

    private HBox createHbox(String field){
        HBox hbox = new HBox();
        Label label = new Label(field);
        label.setStyle("-fx-font-size: 14");
        label.setPrefSize(92,30);

        TextField textField = new TextField();
        textField.setPromptText("new name");
        textField.setPrefSize(92,30);

        Button remove = new Button("remove");
        remove.setPrefSize(92,30);
        remove.setOnMouseClicked(mouseEvent -> {
            categoryController.removeField(label.getText());
            editNotif.setStyle("-fx-text-fill: green");
            editNotif.setText("removed successfully");
            fields.getChildren().remove(hbox);
        });

        Button edit = new Button("rename");
        edit.setPrefSize(92,30);
        edit.setOnMouseClicked(mouseEvent -> {
            if(!textField.getText().isEmpty()) {
                try {
                    categoryController.editField(label.getText());
                    categoryController.renameField(textField.getText());
                    editNotif.setStyle("-fx-text-fill: green");
                    editNotif.setText("renamed successfully");
                    label.setText(textField.getText());
                } catch (CategoryController.ThereIsFieldWithNameException | CategoryController.ThereIsFieldWithNameInSubCategory | CategoryController.NoFieldWithNameException e) {
                    editNotif.setStyle("-fx-text-fill: red");
                    editNotif.setText("couldn't rename due to duplication in fields of (sub)category");
                }
            }
        });
        hbox.getChildren().addAll(label,textField,edit,remove);
        return hbox;
    }

    public void rename(ActionEvent actionEvent) {
        if(newName.getText().isEmpty()){
            nameLabel.setStyle("-fx-text-fill: red");
            nameLabel.setText("First choose a name!:)");
        }else if(newName.getText().equalsIgnoreCase(category.getName())){
            nameLabel.setStyle("-fx-text-fill: red");
            nameLabel.setText("Renaming to old name?:)");
        }else {
            try {
                categoryController.editName(newName.getText());
                nameLabel.setStyle("-fx-text-fill: green");
                nameLabel.setText("successfully renamed!");

            } catch (CategoryController.InvalidCategoryName invalidCategoryName) {
                nameLabel.setStyle("-fx-text-fill: red");
                nameLabel.setText("There is a category with this name!Try again");
            }
        }
    }

    public void addNewNumericalField(ActionEvent actionEvent) {
        if(newIntegerField.getText().isEmpty()){
            integerAlertLabel.setStyle("-fx-text-fill: red");
            integerAlertLabel.setText("You have to fill the box!");
            return;
        }
        try {
            categoryController.addField(newIntegerField.getText(), "IntegerField");
            integerAlertLabel.setStyle("-fx-text-fill: green");
            integerAlertLabel.setText("field added successfully");
            addNewField(newIntegerField.getText());
        } catch (CategoryController.ThereIsFieldWithNameException e) {
            integerAlertLabel.setStyle("-fx-text-fill: red");
            integerAlertLabel.setText("Failed! Due to duplicated name");
        } catch (CategoryController.ThereIsFieldWithNameInSubCategory thereIsFieldWithNameInSubCategory) {
            integerAlertLabel.setStyle("-fx-text-fill: red");
            integerAlertLabel.setText("Failed!There is field with other type in subCategory");
        }
    }

    public void addNewOptionalField(ActionEvent actionEvent) {
        if(newOptionalField.getText().isEmpty()){
            optionalAlertLabel.setStyle("-fx-text-fill: red");
            optionalAlertLabel.setText("You have to fill the box!");
            return;
        }
        try {
            categoryController.addField(newIntegerField.getText(), "OptionalField");
            optionalAlertLabel.setStyle("-fx-text-fill: green");
            optionalAlertLabel.setText("field added successfully");
            addNewField(newIntegerField.getText());
        } catch (CategoryController.ThereIsFieldWithNameException e) {
            optionalAlertLabel.setStyle("-fx-text-fill: red");
            optionalAlertLabel.setText("Failed! Due to duplicated name");
        } catch (CategoryController.ThereIsFieldWithNameInSubCategory thereIsFieldWithNameInSubCategory) {
            optionalAlertLabel.setStyle("-fx-text-fill: red");
            optionalAlertLabel.setText("Failed!There is field with other type in subCategory");
        }
    }
}
