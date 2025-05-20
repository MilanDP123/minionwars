package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;
import javafx.scene.image.Image;

import java.util.Objects;

public class Mountains extends Field {

    public Mountains(int x, int y, MinionWarsModel model, int homeBase, Overlay overlay) {
        super(x, y, model, homeBase, overlay);
        imageName = "tiles/mountains.png";
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagesPath + imageName)));
    }

    @Override
    public String getType() {
        return "mountains";
    }

    @Override
    public boolean canAttack() {
        return false;
    }

}
