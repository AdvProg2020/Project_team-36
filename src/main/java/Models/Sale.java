package Models;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static Models.ProductionStatus.TO_BE;

public class Sale implements Pendable {
    private static ArrayList<Sale> allSales = new ArrayList<>();
    private Seller seller;
    private int offId;
    private ArrayList<Product> productsInSale;
    private ProductionStatus status;
    private Date startTime;
    private Date endTime;
    private double salePercent;//be darsad nist
    static Random random = new Random();
    private static int totalOffsMade = random.nextInt(4988 - 1000) + 1000;
    private String editedField;
    private static Sale offToView;
    private static Sale offToEdit;

    public static ArrayList<Sale> getAllSales() {
        return allSales;
    }

    public Sale(Seller seller, ArrayList<Product> productsInSale, Date startTime, Date endTime, Double salePercent) {
        this.productsInSale = new ArrayList<>();
        this.seller = seller;
        this.offId = getRandomId();
        this.productsInSale = productsInSale;
        this.status = TO_BE;
        this.startTime = startTime;
        this.endTime = endTime;
        this.salePercent = salePercent;
    }


    public Sale(Sale sale){
        this.productsInSale = new ArrayList<>();
        this.seller = sale.seller;
        this.offId = sale.offId;
        
        this.productsInSale = new ArrayList<>(sale.productsInSale);
        this.status = sale.status;
        this.startTime = sale.startTime;
        this.endTime = sale.endTime;
        this.salePercent = sale.salePercent;
    }


    private int getRandomId(){
        totalOffsMade+=1;
        return totalOffsMade;
    }

    public int getOffId() {
        return offId;
    }

    public ArrayList<Product> getProductsInSale() {
        return productsInSale;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public boolean isSaleAvailable(){
        Date now = new Date();
        return (now.after(this.startTime) && now.before(this.endTime)) || now.equals(this.startTime) || now.equals(this.endTime);

    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public double getSalePercent() {
        return salePercent;
    }



    public void setEditedField(String editedField) {
        this.editedField = editedField;
    }

    public String getEditedField() {
        return editedField;
    }

    @Override
    public String toString() {
        return  "    offId: " + offId + '\n' +
                "    startTime: " + startTime + '\n' +
                "    endTime: " + endTime + '\n' +
                "    salePercent: " + (salePercent*100) + '\n'
                ;
    }

    public static Sale getSaleWithId(int offId){
        for (Sale sale : allSales) {
            if(sale.getOffId() == offId){
                return sale;
            }
        }
        return null;
    }

    public static boolean isThereSaleWithId(int offId){
        updateSales();
        for (Sale sale : allSales) {
            if(sale.getOffId()==offId){
                return true;
            }
        }
        return false;
    }

    private void removeProduct(Product product){
        productsInSale.remove(product);
    }

    public static void removeSale(Sale sale){
        sale.seller.removeSale(sale);
        for (Product product : sale.productsInSale) {
            product.getProductFieldBySeller(sale.seller).setSale(null);
        }
        allSales.remove(sale);
    }

    @Override
    public void acceptAddRequest() {
        seller.addSale(this);
        allSales.add(this);
        for (Product product : productsInSale) {
            if(Product.isThereProductWithId(product.getProductId())) {
                if(!(product.getProductFieldBySeller(seller).getSale()==null)){
                    product.getProductFieldBySeller(seller).getSale().removeProduct(product);
                }
                product.getProductFieldBySeller(seller).setSale(this);
            }
        }
    }

    @Override
    public void acceptEditRequest() {
        if(!isThereSaleWithId(this.offId)){
            return;
        }
        getSaleWithId(this.offId).setSalePercent(this.salePercent);
        getSaleWithId(this.offId).setEndTime(this.endTime);
        getSaleWithId(this.offId).setStartTime(this.startTime);
        getSaleWithId(this.offId).setProductsInSale(this.productsInSale);
    }

    @Override
    public String getPendingRequestType() {
        return "sale";
    }

    public Seller getSeller() {
        return seller;
    }

    public static Sale getSaleById(int id){
        for (Sale sale : allSales) {
            if (sale.offId == id){
                return sale;
            }
        }
        return null;
    }

    public Sale(Seller seller, int offId, ProductionStatus status, Date startTime, Date endTime, Double salePercent) {
        this.seller = seller;
        this.offId = offId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.salePercent = salePercent;
        this.productsInSale = new ArrayList<>();
    }

    public static void addToAllSales(Sale sale){
        allSales.add(sale);
    }


    public static void updateSales(){
        ArrayList<Sale> toBeRemoved = new ArrayList<>();
        ArrayList<Product> removingProducts= new ArrayList<>();
        for (Sale sale : allSales) {
            if(sale.getEndTime().before(new Date()))
                toBeRemoved.add(sale);
            else if(sale.seller.getStatus().equals(Status.DELETED))
                toBeRemoved.add(sale);
            else{
                for (Product product : sale.productsInSale) {
                    if(!Product.isThereProductWithId(product.getProductId())){
                        removingProducts.add(product);
                    }
                    else if(!product.isThereSeller(sale.seller)){
                        removingProducts.add(product);
                    }
                }
                if(removingProducts.size()==sale.productsInSale.size())
                    toBeRemoved.add(sale);
                sale.productsInSale.removeAll(removingProducts);
                removingProducts.clear();
            }
        }
        allSales.removeAll(toBeRemoved);
    }

    public void setProductsInSale(ArrayList<Product> productsInSale) {
        this.productsInSale.clear();
        this.productsInSale.addAll(productsInSale);
    }

    public void removeProducts(ArrayList<Product> products){
        productsInSale.removeAll(products);
    }

    public void addProducts(ArrayList<Product> products){
        productsInSale.addAll(products);
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setSalePercent(Double salePercent) {
        this.salePercent = salePercent;
    }

    public boolean isThereProduct(Product wantedProduct){
        for (Product product : productsInSale) {
            if(product.equals(wantedProduct)){
                return true;
            }
        }
        return false;
    }

    public int getSalePercentForTable(){
        return (int) (salePercent*100);
    }

    public static Sale getOffToView() {
        return offToView;
    }

    public static Sale getOffToEdit() {
        return offToEdit;
    }

    public static void setOffToView(Sale offToView) {
        Sale.offToView = offToView;
    }

    public static void setOffToEdit(Sale offToEdit) {
        Sale.offToEdit = offToEdit;
    }
}