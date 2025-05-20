package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.Fields.Field;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MinionViewer extends VBox {

    public MinionViewer(MinionWarsModel model, SimpleObjectProperty<Field> field) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("minionViewer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(k -> new MinionViewerController(model, field));
        fxmlLoader.load();
    }

}
