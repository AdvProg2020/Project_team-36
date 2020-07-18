package Models;

import GUI.Constants;
import Network.Client;
import Repository.SaveCategory;
import Repository.SaveCustomer;
import Repository.SaveProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.ImageView;

import java.lang.reflect.Type;
import java.util.*;

public class Product implements Pendable{
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
    private ImageView productImage;
    private String productImageUrl = "";

    public Product(SaveProduct saveProduct) {
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
            Type type = new TypeToken<ArrayList<SaveCustomer>>(){}.getType();
            List<SaveCustomer> allSaveCustomers = gson.fromJson(response.getData(),type);
            Set<Customer> allCustomers = new HashSet<>();
            allSaveCustomers.forEach(saveCustomer -> allCustomers.add(new Customer(saveCustomer)));
            return allCustomers;
        }else {
            System.out.println(response);
            return null;
        }
    }

    public int getSeenNumber() {
        return seenNumber;
    }

    public ImageView getProductImage() {
        return productImage;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
