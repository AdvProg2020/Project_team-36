package Client.Models;

import Models.Status;
import Repository.SaveRequest;


public class Request {
    private SaveRequest saveRequest;
    private Client.Models.Pendable pendableRequest;
    private int requestId;
    private Status status;

    public Request(SaveRequest saveRequest){
        this.saveRequest = saveRequest;
        this.requestId = saveRequest.getRequestId();
        this.status = saveRequest.getStatus();
        if(saveRequest.getSaveComment()!= null){
            this.pendableRequest = new Comment(saveRequest.getSaveComment());
        }else if(saveRequest.getSaveProduct()!=null){
            this.pendableRequest = new Product(saveRequest.getSaveProduct());
        }else if(saveRequest.getSaveProductField()!=null){
            this.pendableRequest = new ProductField(saveRequest.getSaveProductField());
        }else if(saveRequest.getSaveSale()!=null){
            this.pendableRequest = new Sale(saveRequest.getSaveSale());
        }else if(saveRequest.getSaveSeller()!=null){
            this.pendableRequest = new Seller(saveRequest.getSaveSeller());
        }
    }

    public int getRequestId() {
        return requestId;
    }

    public Pendable getPendableRequest() {
        return pendableRequest;
    }

    public Status getStatus() {
        return status;
    }

    public String getType(){
        return pendableRequest.getPendingRequestType();
    }
}
