package be.ugent.objprog.minionwars.Effects;

public class PoisonFactory extends AbstractEffectFactory {

    @Override
    public Effect createEffect() {
        return new Poison(duration, name, value);
    }

    @Override
    public Effect createEffect(int value) {
        return new Poison(duration, name, value);
    }
}
