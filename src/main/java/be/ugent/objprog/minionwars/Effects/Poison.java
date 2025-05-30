package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public class Poison extends Effect {
    public Poison(int duration, String name, int value) {
        super(duration,  name, value);
    }

    @Override
    public void handleEffect(Minion minion) {
        minion.setDefence(minion.getDefence() - value);
        super.handleEffect(minion);
    }

    @Override
    public Effect clone() {
        return new Poison(duration, name, value);
    }
}
