package Models;

import java.util.ArrayList;

public class Request implements Packable {

    private Pendable pendableRequest;
    private ConfirmationStatusStatus status;
    public static ArrayList<Request> allRequests;

    public Pendable getPendableRequest() {
        return pendableRequest;
    }

    public ConfirmationStatusStatus getStatus() {
        return status;
    }

    public void denyRequest(){

    }

    public void acceptRequest() {

    }

}
