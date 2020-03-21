package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.TokenListAdaptedHook;

/**
 * Abstract hook to extend to define custom logic
 * to run before parse phase.
 */
public abstract class BeforeParseHook extends TokenListAdaptedHook {

    /**
     * Creates a new instance.
     */
    public BeforeParseHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public BeforeParseHook(int sequence) {
        super(HookPoint.BEFORE_PARSE, sequence);
    }
}
