package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;

public class ForestFactory implements FieldFactory {

    @Override
    public Field createField(int x, int y, MinionWarsModel model, int homeBase, Overlay overlay) {
        return new Forest(x, y, model, homeBase, overlay);
    }
}