package Repository;

import Models.Pendable;
import Models.Status;

public class SaveRequest {
    private int requestId;
    private Status status;
    private SaveProduct saveProduct;
    private SaveProductField saveProductField;
    private SaveComment saveComment;
    private SaveSeller saveSeller;
    private SaveSale saveSale;

    private static int lastId;

}
