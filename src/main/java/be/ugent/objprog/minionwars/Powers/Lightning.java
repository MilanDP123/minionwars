package be.ugent.objprog.minionwars.Powers;

import be.ugent.objprog.minionwars.Minions.Minion;
import be.ugent.objprog.minionwars.Status;

public class Lightning extends Power {
    @Override
    public Status getStatus() {
        return Status.POWER_LIGHTNING;
    }

    @Override
    public String getType() {
        return "lightning";
    }

    @Override
    public void handle(Minion target) {
        target.setDefence(target.getDefence() - value);
        super.handle(target);
    }
}
