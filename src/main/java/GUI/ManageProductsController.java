package GUI;

import Controllers.ProductsController;
import Models.Product;
import Models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class ManageProductsController extends ManagerProfileController implements Initializable {

    public Label usernameLabel;
    public ImageView profilePicture;
    public TableView<Product> allProductsTable;
    public TableColumn<?, ?> productPictureColumn;
    public TableColumn<?, ?> productNameColumn;
    public TableColumn<?, ?> productIdColumn;
    public TableColumn<?, ?> removeColumn;
    public ComboBox sortName;
    public CheckBox isAscending;
    private User user;

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

        ArrayList<Product> allProducts = Constants.managerController.getAllProducts();
        setTheTable(allProducts);

    }

    private void setTheTable(ArrayList<Product> allProducts){
        allProductsTable.getItems().clear();
        Product.setManageProductsController(this);
        allProductsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productPictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProductImage"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>("removeHyperlink"));
        allProductsTable.getItems().addAll(allProducts);
    }

    public void removeAction(Product product){
        allProductsTable.getItems().remove(product);
    }

    public void sort(ActionEvent actionEvent) throws ProductsController.NoSortException {
            if (isAscending.isDisable())
                isAscending.setDisable(false);
            ArrayList<Product> products = Constants.managerController.sortProducts(sortName.getValue().toString(), isAscending.isSelected() ? "ascending" : "descending");
            setTheTable(products);

    }
}
