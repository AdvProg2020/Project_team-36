package GUI;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    private List<Parent> openedParents;
    private Stage stage;

    public GUIManager() {
        this.openedParents = new ArrayList<>();
    }

    public void open(Parent parent){
        openedParents.add(parent);
        stage.getScene().setRoot(parent);
    }

    public void back(){
        if (openedParents.size() > 1){
            openedParents.remove(openedParents.size() - 1);
            open(openedParents.get(openedParents.size() - 1));
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addToOpenedParents(Parent parent){
        openedParents.add(parent);
    }
}
