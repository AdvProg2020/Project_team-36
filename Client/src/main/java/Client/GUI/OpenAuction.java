package Client.GUI;

import Client.Controllers.AuctionController;
import Client.Models.Auction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class OpenAuction extends CustomerTemplateController implements Initializable {
    public TextField priceInput;
    public TextField currentOffer;
    public Label productName;
    public Label alertLabel;
    private Auction auction;
    private boolean inPage = true;

    @Override
    public void initialize(int id) throws IOException {
        try {
            this.auction = Constants.auctionController.getAuction(id);
        } catch (AuctionController.NoAuctionWithId noAuctionWithId) {
            AlertBox.display("Error", "Auction has ended!");
            inPage = false;
            Constants.getGuiManager().open("ShowAllAuctions", 1);
        }
        productName.setText(auction.getProductName());
        addNumberListener();
        currentOffer.setText("0");
        update();
    }

    @Override
    public void back() throws IOException {
        inPage=false;
        Constants.getGuiManager().back();
    }

    private void addNumberListener() {
        priceInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("^\\D+$"))
                priceInput.setText(priceInput.getText().replaceAll("\\D", ""));
        });
    }

    public void addPrice(ActionEvent actionEvent) throws IOException {
        int amount = Integer.parseInt(priceInput.getText());
        if (Integer.parseInt(priceInput.getText()) <= Integer.parseInt(currentOffer.getText())) {
            alertLabel.setVisible(true);
            return;
        }
        try {
            Constants.auctionController.addOffer(amount, auction.getId());
            currentOffer.setText(Integer.toString(amount));
        } catch (AuctionController.NotEnoughMoneyInWallet notEnoughMoneyInWallet) {
            AlertBox.display("Error", "You don't have enough money in your wallet!");
        } catch (AuctionController.NoAuctionWithId noAuctionWithId) {
            AlertBox.display("Error", "Auction has ended!");
            inPage = false;
            Constants.getGuiManager().open("ShowAllAuctions", 1);
        }
    }

    private void update() {
        new Thread(() -> {
            while (inPage) {
                try {
                    auction = Constants.auctionController.getAuction(auction.getId());
                } catch (AuctionController.NoAuctionWithId noAuctionWithId) {
                    AlertBox.display("Error", "Auction has ended!");
                    inPage = false;
                    try {
                        Constants.getGuiManager().open("ShowAllAuctions", 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}