package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.SymbolTreeAdaptedHook;

/**
 * Abstract hook to extends to define custom logic to run
 * before the semantic analysis phase.
 */
public abstract class BeforeSemanticAnalysisHook extends SymbolTreeAdaptedHook {

    /**
     * Creates a new instance.
     */
    public BeforeSemanticAnalysisHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public BeforeSemanticAnalysisHook(int sequence) {
        super(HookPoint.BEFORE_SEMANTIC_ANALYSIS, sequence);
    }
}
