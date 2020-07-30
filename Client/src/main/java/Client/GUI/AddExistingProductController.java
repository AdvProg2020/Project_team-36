package Client.GUI;

import Client.Models.Product;
import Client.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class AddExistingProductController extends SellerProfileController implements Initializable {

    public TableColumn<Client.Models.Product,String> productsType;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView profilePicture;
    @FXML
    private TableView<Client.Models.Product> productsTable;
    @FXML
    private TextField priceField;
    @FXML
    private TextField supplyField;
    @FXML
    private Label nameLabel;
    @FXML
    private Label companyLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TableColumn<Client.Models.Product, String> productsNameColumn;
    @FXML
    private TableColumn<Client.Models.Product, ImageView> productsPictureColumn;
    @FXML
    private ImageView productImage;
    private User user;
    private Client.Models.Product product;

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

        productsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
            try {
                product = newValue;
                setProduct();
            } catch (MalformedURLException e) {
                //
            }
        });

        priceField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches(".*[^\\d].*"))
                priceField.setText(priceField.getText().replaceAll("[^\\d]", ""));
        });

        supplyField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.matches(".*[^\\d].*"))
                supplyField.setText(supplyField.getText().replaceAll("[^\\d]", ""));
        });

        ArrayList<Client.Models.Product> allProducts = Constants.managerController.getAllProducts();
        List<Client.Models.Product> sellerAllProducts = ((Client.Models.Seller)user).getAllProducts();
        ArrayList<Product> productsToRemove = new ArrayList<>();
        for (Client.Models.Product product : sellerAllProducts) {
            for (Product toRemove : allProducts) {
                if(toRemove.getProductId()==product.getProductId()){
                    productsToRemove.add(toRemove);
                }
            }
        }
        allProducts.removeAll(productsToRemove);
        setTheTable(allProducts);
    }

    private void setProduct() throws MalformedURLException {
        productImage.setImage(product.getProductImage(100,100).getImage());
        nameLabel.setText(product.getName());
        companyLabel.setText(product.getCompany());
        infoLabel.setText(product.getInformation());
    }

    private void setTheTable(ArrayList<Client.Models.Product> allProducts){
        productsTable.getItems().clear();
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productsPictureColumn.setCellValueFactory(new PropertyValueFactory<>("smallProductImage"));
        productsType.setCellValueFactory(new PropertyValueFactory<>("pendingRequestType"));
        productsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
       productsTable.getItems().addAll(allProducts);
    }


    public void addSellerToProduct() throws IOException {

        if(priceField.getText().isEmpty() || supplyField.getText().isEmpty()){
            errorLabel.setVisible(true);
            return;
        }

        long price = Long.parseLong(priceField.getText());
        int supply = Integer.parseInt(supplyField.getText());

        Constants.sellerController.sendAddSellerToProductRequest(price, supply, product);
        AlertBox.display("Done!","Request for selling this product was sent.");
        Constants.getGuiManager().reopen();
    }
}
