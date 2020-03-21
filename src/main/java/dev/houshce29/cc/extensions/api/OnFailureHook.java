package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.api.internal.ThrowableAdaptedHook;

/**
 * Abstract hook to extend to define custom logic for thrown
 * exceptions/errors during compilation.
 */
public abstract class OnFailureHook extends ThrowableAdaptedHook {

    /**
     * Creates a new instance.
     */
    public OnFailureHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public OnFailureHook(int sequence) {
        super(sequence);
    }
}
