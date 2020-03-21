package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.RawInputAdaptedHook;

/**
 * Abstract hook to extend to define custom logic to
 * run before lexing.
 */
public abstract class BeforeLexHook extends RawInputAdaptedHook {

    /**
     * Creates a new instance.
     */
    public BeforeLexHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public BeforeLexHook(int sequence) {
        super(HookPoint.BEFORE_LEX, sequence);
    }
}
