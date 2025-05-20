package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;

public interface FieldFactory {
    Field createField(int x, int y, MinionWarsModel model, int homeBase, Overlay overlay);
}
