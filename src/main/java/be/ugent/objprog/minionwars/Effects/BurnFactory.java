package be.ugent.objprog.minionwars.Effects;

public class BurnFactory extends AbstractEffectFactory {

    @Override
    public Effect createEffect() {
        return new Burn(duration, name, value);
    }

    @Override
    public Effect createEffect(int value) {
        return new Burn(duration, name, value);
    }
}
