package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;
import javafx.scene.image.Image;

import java.util.Objects;

public class Water extends Field {

    public Water(int x, int y, MinionWarsModel model, int homeBase, Overlay overlay) {
        super(x, y, model, homeBase, overlay);
        imageName = "tiles/water.png";
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagesPath + imageName)));
    }

    public String getType() {
        return "water";
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

}

