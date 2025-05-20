package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public class Blindness extends Effect {
    public Blindness(int duration, String name, int value) {
        super(duration, name, value);
    }

    @Override
    public void handleEffect(Minion minion) {
        minion.setMaxRange(minion.getMaxRange() - value);
        super.handleEffect(minion);
    }

    @Override
    public Effect clone() {
        return new Blindness(duration, name, value);
    }
}
