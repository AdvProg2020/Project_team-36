package Models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GlobalVariables {
    private User loggedInUser;
    private String token;
    private ArrayList<Filter> allFiltersOffs;
    private String sortOff;
    private String sortOffType;
    private Category filterOffsCategory;
    private ArrayList<Filter> allFiltersProducts;
    private Category filterProductsCategory;
    private String sortProduct;
    private String sortProductType;
    private Product product;
    private Seller pendingSellerOfProduct;
    private Filter pendingFilter;

    public String getToken() {
        return token;
    }

    public GlobalVariables() {
        this.allFiltersOffs = new ArrayList<>();
        this.allFiltersProducts = new ArrayList<>();
        this.filterOffsCategory = null;
        this.filterProductsCategory = null;
        this.loggedInUser = null;
        this.pendingFilter = null;
        this.sortProduct = "seen count";
        this.sortOff = "seen count";
        this.sortOffType = "ascending";
        this.sortProductType = "ascending";
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setPendingSellerOfProduct(Seller pendingSellerOfProduct) {
        this.pendingSellerOfProduct = pendingSellerOfProduct;
    }

    public Seller getPendingSellerOfProduct() {
        return pendingSellerOfProduct;
    }

    public void setPendingFilter(Filter pendingFilter) {
        this.pendingFilter = pendingFilter;
    }


    public void setProductFilterOptions(ArrayList<String> options) {
        ((OptionalFilter) pendingFilter).setOptions(options);
        allFiltersProducts.add(pendingFilter);
        this.pendingFilter = null;
    }

    public void setOffFilterOptions(ArrayList<String> options) {
        ((OptionalFilter) pendingFilter).setOptions(options);
        allFiltersOffs.add(pendingFilter);
        this.pendingFilter = null;
    }

    public void setProductFilterRange(String min, String max) {
        BigDecimal mini = new BigDecimal(min);
        BigDecimal maxi = new BigDecimal(max);
        if (mini.compareTo(maxi) > 0) {
            ((RangeFilter) pendingFilter).setMin(max);
            ((RangeFilter) pendingFilter).setMax(min);
        } else {
            ((RangeFilter) pendingFilter).setMin(min);
            ((RangeFilter) pendingFilter).setMax(max);
        }
        allFiltersProducts.add(pendingFilter);
        this.pendingFilter = null;
    }

    public void setOffFilterRange(String min, String max) {
        BigDecimal mini = new BigDecimal(min);
        BigDecimal maxi = new BigDecimal(max);
        if (mini.compareTo(maxi) > 0) {
            ((RangeFilter) pendingFilter).setMin(max);
            ((RangeFilter) pendingFilter).setMax(min);
        } else {
            ((RangeFilter) pendingFilter).setMin(min);
            ((RangeFilter) pendingFilter).setMax(max);
        }
        allFiltersOffs.add(pendingFilter);
        this.pendingFilter = null;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setSortProduct(String name, String type) {
        this.sortProduct = name;
        this.sortProductType = type;
    }

    public void setFilterProductsCategory(Category filterProductsCategory) {
        this.filterProductsCategory = filterProductsCategory;
    }

    public void setFilterOffsCategory(Category filterOffsCategory) {
        this.filterOffsCategory = filterOffsCategory;
    }

    public Category getFilterOffsCategory() {
        return filterOffsCategory;
    }

    public Category getFilterProductsCategory() {
        return filterProductsCategory;
    }

    public ArrayList<Filter> getAllFiltersOffs() {
        return allFiltersOffs;
    }

    public ArrayList<Filter> getAllFiltersProducts() {
        return allFiltersProducts;
    }

    public void addFilterProducts(Filter filter) {
        allFiltersProducts.add(filter);
    }

    public void addFilterOffs(Filter filter) {
        allFiltersOffs.add(filter);
    }

    public String getSortProductType() {
        return sortProductType;
    }

    public String getSortOffType() {
        return sortOffType;
    }

    public void removeSortProduct() {
        this.sortProduct = "production date";
        this.sortProductType = "ascending";
    }

    public void removeSortOff() {
        this.sortOff = "production date";
        this.sortOffType = "ascending";
    }

    public String getSortProduct() {
        return this.sortProduct;
    }

    public void setSortOff(String name, String type) {
        this.sortOffType = type;
        this.sortOff = name;
    }

    public String getSortOff() {
        return sortOff;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
