package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.SymbolTreeAdaptedHook;

/**
 * Abstract hook to extend to define custom logic to run
 * after the semantic analysis phase.
 */
public abstract class AfterSemanticAnalysisHook extends SymbolTreeAdaptedHook {

    /**
     * Creates a new instance.
     */
    public AfterSemanticAnalysisHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public AfterSemanticAnalysisHook(int sequence) {
        super(HookPoint.AFTER_SEMANTIC_ANALYSIS, sequence);
    }
}
