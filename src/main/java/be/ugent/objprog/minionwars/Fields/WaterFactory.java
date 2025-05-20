package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;

public class WaterFactory implements FieldFactory {
    @Override
    public Field createField(int x, int y, MinionWarsModel model, int ignoredHomeBase, Overlay overlay) {
        return new Water(x, y, model, 0, overlay);
    }
}
