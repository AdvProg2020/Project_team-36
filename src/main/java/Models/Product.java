package Models;

import java.util.ArrayList;

public class Product implements Pendable,Packable {
    private static ArrayList<Product> allProducts = new ArrayList<>();
    private int productId;
    private String name;
    private String company;
    private Category category;
    private ArrayList<Field> fieldsOfCategory;
    private String information;
    private ArrayList<ProductField> productFields;
    private ArrayList<Score> allScore;
    private ArrayList<Comment> allComments;


    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public static Product getProduct(int id){
        for (Product product : allProducts) {
            if(product.getProductId() == id)
                return product;
        }
        return null;
    }

    public ArrayList<Comment> getAllComments() {
        return allComments;
    }

    public ArrayList<Score> getAllScore() {
        return allScore;
    }

    public static ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public String getCompany() {
        return company;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<Seller> getAllSellers(){
        ArrayList<Seller> result = new ArrayList<>();
        for (ProductField productField : productFields) {
            result.add(productField.getSeller());
        }
        return result;
    }

    public ArrayList<Field> getFieldsOfCategory() {
        return fieldsOfCategory;
    }

    public Field getField(String name){
        for (Field field : fieldsOfCategory) {
            if(field.getName().equalsIgnoreCase(name))
                return field;
        }
        return null;
    }

    public String getInformation() {
        return information;
    }

    public boolean enoughSupplyOfSeller(Seller seller, int count){
        for (ProductField field : productFields) {
            if(field.getSeller().equals(seller)){
                if(field.getSupply()>=count)
                    return true;
            }
        }
        return false;
    }

    public ArrayList<ProductField> getProductFields() {
        return productFields;
    }

    public void renameField(String oldName,String newName){
        for (Field field : fieldsOfCategory) {
            if(field.getName().equalsIgnoreCase(newName)){
                this.fieldsOfCategory.remove(this.getField(oldName));
                return;
            }
        }
        this.getField(oldName).setName(newName);
    }

    public void removeField(String name){
        for (Field field : fieldsOfCategory) {
            if(field.getName().equalsIgnoreCase(name)) {
                fieldsOfCategory.remove(field);
                return;
            }
        }
    }

    public void addField(Field field){
        fieldsOfCategory.add(field);
    }

    public boolean isThereField(String name){
        for (Field field : fieldsOfCategory) {
            if(field.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return  "    productId: " + productId + '\n' +
                "    name: " + name + '\n' +
                "    company: " + company + '\n' +
                "    category: " + category + '\n' +
                "    information: " + information + '\n'
                ;
    }

    public void addScore(Score score){
        this.allScore.add(score);
    }

    public void buyProductFromSeller(Seller seller, int count){
        for (ProductField field : productFields) {
            if(field.getSeller().equals(seller)){
                field.buyFromSeller(count);
            }
        }
    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }

    @Override
    public String getPendingRequestType() {
        return "product";
    }

    public ProductField getProductFieldBySeller(Seller seller){
        for (ProductField productField : productFields) {
            if(productField.getSeller().equals(seller))
                return productField;
        }
        return null;
    }

    public boolean isThereBuyer(Customer customer){
        for (ProductField productField : this.productFields) {
            for (Customer buyer : productField.getAllBuyers()) {
                if(buyer.equals(customer))
                    return true;
            }
        }
        return false;
    }
}
