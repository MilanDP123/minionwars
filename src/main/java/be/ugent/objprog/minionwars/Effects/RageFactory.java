package be.ugent.objprog.minionwars.Effects;

public class RageFactory extends AbstractEffectFactory {

    @Override
    public Effect createEffect() {
        return new Rage(duration, name, value);
    }

    @Override
    public Effect createEffect(int value) {
        return new Rage(duration, name, value);
    }
}
