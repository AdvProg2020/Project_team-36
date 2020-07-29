package Client.GUI;

import Client.Controllers.AuctionController;
import Client.Controllers.ChatsController;
import Client.Controllers.EntryController;
import Client.Models.Auction;
import Client.Models.Chat;
import Models.Message;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenAuction extends CustomerTemplateController implements Initializable {
    public TextField priceInput;
    public TextField currentOffer;
    public Label productName;
    public Label alertLabel;
    public TextArea chatsArea;
    public TextArea messageArea;
    private Auction auction;
    private int chatId;
    private final ChatsController chatsController = new ChatsController();
    private Thread updateThread;

    @Override
    public void initialize(int id) throws IOException {
        try {
            this.auction = Constants.auctionController.getAuction(id);
            chatId = auction.getChat().getId();
        } catch (AuctionController.NoAuctionWithId noAuctionWithId) {
            AlertBox.display("Error", "Auction has ended!");
            intteruptThread();
            Constants.getGuiManager().open("ShowAllAuctions", 1);
        }
        productName.setText(auction.getProductName());
        addNumberListener();
        currentOffer.setText("0");
        updateThread = update();
        System.out.println(auction.getFinalBuyer().toString());
    }

    @Override
    public void back() throws IOException {
        intteruptThread();
        Constants.getGuiManager().back();
    }

    @Override
    public void logout() throws EntryController.NotLoggedInException, IOException {
        intteruptThread();
        super.logout();
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
            intteruptThread();
            Constants.getGuiManager().open("ShowAllAuctions", 1);
        }
    }

    private Thread update() {
        Thread thread  = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                try {
                    auction = Constants.auctionController.getAuction(auction.getId());
                    setChatsArea();
                } catch (AuctionController.NoAuctionWithId noAuctionWithId) {
                    AlertBox.display("Error", "Auction has ended!");
                    intteruptThread();
                    try {
                        Constants.getGuiManager().open("ShowAllAuctions", 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return thread;
    }

    private void setChatsArea() {
        Chat chat = chatsController.getChatById(chatId);
        StringBuilder allMessages = new StringBuilder();
        for (Message message : chat.getMessagesInChat()) {
            Date date = new Date(message.getTime());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm");
            String strDate = formatter.format(date);
            String madeMessage = (message.getSenderUsername() + ":  " + message.getText() + strDate + "\n\n");
            allMessages.append(madeMessage);
        }
        chatsArea.setText(allMessages.toString());
    }

    public void sendAction() throws IOException {
        if (messageArea.getText().isEmpty()) {
            return;
        }
        String text = messageArea.getText();
        chatsController.sendNewMessage(text, chatId, Constants.globalVariables.getLoggedInUser().getUsername());
        messageArea.clear();
        Constants.getGuiManager().reopen();
    }

    @Override
    public void openAllAuctions() throws IOException {
        intteruptThread();
        super.openAllAuctions();
    }

    @Override
    public void openWallet() throws IOException {
        intteruptThread();
        super.openWallet();
    }

    @Override
    public void getSupport() throws IOException {
        intteruptThread();
        super.getSupport();
    }

    @Override
    public void goToCart() throws IOException {
        intteruptThread();
        super.goToCart();
    }

    @Override
    public void showPersonalInfo() throws IOException {
        intteruptThread();
        super.showPersonalInfo();
    }

    @Override
    public void viewOrders() throws IOException {
        intteruptThread();
        super.viewOrders();
    }

    @Override
    public void viewDiscounts() throws IOException {
        intteruptThread();
        super.viewDiscounts();
    }

    private void intteruptThread(){
        updateThread.interrupt();
    }
}