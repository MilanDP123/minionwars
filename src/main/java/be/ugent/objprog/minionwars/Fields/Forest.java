package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;
import javafx.scene.image.Image;

import java.util.Objects;

public class Forest extends Field{

    public Forest(int x, int y, MinionWarsModel model, int homeBase, Overlay overlay) {
        super(x, y, model, homeBase, overlay);
        imageName = "tiles/forest.png";
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagesPath + imageName)));
    }

    @Override
    public void colorReachableNeighbours(int range) {
        range -= 1;
        super.colorReachableNeighbours(range);
    }

    @Override
    public String getType() {
        return "forest";
    }

}

