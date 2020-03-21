package dev.houshce29.cc.analyze;

import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * Default semantic analyzer. This analyzer does nothing.
 */
public final class DefaultSemanticAnalyzer implements SemanticAnalyzer {

    @Override
    public void analyze(List<Token> tokens, SymbolTree symbolTree) {
        // Do nothing.
    }
}
