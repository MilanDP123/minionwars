package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public class Slow extends Effect {
    public Slow(int duration, String name, int value) {
        super(duration, name, value);
    }

    @Override
    public void handleEffect(Minion minion) {
        minion.setMovement(minion.getMovement() - value);
        super.handleEffect(minion);
    }

    @Override
    public Effect clone() {
        return new Slow(duration, name, value);
    }
}
