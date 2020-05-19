package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class Product implements Pendable, Packable {
    private static ArrayList<Product> allProducts = new ArrayList<>();
    private int productId;
    private String name;
    private String company;
    private Category category;
    private ArrayList<Field> fieldsOfCategory;
    private String information;
    private ArrayList<ProductField> productFields;
    private ArrayList<Score> allScore;
    private Date productionDate;
    private ArrayList<Comment> allComments;
    private HashSet<Customer> allBuyers;
    private static int allProductsMade=0;
    private int seenNumber;

    public Product(String name, String company, Category category, ArrayList<Field> fieldsOfCategory,
                   String information, ProductField productField, Date productionDate) {
        this.productFields = new ArrayList<>();
        this.fieldsOfCategory = new ArrayList<>();
        this.allBuyers = new HashSet<>();
        this.allComments = new ArrayList<>();
        this.allScore = new ArrayList<>();
        this.productId = (allProductsMade+=1);
        this.name = name;
        this.company = company;
        this.category = category;
        this.fieldsOfCategory = fieldsOfCategory;
        this.information = information;
        this.productFields.add(productField);
        this.productionDate = productionDate;
    }

    public Product(Product product, ProductField productField) {
        this.productFields = new ArrayList<>();
        this.fieldsOfCategory = new ArrayList<>();
        this.productId = product.productId;
        this.name = product.name;
        this.company = product.company;
        this.category = product.category;
        this.productFields.add(productField);
        this.fieldsOfCategory = product.fieldsOfCategory;
        this.information = product.information;

    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public static Product getProduct(int id) {
        updateAllProducts();
        for (Product product : allProducts) {
            if (product.getProductId() == (id))
                return product;
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setFieldsOfCategory(ArrayList<Field> fieldsOfCategory) {
        this.fieldsOfCategory = fieldsOfCategory;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setProductFields(ArrayList<ProductField> productFields) {
        this.productFields = productFields;
    }

    public long getHighestCurrentPrice() {
        long price = 0;
        for (ProductField productField : this.productFields) {
            if (productField.getCurrentPrice() > price && !productField.getSeller().getStatus().equals(Status.DELETED))
                price = productField.getCurrentPrice();
        }
        return price;
    }

    public long getLowestCurrentPrice() {
        ArrayList<Long> temp = new ArrayList<>();

        for (ProductField productField : this.productFields) {
            if (!productField.getSeller().getStatus().equals(Status.DELETED))
                temp.add(productField.getCurrentPrice());
        }
        return Collections.min(temp);
    }

    public void seen() {
        this.seenNumber += 1;
    }

    public int getSeenNumber() {
        return seenNumber;
    }

    public double getScore() {
        int sum = 0;
        for (Score score : allScore) {
            sum += score.getScore();
        }
        int size = allScore.size();
        return (double) (sum / size);
    }

    public ArrayList<Comment> getAllComments() {
        return allComments;
    }

    public ArrayList<Score> getAllScore() {
        return allScore;
    }

    public static ArrayList<Product> getAllProducts() {
        updateAllProducts();
        return allProducts;
    }

    public static Product getProductWithId(int productId){
        updateAllProducts();
        for (Product product : allProducts) {
            if(product.getProductId()==productId){
                return product;
            }
        }
        return null;
    }

    public static ArrayList<Product> getAllInSaleProducts() {
        updateAllProducts();
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.isProductInSale())
                result.add(product);
        }
        return result;
    }

    public ProductField getBestSale() {
        ArrayList<ProductField> temp = new ArrayList<>();
        for (ProductField field : productFields) {
            if (field.getSale() != null)
                temp.add(field);
        }
        try {
            new Sort().sort(temp, ProductField.class.getDeclaredMethod("getCurrentPrice"), false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return temp.get(0);
    }

    public boolean isProductInSale() {
        for (ProductField field : productFields) {
            if (field.getSale() != null)
                return true;
        }
        return false;
    }

    public String getCompany() {
        return company;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<Seller> getAllSellers() {
        ArrayList<Seller> result = new ArrayList<>();
        for (ProductField productField : productFields) {
            if (productField.getSeller().getStatus().equals(Status.DELETED))
                result.add(productField.getSeller());
        }
        return result;
    }

    public ArrayList<Field> getFieldsOfCategory() {
        return fieldsOfCategory;
    }

    public Field getField(String name) {
        for (Field field : fieldsOfCategory) {
            if (field.getName().equalsIgnoreCase(name))
                return field;
        }
        return null;
    }

    public String getInformation() {
        return information;
    }

    public boolean enoughSupplyOfSeller(Seller seller, int count) {
        for (ProductField field : productFields) {
            if (field.getSeller().equals(seller)) {
                if (field.getSupply() >= count)
                    return true;
            }
        }
        return false;
    }

    public ArrayList<ProductField> getProductFields() {
        return productFields;
    }

    public void renameField(String oldName, String newName) {
        for (Field field : fieldsOfCategory) {
            if (field.getName().equalsIgnoreCase(newName)) {
                this.fieldsOfCategory.remove(this.getField(oldName));
                return;
            }
        }
        this.getField(oldName).setName(newName);
    }

    public void removeField(String name) {
        for (Field field : fieldsOfCategory) {
            if (field.getName().equalsIgnoreCase(name)) {
                fieldsOfCategory.remove(field);
                return;
            }
        }
    }

    public void addField(Field field) {
        fieldsOfCategory.add(field);
    }

    public boolean isThereField(String name) {
        for (Field field : fieldsOfCategory) {
            if (field.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "    productId: " + productId + '\n' +
                "    name: " + this.name + '\n' +
                "    company: " + company + '\n' +
                "    category: " + category.getName() + '\n' +
                "    information: " + information + '\n' +
                "    lowest price: " + this.getLowestCurrentPrice() + '\n'
                ;
    }

    public void addScore(Score score) {
        this.allScore.add(score);
    }

    public void buyProductFromSeller(Seller seller, int count) {
        for (ProductField field : productFields) {
            if (field.getSeller().equals(seller)) {
                field.buyFromSeller(count);
            }
        }
    }

    public boolean isProductDeleted() {
        if (productFields.isEmpty())
            return true;
        for (ProductField productField : productFields) {
            if (!productField.getSeller().getStatus().equals(Status.DELETED))
                return false;
        }
        return true;
    }

    public static boolean isThereProductWithId(int id){
        updateAllProducts();
        for (Product product : allProducts) {
            if(product.getProductId()==id){
                return true;
            }
        }
        return false;
    }

    public static void updateAllProducts(){
        ArrayList<ProductField> tempProductField = new ArrayList<>();
        ArrayList<Product> tempProduct = new ArrayList<>();
        for (Product product : allProducts) {
            for (ProductField field : product.getProductFields()) {
                if (field.getSeller().getStatus().equals(Status.DELETED))
                    tempProductField.add(field);
            }
            if (tempProductField.size() == product.getProductFields().size())
                tempProduct.add(product);
            product.getProductFields().removeAll(tempProductField);
            tempProductField.clear();
        }
        for (Product product : tempProduct) {
            product.getCategory().removeProduct(product);
        }
        allProducts.removeAll(tempProduct);
    }

    public static void removeProduct(Product product){
        for (ProductField productField : product.getProductFields()) {
            productField.getSeller().removeProduct(product);
        }
        product.getCategory().removeProduct(product);
        allProducts.remove(product);
    }

    public void removeSeller(Seller seller){
        for (ProductField field : productFields) {
            if(field.getSeller().equals(seller)){
                productFields.remove(field);
                break;
            }
        }
        if(productFields.size()==0)
            removeProduct(this);
    }

    public StringBuilder printSellerProductDetails(Seller seller){
        ProductField productField = getProductFieldBySeller(seller);
        StringBuilder toBePrinted = new StringBuilder();
        toBePrinted.append("    product id: ").append(productId).append("\n    name: ").append(name).
                    append("\n    company: ").append(company).append("\n    price: ").
                    append(productField.getPrice()).append("\n    supply: ").append(productField.getSupply()).
                    append("    category: ").append(category.getName()).append('\n');
        for (Field field : fieldsOfCategory) {
            toBePrinted.append("       ").append(field.getFieldInfo()).append('\n');
        }
        toBePrinted.append("    information: ").append(information).append("\n    production date: ").
                append(productionDate).append("\n    seen number: ").append(company).append('\n');
        return toBePrinted;
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }

    public ProductField getProductFieldBySeller(Seller seller) {
        for (ProductField productField : productFields) {
            if (productField.getSeller().equals(seller))
                return productField;
        }
        return null;
    }

    public void addProductField(ProductField productField){
        productFields.add(productField);
    }

    public boolean isThereBuyer(Customer customer) {
        for (ProductField productField : this.productFields) {
            for (Customer buyer : productField.getAllBuyers()) {
                if (buyer.equals(customer))
                    return true;
            }
        }
        return false;
    }

    public void addComment(Comment comment){
        allComments.add(comment);
    }

    @Override
    public void acceptAddRequest() {
        category.addProduct(this);
        productFields.get(0).getSeller().addProduct(this);
        allProducts.add(this);
    }

    @Override
    public void acceptEditRequest() {

    }

    @Override
    public String getPendingRequestType() {
        return "product";
    }

    //-..-
}
