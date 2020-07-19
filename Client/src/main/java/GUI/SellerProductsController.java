package GUI;

import Models.Product;
import Models.Seller;
import Models.User;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class  SellerProductsController extends SellerProfileController implements Initializable {


    public ImageView profilePicture;
    public Label usernameLabel;
    public TableColumn<?, ?> buyersColumn;
    public TableColumn<?, ?> productIdColumn;
    public TableColumn<?, ?> productNameColumn;
    public TableColumn<?, ?> productPictureColumn;
    public TableView<Product> allProductsTable;
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

        List<Product> allProducts = ((Seller)user).getAllProducts();
        setTheTable(allProducts);
    }


    private void setTheTable(List<Product> allProducts){
        Product.setSellerProductsController(this);
        allProductsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productPictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProductImage"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        buyersColumn.setCellValueFactory(new PropertyValueFactory<>("buyersDropDown"));
        allProductsTable.getItems().addAll(allProducts);
    }

    public void viewAction() throws IOException {
        TableView.TableViewSelectionModel<Product> selectedProduct = allProductsTable.getSelectionModel();

        if (selectedProduct.isEmpty()) {
            return;
        }

        Product toBeViewed = selectedProduct.getSelectedItem();
        productController.setProductToView(toBeViewed);
        Constants.getGuiManager().open("ViewSellerProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void editAction() throws IOException {
        TableView.TableViewSelectionModel<Product> selectedProduct = allProductsTable.getSelectionModel();

        if (selectedProduct.isEmpty()) {
            return;
        }

        Product toBeEdited = selectedProduct.getSelectedItem();
        productController.setProductToEdit(toBeEdited);
        Constants.getGuiManager().open("EditSellerProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }


    public void removeAction() {
        TableView.TableViewSelectionModel<Product> selectedProduct = allProductsTable.getSelectionModel();

        if (selectedProduct.isEmpty()) {
            return;
        }

        Product toBeRemoved = selectedProduct.getSelectedItem();
        managerController.removeProduct(toBeRemoved);
        allProductsTable.getItems().remove(toBeRemoved);
    }

    public void addNewProductAction() throws IOException {
        Constants.getGuiManager().open("AddNewProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void addExistingProduct() throws IOException {
        Constants.getGuiManager().open("AddExistingProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }
}
