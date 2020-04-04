package dev.houshce29.cc.generate;

import dev.houshce29.cc.common.CompilerComponent;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * The final part of the compiler that generates something.
 */
public interface Generator extends CompilerComponent {

    /**
     * Generates a final resulting entity as a result of
     * successful compilation. Typically, if this is a
     * code generator, then `null` will be returned (since
     * the result will be some generated code in a file
     * somewhere). Otherwise, this should be populated.
     * @param tokens Tokens possibly used in generation.
     * @param symbolTree Symbol tree used in generation.
     * @return Result, which can be `null`.
     */
    Object generate(List<Token> tokens, SymbolTree symbolTree);
}
