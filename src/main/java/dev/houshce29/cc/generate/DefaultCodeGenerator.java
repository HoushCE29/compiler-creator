package dev.houshce29.cc.generate;

import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Default code generator that simply prints to the console.
 */
public final class DefaultCodeGenerator implements CodeGenerator {

    @Override
    public void generate(List<Token> tokens, SymbolTree symbolTree) {
        System.out.println(tokens.stream()
                .map(t -> t.getId() + ":" + t.getValue())
                .collect(Collectors.toList()));
        System.out.println();
        System.out.println(symbolTree);
    }
}
