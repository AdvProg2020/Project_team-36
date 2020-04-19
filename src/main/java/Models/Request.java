package Models;

import java.util.ArrayList;

public class Request implements Packable {

    private Pendable pendableRequest;
    private ConfirmationStatus status;
    private static ArrayList<Request> allRequests;

    public Pendable getPendableRequest() {
        return pendableRequest;
    }

    public ConfirmationStatus getStatus() {
        return status;
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
