package dev.houshce29.cc.generate;

import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * Simple abstraction for code generation, in which
 * nothing is returned for consumption in the current
 * Java runtime upon generation.
 */
public abstract class CodeGenerator implements Generator {

    @Override
    public Object generate(List<Token> tokens, SymbolTree symbolTree) {
        generateCode(tokens, symbolTree);
        return null;
    }

    /**
     * Generates code from the tokens and symbol tree.
     * @param tokens Tokens to aid in code generation.
     * @param symbolTree Symbol tree to generate code from.
     */
    abstract void generateCode(List<Token> tokens, SymbolTree symbolTree);
}
