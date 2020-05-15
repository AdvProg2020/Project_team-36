package Models;


import java.util.ArrayList;

public interface Filter {
     ArrayList<Product> filter(ArrayList<Product> products);
     String getName();

    //-..-
}
