package Models;

import Repository.SaveSeller;

public class Seller extends User implements Pendable{
    private SaveSeller saveSeller;
//todo bade inke usero zadim!!!

    public Seller(SaveSeller saveSeller){
        super(saveSeller);
        this.saveSeller = saveSeller;
    }
}
