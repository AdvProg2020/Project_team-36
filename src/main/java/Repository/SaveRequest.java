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

    public SaveRequest(Request request){
        this.requestId = request.getRequestId();
        this.status = request.getStatus();

        if (request.getPendableRequest() instanceof Product) {
            this.saveProduct = new SaveProduct((Product) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof ProductField) {
            this.saveProductField = new SaveProductField((ProductField) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof Comment) {
            this.saveComment = new SaveComment((Comment) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof Seller) {
            this.saveSeller = new SaveSeller((Seller) request.getPendableRequest());
        } else if (request.getPendableRequest() instanceof Sale) {
            this.saveSale = new SaveSale((Sale) request.getPendableRequest());
        }
    }

    public static void save(Request request) {
        SaveRequest saveRequest = new SaveRequest(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String saveRequestGson = gson.toJson(saveRequest);
        FileUtil.write(FileUtil.generateAddress(Request.class.getName(), saveRequest.requestId), saveRequestGson);
    }

    public static Request load(int id) {
        lastId = Math.max(lastId, id);
        Request potentialRequest = Request.getRequestById(id);
        if (potentialRequest != null) {
            return potentialRequest;
        }

        Gson gson = new Gson();
        String data = FileUtil.read(FileUtil.generateAddress(Request.class.getName(), id));
        if (data == null) {
            return null;
        }
        SaveRequest saveRequest = gson.fromJson(data, SaveRequest.class);

        Pendable pendable = null;
        if (saveRequest.saveSale != null) {
            pendable = saveRequest.saveSale.generateSale();
        } else if (saveRequest.saveComment != null) {
            pendable = saveRequest.saveComment.generateComment();
        } else if (saveRequest.saveProductField != null) {
            pendable = saveRequest.saveProductField.generateProductField();
        } else if (saveRequest.saveProduct != null) {
            pendable = saveRequest.saveProduct.generateProduct();
        } else if (saveRequest.saveSeller != null) {
            pendable = saveRequest.saveSeller.generateSeller();
        }

        Request request = new Request(pendable,id,saveRequest.status);
        Request.addToAllRequests(request);
        return request;
    }

    public int getRequestId() {
        return requestId;
    }

    public Status getStatus() {
        return status;
    }

    public SaveProduct getSaveProduct() {
        return saveProduct;
    }

    public SaveProductField getSaveProductField() {
        return saveProductField;
    }

    public SaveComment getSaveComment() {
        return saveComment;
    }

    public SaveSeller getSaveSeller() {
        return saveSeller;
    }

    public SaveSale getSaveSale() {
        return saveSale;
    }

    public static int getLastId() {
        return lastId;
    }
}
