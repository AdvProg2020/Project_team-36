package Client.Models;

import Client.GUI.Constants;
import Client.Network.Client;
import Models.*;
import Repository.SaveCategory;
import Repository.SaveCustomer;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.*;

public class Product implements Pendable {
    private SaveProduct saveProduct;
    private int productId;
    private String name;
    private String company;
    private List<Field> fieldsOfCategory;
    private String information;
    private List<ProductField> productFields;
    private List<Score> allScore;
    private Date productionDate;
    private List<Comment> allComments;
    private int seenNumber;
    private String productImageUrl = "";
    private FileProduct fileProduct;

    public Product(SaveProduct saveProduct) {
        this.fileProduct = saveProduct.getFileProduct();
        this.saveProduct = saveProduct;
        this.name = saveProduct.getName();
        this.productId = saveProduct.getProductId();
        this.company = saveProduct.getCompany();
        this.information = saveProduct.getInformation();
        this.productionDate = new Date(saveProduct.getProductionDate());
        this.seenNumber = saveProduct.getSeenNumber();
        this.fieldsOfCategory = new ArrayList<>();
        saveProduct.getOptionalFieldsOfCategory().forEach(optionalField -> fieldsOfCategory.add(optionalField));
        saveProduct.getIntegerFieldsOfCategory().forEach(integerField -> fieldsOfCategory.add(integerField));
        this.productFields = new ArrayList<>();
        saveProduct.getProductFields().forEach(saveProductField -> productFields.add(new ProductField(saveProductField)));
        this.allScore = new ArrayList<>();
        saveProduct.getAllScore().forEach(saveScore -> allScore.add(new Score(saveScore)));
        this.allComments = new ArrayList<>();
        saveProduct.getAllComments().forEach(saveComment -> allComments.add(new Comment(saveComment)));
        this.productImageUrl = saveProduct.getProductImageURL();
    }

    public SaveProduct getSaveProduct() {
        return saveProduct;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public Category getCategory() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Category");
        query.getMethodInputs().put("id", "" + saveProduct.getCategoryId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Category")) {
            Gson gson = new Gson();
            SaveCategory saveCategory = gson.fromJson(response.getData(), SaveCategory.class);
            return new Category(saveCategory);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public List<Field> getFieldsOfCategory() {
        return fieldsOfCategory;
    }

    public String getInformation() {
        return information;
    }

    public List<ProductField> getProductFields() {
        return productFields;
    }

    public List<Score> getAllScore() {
        return allScore;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public List<Comment> getAllComments() {
        return allComments;
    }

    public Set<Customer> getAllBuyers() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetAllById", "Customer");
        this.saveProduct.getAllBuyers().forEach(id -> query.getMethodInputs().put(id + "", ""));
        Response response = Client.process(query);
        if (response.getReturnType().equals("List<Customer>")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<SaveCustomer>>() {
            }.getType();
            List<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(), type);
            Set<Customer> allCustomers = new HashSet<>();
            allSaveCustomers.forEach(saveCustomer -> allCustomers.add(new Customer(saveCustomer)));
            return allCustomers;
        } else {
            System.out.println(response);
            return null;
        }
    }

    public int getSeenNumber() {
        return seenNumber;
    }

    public ImageView getSmallProductImage() throws MalformedURLException {
        byte[] bytes =null ;
        try {
            bytes = Client.readFile(productImageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image img = new Image(new ByteArrayInputStream(bytes),50,50,false,false);
        return new ImageView(img);
    }

    public ImageView getProductImage(int height, int width) throws MalformedURLException {
        byte[] bytes =null ;
        try {
            bytes = Client.readFile(productImageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image img = new Image(new ByteArrayInputStream(bytes),width,height,false,false);
        return new ImageView(img);
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public long getScore() {
        if (allScore.size() == 0)
            return 0;
        int sum = 0;
        for (Score score : allScore) {
            sum += score.getScore();

        }
        return (long) sum / allScore.size();
    }

    public int getTotalSupply() {
        int sum = 0;
        for (ProductField field : productFields) {
            sum += field.getSupply();
        }
        return sum;
    }

    @Override
    public String getPendingRequestType() {
        if(fileProduct==null)
            return "product";
        return "file product";
    }

    public ProductField getProductFieldBySeller(int sellerId) {
        for (ProductField productField : productFields) {
            if (productField.getSeller().getUserId() == sellerId)
                return productField;
        }
        return null;
    }

    public long getLowestPrice() {
        ArrayList<Long> temp = new ArrayList<>();
        for (ProductField productField : this.productFields) {
            if (!productField.getSeller().getStatus().equals(Status.DELETED))
                temp.add(productField.getPrice());
        }
        return Collections.min(temp);
    }

    public long getLowestCurrentPrice() {
        ArrayList<Long> temp = new ArrayList<>();

        for (ProductField productField : this.productFields) {
            if (!productField.getSeller().getStatus().equals(Status.DELETED))
                temp.add(productField.getPrice());
        }
        return Collections.min(temp);
    }

    public Field getField(String name) {
        for (Field field : fieldsOfCategory) {
            if (field.getName().equalsIgnoreCase(name))
                return field;
        }
        return null;
    }

    public ComboBox<String> getBuyersDropDown(){

        ArrayList<String> usernames = new ArrayList<>();
        for (Customer buyer : getAllBuyers()) {
            usernames.add(buyer.getUsername());
        }

        ComboBox<String> comboBox =
                new ComboBox<>(FXCollections
                        .observableArrayList(usernames));
        return comboBox;
    }

    public FileProduct getFileProduct() {
        return fileProduct;
    }

    public boolean isFileProduct(){
        return !(fileProduct==null);
    }

    @Override
    public String toString() {
        return "    productId: " + productId + '\n' +
                "    name: " + this.name + '\n' +
                "    company: " + company + '\n' +
                "    category: " + this.getCategory().getName() + '\n' +
                "    information: " + information + '\n' +
                "    price: " + this.getLowestCurrentPrice() + '\n'
                ;
    }

}
