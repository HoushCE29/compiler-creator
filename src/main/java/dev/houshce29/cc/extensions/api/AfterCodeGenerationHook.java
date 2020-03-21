package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.SymbolTreeAdaptedHook;

/**
 * Abstract hook to extend to define custom logic to run
 * after code generation.
 */
public abstract class AfterCodeGenerationHook extends SymbolTreeAdaptedHook {

    /**
     * Creates a new instance.
     */
    public AfterCodeGenerationHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public AfterCodeGenerationHook(int sequence) {
        super(HookPoint.AFTER_CODE_GENERATION, sequence);
    }
}
