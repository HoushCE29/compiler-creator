package dev.houshce29.cc.generate;

import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * Default generator that does nothing and returns `null`.
 */
public final class DefaultGenerator implements Generator {

    @Override
    public Object generate(List<Token> tokens, SymbolTree symbolTree) {
        return null;
    }
}
