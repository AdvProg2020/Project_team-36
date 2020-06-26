package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    private List<String> openedParents;
    private List<Integer> openedParentsIds;
    private Stage stage;
    private Stage loginStage;

    public GUIManager() {
        this.openedParents = new ArrayList<>();
        this.openedParentsIds = new ArrayList<>();
    }

    public void open(String name, int id) throws IOException {
        openedParents.add(name);
        openedParentsIds.add(id);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + name + ".fxml"));
        Parent parent = fxmlLoader.load();
        stage.getScene().setRoot(parent);
        ((Initializable) fxmlLoader.getController()).initialize(id);
    }

    public void back() throws IOException {
        if (openedParents.size() > 1) {
            openedParents.remove(openedParents.size() - 1);
            openedParentsIds.remove(openedParentsIds.size() - 1);
            open(openedParents.remove(openedParents.size() - 1),
                    openedParentsIds.remove(openedParentsIds.size() - 1));
        }
    }

    public void login(){
         LoginBox loginBox = new LoginBox();
         this.loginStage = loginBox.getWindow();
         loginBox.display();
    }

    public void successfulLogin(){
        loginStage.close();
        try {
            reopen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void reopen() throws IOException {
        open(openedParents.remove(openedParents.size() - 1),
                openedParentsIds.remove(openedParentsIds.size() - 1));
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public void openRegister(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RegisterMenu.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginStage.getScene().setRoot(parent);

    }

    public void openLogin(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginMenu.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginStage.getScene().setRoot(parent);
    }

    public void openCompareStage(int id) throws IOException {
        Stage compareStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CompareBox.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        compareStage.setScene(new Scene(parent));
        compareStage.getScene().setRoot(parent);
        ((Initializable) fxmlLoader.getController()).initialize(id);
        compareStage.showAndWait();
    }
}
