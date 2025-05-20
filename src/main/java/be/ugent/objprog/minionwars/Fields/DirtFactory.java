package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;

public class DirtFactory implements FieldFactory {

    @Override
    public Field createField(int x, int y, MinionWarsModel model, int homeBase, Overlay overlay) {
        return new Dirt(x, y, model, homeBase, overlay);
    }
}
