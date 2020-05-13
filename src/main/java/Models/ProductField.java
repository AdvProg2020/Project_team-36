package Models;

import java.util.ArrayList;
import java.util.Date;

public class ProductField {
    private ProductionStatus status;
    private Long price;
    private Seller seller;
    private int supply;
    private Date productionDate;
    private ArrayList<Comment> allComments;
    private ArrayList<Score> allScore;

    public ProductionStatus getStatus() {
        return status;
    }

    public Long getPrice() {
        return price;
    }

    public Seller getSeller() {
        return seller;
    }

    public int getSupply() {
        return supply;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public ArrayList<Comment> getAllComments() {
        return allComments;
    }

    public ArrayList<Score> getAllScore() {
        return allScore;
    }

    //-..-
}
