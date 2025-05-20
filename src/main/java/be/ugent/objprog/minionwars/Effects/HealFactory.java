package be.ugent.objprog.minionwars.Effects;

public class HealFactory extends AbstractEffectFactory {

    @Override
    public Effect createEffect() {
        return new Heal(duration, name, value);
    }

    @Override
    public Effect createEffect(int value) {
        return new Heal(duration, name, value);
    }
}
