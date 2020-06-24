package Controllers;

import Models.BooleanFilter;
import Models.Category;
import Models.Filter;
import Models.Product;

import java.util.ArrayList;
import java.util.Set;

public interface ObjectController {

    String getProductCurrentSortName();

    String getSortProductType();

    void removeSortProduct();

    void setSort(String name, String type) throws ProductsController.NoSortException;

    Set<String> getAvailableFilters();

    ArrayList<Filter> getCurrentFilters();

    Category getCurrentCategoryFilter();

    void setNewFilter(String name) throws ProductsController.IntegerFieldException, ProductsController.OptionalFieldException, ProductsController.NoFilterWithNameException;

    void setFilterRange(String min, String max);

    void setFilterOptions(ArrayList<String> options);

    void removeFilter(String name) throws ProductsController.NoFilterWithNameException;

    void setCategoryFilter(String name) throws ProductsController.NoCategoryWithName;

    Set<String> getAvailableSorts();

    void setCompanyFilter(ArrayList<String> options);

    void addNameFilter(String name);

    void removeNameFilter(String name);

    void availabilityFilter();

    void removeAvailabilityFilter();

    void addSellerFilter(String name);

    void removeSellerFilter(String name);
}
