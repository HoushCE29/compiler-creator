package dev.houshce29.cc.generate;

import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * The final part of the compiler that generates code.
 */
public interface CodeGenerator {

    /**
     * Generates code from the tokens and/or symbol tree.
     * @param tokens List of tokens from the lex phase.
     * @param symbolTree Symbol tree from the parse phase.
     */
    void generate(List<Token> tokens, SymbolTree symbolTree);
}
