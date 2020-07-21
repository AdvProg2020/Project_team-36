package GUI;

import Models.Product;
import Models.Seller;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.List;

public class  SellerProductsController extends SellerProfileController implements Initializable {


    @FXML private ImageView profilePicture;
    @FXML private Label usernameLabel;
    @FXML private TableColumn<?, ?> buyersColumn;
    @FXML private TableColumn<?, ?> productIdColumn;
    @FXML private TableColumn<?, ?> productNameColumn;
    @FXML private TableColumn<?, ?> productPictureColumn;
    @FXML private TableView<Product> allProductsTable;

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

        List<Product> allProducts = ((Seller) user).getAllProducts();
        setTheTable(allProducts);
    }


    private void setTheTable(List<Product> allProducts){
        allProductsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //todo inam dorost she :)))
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
        Constants.productsController.setProductToView(toBeViewed);
        Constants.getGuiManager().open("ViewSellerProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void editAction() throws IOException {
        TableView.TableViewSelectionModel<Product> selectedProduct = allProductsTable.getSelectionModel();

        if (selectedProduct.isEmpty()) {
            return;
        }

        Product toBeEdited = selectedProduct.getSelectedItem();
        Constants.productsController.setProductToEdit(toBeEdited);
        Constants.getGuiManager().open("EditSellerProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void removeAction() {
        TableView.TableViewSelectionModel<Product> selectedProduct = allProductsTable.getSelectionModel();

        if (selectedProduct.isEmpty()) {
            return;
        }

        Product toBeRemoved = selectedProduct.getSelectedItem();
        Constants.managerController.removeProduct(toBeRemoved);
        allProductsTable.getItems().remove(toBeRemoved);
    }

    public void addNewProductAction() throws IOException {
        Constants.getGuiManager().open("AddNewProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }

    public void addExistingProduct() throws IOException {
        Constants.getGuiManager().open("AddExistingProduct",Constants.globalVariables.getLoggedInUser().getUserId());
    }
}
