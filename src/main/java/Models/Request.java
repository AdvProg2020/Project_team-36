package Models;


import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static Models.Status.*;

public class Request {
    private Pendable pendableRequest;
    private int requestId;
    static Random random = new Random();
    private static int totalRequestsMade = random.nextInt(4988 - 1000) + 1000;
    private Status status;
    private Date date;
    private static ArrayList<Request> allRequests = new ArrayList<>();

    public Request(Pendable pendable, Status status){
        this.requestId = getRandomId();
        this.pendableRequest = pendable;
        this.status = status;
        allRequests.add(this);
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

    public Status getStatus() {
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
        if(status.equals(TO_BE_ADDED)||status.equals(TO_BE_CONFIRMED)){
            return "  requestId: " + requestId +
                    "\n  this is a request for a new " + pendableRequest.getPendingRequestType() + " to be added." +
                    '\n' + pendableRequest;
        }else{
            return "  requestId: " + requestId +
                    "\n  this is a request to edit a " + pendableRequest.getPendingRequestType() +
                    '\n' + pendableRequest;
        }
    }

    public static void denyRequest(int id){
        Request toBeDeclined = getRequestWithId(id);
        allRequests.remove(toBeDeclined);
        Pendable pendable = toBeDeclined.getPendableRequest();
        if(pendable.getPendingRequestType().equals("seller account")){
            User.removeUsername(((Seller)pendable).getUsername());
        }
        pendable = null;
        toBeDeclined = null;
    }

    public void acceptRequest() {
       if(status.equals(TO_BE_EDITED)){
            pendableRequest.acceptEditRequest();
        } else if (status.equals(TO_BE_ADDED)){
            pendableRequest.acceptAddRequest();
        }
       allRequests.remove(this);
    }

    public static Request getRequestById(int id){
        for (Request request : allRequests) {
            if (request.requestId == id){
                return request;
            }
        }
        return null;
    }

    public Request(Pendable pendableRequest, int requestId, Status status) {
        this.pendableRequest = pendableRequest;
        this.requestId = requestId;
        this.status = status;
    }

    public static void addToAllRequests(Request request){
        allRequests.add(request);
    }


    public String getRequestType(){
        String type;
        if(status.equals(TO_BE_ADDED)) {
            type = "add a new " + pendableRequest.getPendingRequestType() ;
        } else {
            type = "edit a " + pendableRequest.getPendingRequestType();
        }
        return type;
    }


    public String getType(){
        return this.pendableRequest.getPendingRequestType();
    }

}
