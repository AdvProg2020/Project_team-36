package Models;

import java.util.ArrayList;

public class Request implements Packable {
    private Pendable pendableRequest;
    private int requestId;
    private ConfirmationStatus status;
    private static ArrayList<Request> allRequests = new ArrayList<>();

    public Pendable getPendableRequest() {
        return pendableRequest;
    }

    public int getRequestId() {
        return requestId;
    }

    public ConfirmationStatus getStatus() {
        return status;
    }

    public static ArrayList<Request> getAllRequests() {
        return allRequests;
    }

    public static Request getRequestWithId(int id){
        for (Request request : allRequests) {
            if(request.getRequestId()==id){
                return request;
            }
        }
        return null;
    }

    public static boolean isThereRequestWithId(int id){
        for (Request request : allRequests) {
            if(request.getRequestId()==id){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "  requestId: " + requestId +
                "\n  this is a request for a new" + pendableRequest.getPendingRequestType() +
                '\n' + pendableRequest;
    }

    public void denyRequest(){

    }

    public void acceptRequest() {

    }

    public Data pack(Object object) {
        return null;
    }

    public Object unpack(Data data) {
        return null;
    }
}
