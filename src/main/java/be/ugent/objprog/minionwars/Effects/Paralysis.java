package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public class Paralysis extends Effect {
    public Paralysis(int duration, String name, int value) {
        super(duration, name, value);
    }

    @Override
    public void handleEffect(Minion minion) {
        minion.setMoved(true);
        minion.setAttacked(true);
        super.handleEffect(minion);
    }

    @Override
    public Effect clone() {
        return new Paralysis(duration, name, value);
    }

}
