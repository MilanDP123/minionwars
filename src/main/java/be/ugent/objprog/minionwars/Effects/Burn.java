package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public class Burn extends Effect {
    public Burn(int duration, String name, int value) {
        super(duration, name, value);
    }

    @Override
    public void handleEffect(Minion minion) {
        minion.setDefence(minion.getDefence() - value);
        super.handleEffect(minion);
    }

    @Override
    public Effect clone() {
        return new Burn(duration, name, value);
    }
}