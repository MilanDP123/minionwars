package be.ugent.objprog.minionwars.Effects;

public class SlowFactory extends AbstractEffectFactory {

    @Override
    public Effect createEffect() {
        return new Slow(duration, name, value);
    }

    @Override
    public Effect createEffect(int value) {
        return new Slow(duration, name, value);
    }
}
