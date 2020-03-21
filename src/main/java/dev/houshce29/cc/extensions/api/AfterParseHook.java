package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.SymbolTreeAdaptedHook;

/**
 * Abstract hook to extend to define custom logic to run
 * after the parse phase.
 */
public abstract class AfterParseHook extends SymbolTreeAdaptedHook {

    /**
     * Creates a new instance.
     */
    public AfterParseHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public AfterParseHook(int sequence) {
        super(HookPoint.AFTER_PARSE, sequence);
    }
}
