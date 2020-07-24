package Client.GUI;

import Client.Controllers.AuctionController;
import Client.Models.Auction;

import java.io.IOException;

public class OpenAuction extends CustomerTemplateController implements Initializable{
    private Auction auction;

    @Override
    public void initialize(int id) throws IOException {
        try {
            this.auction =  Constants.auctionController.getAuction(id);
        } catch (AuctionController.NoAuctionWithId noAuctionWithId) {
            AlertBox.display("Error","Auction has ended!");
            Constants.getGuiManager().open("ShowAllAuctions",1);
        }

    }
}
