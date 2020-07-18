package Models;

import Repository.SaveSeller;

public class Seller implements Pendable{
    private SaveSeller saveSeller;
//todo bade inke usero zadim!!!

    public Seller(SaveSeller saveSeller){
        this.saveSeller = saveSeller;
    }

    @Override
    public String getPendingRequestType() {
        return "seller";
    }

    @Override
    public void acceptAddRequest() {}

    @Override
    public void acceptEditRequest() {}
}
