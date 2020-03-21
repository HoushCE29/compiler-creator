package dev.houshce29.cc.extensions.api.internal;

import dev.houshce29.cc.extensions.AbstractHook;
import dev.houshce29.cc.extensions.HookPoint;

/**
 * Abstract adapted hook for exception handling/processing.
 */
public abstract class ThrowableAdaptedHook extends AbstractHook {

    public ThrowableAdaptedHook(int sequence) {
        super(HookPoint.ON_FAILURE, sequence);
    }

    @Override
    public final void execute(Object... objects) {
        if (objects.length > 0 && objects[0] instanceof Throwable) {
            this.execute((Throwable) objects[0]);
        }
    }

    /**
     * Executes custom hook logic for the given throwable.
     * @param thrown Throwable that was thrown.
     */
    public abstract void execute(Throwable thrown);
}
