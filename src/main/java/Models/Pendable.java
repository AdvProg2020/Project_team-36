package Models;

public interface Pendable {
    String getPendingRequestType();
    String toString();
    void acceptAddRequest();
    void acceptEditRequest();
    //-..-
}
