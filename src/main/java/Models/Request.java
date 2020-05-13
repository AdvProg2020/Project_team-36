package Models;

import java.util.ArrayList;

import static Models.ConfirmationStatus.*;

public class Request implements Packable {
    private Pendable pendableRequest;
    private int requestId;
    private static int totalRequestsMade;
    private ConfirmationStatus status;
    private static ArrayList<Request> allRequests = new ArrayList<>();

    public Request(Pendable pendable){
        this.requestId = getRandomId();
        this.pendableRequest = pendable;
        this.status = TO_BE_CONFIRMED;
    }

    private int getRandomId(){
        totalRequestsMade+=1;
        return totalRequestsMade;
    }

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

    public static void denyRequest(int id){
        Request toBeDeclined = getRequestWithId(id);
        allRequests.remove(toBeDeclined);
        Pendable pendable = toBeDeclined.getPendableRequest();
        pendable = null;
        toBeDeclined = null;
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
