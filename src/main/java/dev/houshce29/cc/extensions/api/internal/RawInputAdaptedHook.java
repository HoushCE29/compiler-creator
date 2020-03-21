package dev.houshce29.cc.extensions.api.internal;

import dev.houshce29.cc.extensions.AbstractHook;
import dev.houshce29.cc.extensions.HookPoint;

/**
 * Abstract hook that eases defining a hook that expects
 * raw input as the input to the logic.
 */
public abstract class RawInputAdaptedHook extends AbstractHook {

    public RawInputAdaptedHook(HookPoint hookPoint) {
        super(hookPoint);
    }

    public RawInputAdaptedHook(HookPoint hookPoint, int sequence) {
        super(hookPoint, sequence);
    }

    @Override
    public final void execute(Object... objects) {
        if (objects.length > 0 && objects[0] instanceof String) {
            this.execute((String) objects[0]);
        }
    }

    /**
     * Executes custom hook logic on the raw input.
     * @param rawInput Raw input to run custom logic for.
     */
    public abstract void execute(String rawInput);
}
