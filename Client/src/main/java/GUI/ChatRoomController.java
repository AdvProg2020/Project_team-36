package GUI;

import Controllers.ChatsController;
import Models.Chat;
import Models.Message;
import Models.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ChatRoomController implements Initializable {
    @FXML private VBox chatsVBox;
    @FXML private VBox usersVBox;
    @FXML private TextArea chatsArea;
    @FXML private TextArea messageArea;
    @FXML private Button sendButton;
    @FXML private ImageView writerProfilePicture;
    @FXML private TextArea writerUsername;
    private User writer;
    private Chat chat;
    private final ChatsController chatsController = new ChatsController();


    @Override
    public void initialize(int id) throws IOException {
        this.writer = Constants.globalVariables.getLoggedInUser();
        writerUsername.setText(writer.getUsername()+"\n"+writer.getType());
        writerProfilePicture.setImage(writer.getProfilePicture(50, 50).getImage());
        chat = chatsController.getChatById(id);
        setUsersVBox();
        setChatsVBox();
        setChatsArea();
        Timer t = new Timer( );
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Constants.getGuiManager().reopen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0,1000);
    }

    private void setUsersVBox() throws IOException {
        for (User user : chat.getUsersInChat()) {
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPadding(new Insets(0,5,0,5));

            ImageView profilePicture = new ImageView();
            profilePicture.setImage(user.getProfilePicture(50, 50).getImage());
            Label username = new Label(user.getUsername());

            hBox.getChildren().addAll(profilePicture, username);
            usersVBox.getChildren().add(hBox);
        }
    }

    private void setChatsVBox(){
        for (Chat newChat : writer.getChats()) {
            Hyperlink chatHyperLink = new Hyperlink("chat: "+newChat.getId());
            chatHyperLink.setOnAction(e -> {
                try {
                    Constants.getGuiManager().open("ChatRoom",newChat.getId());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            chatsVBox.getChildren().add(chatHyperLink);
        }
    }

    private void setChatsArea(){
        StringBuilder allMessages = new StringBuilder();
        for (Message message : chat.getMessagesInChat()) {
            Date date = new Date(message.getTime());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm");
            String strDate = formatter.format(date);
            String madeMessage = (message.getSenderUsername()+":  "+message.getText()+strDate+"\n\n");
            allMessages.append(madeMessage);
        }
        chatsArea.setText(allMessages.toString());
    }

    public void sendAction() throws IOException {
        String text = messageArea.getText();
        chatsController.sendNewMessage(text, chat.getId(), writer.getUsername());
        messageArea.clear();
        Constants.getGuiManager().reopen();
    }
}

