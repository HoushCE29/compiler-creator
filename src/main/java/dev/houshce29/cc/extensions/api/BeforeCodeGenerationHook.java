package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.SymbolTreeAdaptedHook;

/**
 * Abstract hook to extend to define custom logic to run
 * before code generation.
 */
public abstract class BeforeCodeGenerationHook extends SymbolTreeAdaptedHook {

    /**
     * Creates a new instance.
     */
    public BeforeCodeGenerationHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public BeforeCodeGenerationHook(int sequence) {
        super(HookPoint.BEFORE_CODE_GENERATION, sequence);
    }
}