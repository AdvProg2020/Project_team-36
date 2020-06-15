package GUI;

import Models.Product;
import Models.User;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class ManageProductsController extends ManagerProfileController implements Initializable {

    public Label usernameLabel;
    public ImageView profilePicture;
    public TableView allProductsTable;
    public TableColumn productPictureColumn;
    public TableColumn productNameColumn;
    public TableColumn productIdColumn;
    public TableColumn removeColumn;
    private User manager;

    @Override
    public void initialize(int id) {

//        manager = Constants.loggedInUser.getLoggedInUser();
//        Image profile = new Image(getClass().getResource(manager.getProfilePictureUrl()).toExternalForm(),150,150,false,false);
//        profilePicture.setImage(profile);
//        usernameLabel.setText(manager.getUsername());

        Product.setManageProductsController(this);
        allProductsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productPictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProductImage"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>("removeHyperlink"));

        ArrayList<Product> allProducts = Constants.managerController.getAllProducts();
        allProductsTable.getItems().addAll(allProducts);

    }

    public void removeAction(Product product){
        allProductsTable.getItems().remove(product);
    }
}
