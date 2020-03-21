package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.SymbolTreeAdaptedHook;

/**
 * Abstract hook to extend to define custom logic to run
 * after compilation.
 */
public abstract class AfterCompilationHook extends SymbolTreeAdaptedHook {

    /**
     * Creates a new instance.
     */
    public AfterCompilationHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public AfterCompilationHook(int sequence) {
        super(HookPoint.AFTER_COMPILE, sequence);
    }
}
