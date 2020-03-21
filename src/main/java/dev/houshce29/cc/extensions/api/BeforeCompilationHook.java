package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.RawInputAdaptedHook;

/**
 * Abstract hook to extend to define custom logic before any compiler components
 * touch the raw input.
 */
public abstract class BeforeCompilationHook extends RawInputAdaptedHook {

    /**
     * Creates a new instance.
     */
    public BeforeCompilationHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public BeforeCompilationHook(int sequence) {
        super(HookPoint.BEFORE_COMPILE, sequence);
    }
}
