package dev.houshce29.cc.extensions.api.internal;

import dev.houshce29.cc.extensions.AbstractHook;
import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * Abstract adapted hook for accepting list of tokens and symbol tree
 * into the custom logic.
 */
public abstract class SymbolTreeAdaptedHook extends AbstractHook {

    public SymbolTreeAdaptedHook(HookPoint hookPoint) {
        super(hookPoint);
    }

    public SymbolTreeAdaptedHook(HookPoint hookPoint, int sequence) {
        super(hookPoint, sequence);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void execute(Object... objects) {
        if (objects.length > 1 && objects[0] instanceof List
                && objects[1] instanceof SymbolTree) {

            this.execute((List<Token>) objects[0], (SymbolTree) objects[1]);
        }
    }

    /**
     * Executes custom hook logic on the token list and symbol tree.
     * @param tokens Tokens that were used to build the symbol tree.
     * @param symbolTree Symbol tree to do processing on.
     */
    public abstract void execute(List<Token> tokens, SymbolTree symbolTree);
}
