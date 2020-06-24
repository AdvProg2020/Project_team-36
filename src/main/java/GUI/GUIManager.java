package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    private List<String> openedParents;
    private List<Integer> openedParentsIds;
    private Stage stage;

    public GUIManager() {
        this.openedParents = new ArrayList<>();
        this.openedParentsIds = new ArrayList<>();
    }

    public void open(String name, int id) throws IOException {
        openedParents.add(name);
        openedParentsIds.add(id);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + name + ".fxml"));
        Parent parent = fxmlLoader.load();
        ((Initializable) fxmlLoader.getController()).initialize(id);
        stage.getScene().setRoot(parent);
    }

    public void back() throws IOException {
        if (openedParents.size() > 1) {
            openedParents.remove(openedParents.size() - 1);
            openedParentsIds.remove(openedParentsIds.size() - 1);
            open(openedParents.remove(openedParents.size() - 1),
                    openedParentsIds.remove(openedParentsIds.size() - 1));
        }
    }

    public void login(){}

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
}
