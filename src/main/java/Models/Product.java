package Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
    private int seenNumber;

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
        for (Product product : allProducts) {
            if (product.getProductId() == (id))
                return product;
        }
        return null;
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
                "    information: " + information + '\n'+
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

    public boolean isThereBuyer(Customer customer) {
        for (ProductField productField : this.productFields) {
            for (Customer buyer : productField.getAllBuyers()) {
                if (buyer.equals(customer))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String getPendingRequestType() {
        return "product";
    }

    //-..-
}
