package Repository;

import Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveRequest {
    private int requestId;
    private Status status;
    private SaveProduct saveProduct;
    private SaveProductField saveProductField;
    private SaveComment saveComment;
    private SaveSeller saveSeller;
    private SaveSale saveSale;

    private static int lastId;

    private SaveRequest() {
    }

    public static void save(Request request) {
        SaveRequest saveRequest = new SaveRequest();
        saveRequest.requestId = request.getRequestId();
        saveRequest.status = request.getStatus();

        if (request.getPendableRequest() instanceof Product) {
            saveRequest.saveProduct = new SaveProduct((Product) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof ProductField) {
            saveRequest.saveProductField = new SaveProductField((ProductField) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof Comment) {
            saveRequest.saveComment = new SaveComment((Comment) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof Seller) {
            saveRequest.saveSeller = new SaveSeller((Seller) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof Sale) {
            saveRequest.saveSale = new SaveSale((Sale) request.getPendableRequest());
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveRequestGson = gson.toJson(saveRequest);
        FileUtil.write(FileUtil.generateAddress(Request.class.getName(),saveRequest.requestId),saveRequestGson);
    }
}
