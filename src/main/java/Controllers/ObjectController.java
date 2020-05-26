package Controllers;

import Models.Category;
import Models.Filter;

import java.util.ArrayList;
import java.util.Set;

public interface ObjectController {

    public String getProductCurrentSortName();

    public String getSortProductType();

    public void removeSortProduct();

    public void setSort(String name, String type) throws ProductsController.NoSortException;

    public Set<String> getAvailableFilters();

    public ArrayList<Filter> getCurrentFilters();

    public Category getCurrentCategoryFilter();

    public void setNewFilter(String name) throws ProductsController.IntegerFieldException, ProductsController.OptionalFieldException, ProductsController.NoFilterWithNameException;

    public void setFilterRange(String min, String max);

    public void setFilterOptions(ArrayList<String> options);

    public void removeFilter(String name) throws ProductsController.NoFilterWithNameException;

    public void setCategoryFilter(String name) throws ProductsController.NoCategoryWithName;

    public Set<String> getAvailableSorts();
}
