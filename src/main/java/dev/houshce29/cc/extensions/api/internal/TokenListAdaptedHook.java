package dev.houshce29.cc.extensions.api.internal;

import dev.houshce29.cc.extensions.AbstractHook;
import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.lex.Token;

import java.util.List;

/**
 * Abstract adapted hook for accepting a list of tokens.
 */
public abstract class TokenListAdaptedHook extends AbstractHook {

    public TokenListAdaptedHook(HookPoint hookPoint) {
        super(hookPoint);
    }

    public TokenListAdaptedHook(HookPoint hookPoint, int sequence) {
        super(hookPoint, sequence);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void execute(Object... objects) {
        if (objects.length > 0 && objects[0] instanceof List) {
            this.execute((List<Token>) objects[0]);
        }
    }

    /**
     * Executes custom hook logic on the list of tokens.
     * @param tokens Tokens to process with custom logic.
     */
    public abstract void execute(List<Token> tokens);
}
