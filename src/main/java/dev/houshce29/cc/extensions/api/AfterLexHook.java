package dev.houshce29.cc.extensions.api;

import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.extensions.api.internal.TokenListAdaptedHook;

/**
 * Abstract hook to extend to define custom logic to
 * run on the tokens after the lex phase.
 */
public abstract class AfterLexHook extends TokenListAdaptedHook {

    /**
     * Creates a new instance.
     */
    public AfterLexHook() {
        this(0);
    }

    /**
     * Creates a new sequenced instance.
     * @param sequence Sequence of this hook.
     */
    public AfterLexHook(int sequence) {
        super(HookPoint.AFTER_LEX, sequence);
    }
}
