package Models;

import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Product implements Pendable {
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
    static Random random = new Random();
    private static int allProductsMade = random.nextInt(4988 - 1000) + 1000;
    private int seenNumber;
    private String productImageUrl = "";
    private String editedField;
    private static Product productToEdit;
    private static Product productToView;
    private FileProduct fileProduct;

    public String getProductImageUrl() {
        return productImageUrl;
    }

//    public static void addTest(){
//        ArrayList<Field> fields = new ArrayList<>();
//        IntegerField size = new IntegerField("size");
//        size.setValue("5000");
//        IntegerField productionDate = new IntegerField("production date");
//        productionDate.setValue("2020");
//        OptionalField color = new OptionalField("color");
//        color.setValue("red");
//        OptionalField waterProof = new OptionalField("waterProof");
//        waterProof.setValue("high level");
//        fields.add(size);
//        fields.add(color);
//        fields.add(productionDate);
//        fields.add(waterProof);
//        Seller seller = new Seller("seller");
//        Image image = new Image(Product.class.getResource("/images/edit.png").toExternalForm());
//        Product product = new Product("abc","team36",new Category("testingCategory"),fields,
//                "this a string of information about this product.",new ProductField(2000,seller,
//                100,0),new Date(), "/images/edit.png");
//        product.setProductImage(new ImageView(image));
//        ArrayList<Product> sale=  new ArrayList<>();
//        sale.add(product);
//        product.productImageUrl = "/images/edit.png";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        dateFormat.setLenient(false);
//        Date startDate=null;
//        Date endDate=null;
//        try {
//            startDate = dateFormat.parse("2020/5/10");
//            endDate = dateFormat.parse("2020/10/10");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        product.getProductFieldBySeller(seller).setSale(new Sale(seller,sale,startDate,endDate,0.2));
//        allProducts.add(product);
//        seller.addProduct(product);
//        test1();
//        test2();
//    }
//
//    private static void test1(){
//        ArrayList<Field> fields = new ArrayList<>();
//        IntegerField size = new IntegerField("size");
//        size.setValue("4000");
//        IntegerField productionDate = new IntegerField("production date");
//        productionDate.setValue("2018");
//        OptionalField color = new OptionalField("color");
//        color.setValue("red");
//        OptionalField waterProof = new OptionalField("waterProof");
//        waterProof.setValue("high level");
//        fields.add(size);
//        fields.add(color);
//        fields.add(productionDate);
//        fields.add(waterProof);
//        Seller seller = new Seller("sell");
//        Image image = new Image(Product.class.getResource("/images/edit.png").toExternalForm());
//        Product product = new Product("def","team37",new Category("nazanin"),fields,
//                "this a string of information about that product.",new ProductField(755555,seller,
//                0,123),new Date(), "/images/edit.png");
//        product.setProductImage(new ImageView(image));
//        ArrayList<Product> sale=  new ArrayList<>();
//        sale.add(product);
//        product.productImageUrl = "/images/edit.png";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        dateFormat.setLenient(false);
//        Date startDate=null;
//        Date endDate=null;
//        try {
//            startDate = dateFormat.parse("2018/5/10");
//            endDate = dateFormat.parse("2019/10/10");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        product.getProductFieldBySeller(seller).setSale(new Sale(seller,sale,startDate,endDate,0.8));
//        allProducts.add(product);
//        seller.addProduct(product);
//
//
//    }
//
//    private static void test2(){
//       for(int i=0;i<20;i++) {
//            ArrayList<Field> fields = new ArrayList<>();
//            IntegerField size = new IntegerField("size");
//            size.setValue("4000");
//            IntegerField productionDate = new IntegerField("production date");
//            productionDate.setValue("2018");
//            OptionalField color = new OptionalField("color");
//            color.setValue("red");
//            OptionalField waterProof = new OptionalField("waterProof");
//            waterProof.setValue("high level");
//            fields.add(size);
//            fields.add(color);
//            fields.add(productionDate);
//            fields.add(waterProof);
//            Seller seller = new Seller("what?");
//            Image image = new Image(Product.class.getResource("/images/edit.png").toExternalForm());
//            Product product = new Product("nazanin", "shalalay", new Category("NAZANIN"), fields,
//                    "this a string of information about nazanin.", new ProductField(i*4567+45, seller,
//                    i*23+4, 456), new Date(), "/images/edit.png");
//            product.setProductImage(new ImageView(image));
//            ArrayList<Product> sale = new ArrayList<>();
//            sale.add(product);
//           // product.productImageUrl = ;
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//            dateFormat.setLenient(false);
//            Date startDate = null;
//            Date endDate = null;
//            try {
//                startDate = dateFormat.parse("2017/5/10");
//                endDate = dateFormat.parse("2020/10/30");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            product.getProductFieldBySeller(seller).setSale(new Sale(seller, sale, startDate, endDate, 0.15));
//            allProducts.add(product);
//            seller.addProduct(product);
//        }
//    }


    public Product(String name, String company, Category category, ArrayList<Field> fieldsOfCategory,
                   String information, ProductField productField, Date productionDate, String productImageURL) {
        this.productFields = new ArrayList<>();
        this.fieldsOfCategory = new ArrayList<>(fieldsOfCategory);
        this.allBuyers = new HashSet<>();
        this.allComments = new ArrayList<>();
        this.allScore = new ArrayList<>();
        this.productId = (allProductsMade+=1);
        this.name = name;
        this.company = company;
        this.category = category;
        this.information = information;
        this.productFields.add(productField);
        this.productionDate = productionDate;
        productField.setMainProductId(this.productId);
        this.productImageUrl = productImageURL;
    }

    public Product(Product product, ProductField productField) {
        this.productFields = new ArrayList<>();
        this.fieldsOfCategory = new ArrayList<>(product.getFieldsOfCategory());
        this.allBuyers = new HashSet<>(product.getAllBuyers());
        this.allComments = new ArrayList<>(product.getAllComments());
        this.allScore = new ArrayList<>(product.getAllScore());
        this.productId = product.getProductId();
        this.name = product.getName();
        this.company = product.getCompany();
        this.category = product.getCategory();
        this.productFields.add(productField);
        this.productionDate = product.getProductionDate();
        this.information = product.getInformation();
    }

    public void setFileProduct(FileProduct fileProduct){
        this.fileProduct = fileProduct;
    }


    public boolean isInAuction(){
        for (ProductField field : productFields) {
            if(field.isInAuction())
                return true;
        }
        return false;
    }
    public Product(int productId, String name, String company, Category category,
                   ArrayList<IntegerField> integerFieldsOfCategory,ArrayList<OptionalField> optionalFieldsOfCategory,
                   String information, Date productionDate, int seenNumber, String productImageURL,FileProduct fileProduct) {
        this.productImageUrl = productImageURL;
        this.productId = productId;
        this.name = name;
        this.company = company;
        this.category = category;
        this.fieldsOfCategory = new ArrayList<>();
        optionalFieldsOfCategory.forEach(fieldsOfCategory -> this.fieldsOfCategory.add(fieldsOfCategory));
        integerFieldsOfCategory.forEach(fieldsOfCategory -> this.fieldsOfCategory.add(fieldsOfCategory));
        this.information = information;
        this.productionDate = productionDate;
        this.seenNumber = seenNumber;
        this.productFields = new ArrayList<>();
        this.allComments = new ArrayList<>();
        this.allScore = new ArrayList<>();
        this.allBuyers = new HashSet<>();
        this.fileProduct = fileProduct;
    }

    public int getProductId() {
        return productId;
    }

    public static Product getProductById(int productId){
        for (Product product : allProducts) {
            if(product.productId == productId){
                return product;
            }
        }
        return null;
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
            if (product.getProductId() == id)
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
        fieldsOfCategory.clear();
        this.fieldsOfCategory.addAll(fieldsOfCategory);
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setProductFields(ArrayList<ProductField> productFields) {
        this.productFields = productFields;
    }

    public void addBuyer(Customer customer){
        this.allBuyers.add(customer);
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

    public long getLowestPrice(){
        ArrayList<Long> temp = new ArrayList<>();

        for (ProductField productField : this.productFields) {
            if (!productField.getSeller().getStatus().equals(Status.DELETED))
                temp.add(productField.getOfficialPrice());
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
        if(allScore.isEmpty())
            return 0;
        for (Score score : allScore) {
            sum += score.getScore();
        }
        int size = allScore.size();
        return  ((double)sum / size);
    }

    public ArrayList<Comment> getAllComments() {
        this.updateComments();
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

    public ProductField getBestSale() throws NoSaleForProduct{
        ArrayList<ProductField> temp = new ArrayList<>();
        for (ProductField field : productFields) {
            if (field.getSale() != null)
                temp.add(field);
        }
        if(temp.isEmpty())
            throw new NoSaleForProduct();
        try {
            new Sort().sort(temp, ProductField.class.getDeclaredMethod("getCurrentPrice"), false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return temp.get(0);
    }

    public boolean isProductInSale() {
        for (ProductField field : productFields) {
            if (field.getSale() != null&&field.getSale().isSaleAvailable())
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
            if (!productField.getSeller().getStatus().equals(Status.DELETED))
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
                "    price: " + this.getLowestCurrentPrice() + '\n'
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

    public boolean isThereSeller(Seller seller){
        for (ProductField field : productFields) {
            if(seller.equals(field.getSeller()))
                return true;
        }
        return false;
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
        this.updateBuyers();
        for (Customer buyer : allBuyers) {
            if(buyer.equals(customer))
                return true;
        }
        return false;
    }

    public void addComment(Comment comment){
        allComments.add(comment);
    }

    public String getEditedField() {
        return editedField;
    }

    public void setEditedField(String editedField) {
        this.editedField = editedField;
    }

    @Override
    public void acceptAddRequest() {
        category.addProduct(this);
        productFields.get(0).getSeller().addProduct(this);
        allProducts.add(this);
    }

    @Override
    public void acceptEditRequest() {
        updateAllProducts();
        if(!Product.isThereProductWithId(this.getProductId())){
            return;
        }
        Product mainProduct = Product.getProductWithId(this.getProductId());
        mainProduct.setProductImageUrl(this.productImageUrl);
        mainProduct.setName(this.name);
        mainProduct.setCompany(this.company);
        mainProduct.setInformation(this.information);
        mainProduct.setCategory(this.category);
        mainProduct.setFieldsOfCategory(this.fieldsOfCategory);
    }
    @Override
    public String getPendingRequestType() {
        return "product";
    }

    public static void addToAllProducts(Product product){
        allProducts.add(product);
    }

    public void updateComments(){
        ArrayList<Comment> temp = new ArrayList<>();
        for (Comment comment : this.allComments) {
            if(comment.getUser().getStatus().equals(Status.DELETED))
                temp.add(comment);
        }
        this.allComments.removeAll(temp);
    }

    public int getTotalSupply(){
        int sum=0;
        for (ProductField field : productFields) {
            if(!field.isInAuction())
            sum+=field.getSupply();
        }
        return sum;
    }

    public static void updateAllProducts(){
        ArrayList<ProductField> tempProductField = new ArrayList<>();
        ArrayList<Product> tempProduct = new ArrayList<>();
        for (Product product : allProducts) {
            for (ProductField field : product.getProductFields()) {
                if (field.getSeller().getStatus().equals(Status.DELETED))
                    tempProductField.add(field);
                else{
                    field.updateProductField();

                }
            }
            if (tempProductField.size() == product.getProductFields().size())
                tempProduct.add(product);
            product.getProductFields().removeAll(tempProductField);
            tempProductField.clear();
            product.updateBuyers();
            product.updateComments();
        }
        for (Product product : tempProduct) {
            product.getCategory().removeProduct(product);
        }
        allProducts.removeAll(tempProduct);
    }

    public boolean isProductAvailable(){
        return this.getTotalSupply()>0;
    }

    public boolean isThereSeller(String username){
        for (ProductField field : productFields) {
            if(field.getSeller().getUsername().equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

    public void updateBuyers(){
        HashSet<Customer> toBeRemoved = new HashSet<>();
        for (Customer buyer : allBuyers) {
            if(buyer.getStatus().equals(Status.DELETED))
                toBeRemoved.add(buyer);
        }
        allBuyers.removeAll(toBeRemoved);
    }


    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }


    public HashSet<Customer> getAllBuyers() {
        return allBuyers;
    }

    public static class NoSaleForProduct extends Exception{

    }

    public static Product getProductToEdit() {
        return productToEdit;
    }

    public static Product getProductToView() {
        return productToView;
    }

    public static void setProductToEdit(Product productToEdit) {
        Product.productToEdit = productToEdit;
    }

    public static void setProductToView(Product productToView) {
        Product.productToView = productToView;
    }

    public FileProduct getFileProduct() {
        return fileProduct;
    }

    public boolean isFileProduct(){
        return !(fileProduct==null);
    }
}
