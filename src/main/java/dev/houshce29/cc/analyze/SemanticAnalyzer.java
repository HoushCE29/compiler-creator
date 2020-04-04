package dev.houshce29.cc.analyze;

import dev.houshce29.cc.common.CompilerComponent;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * Analyzes the semantics of code.
 */
public interface SemanticAnalyzer extends CompilerComponent {

    /**
     * Analyzes the compiled artifacts before proceeding to
     * the final stages of the compilation process. This ensures
     * that semantic properties are in order such that the resulting
     * runnable entity from this compiler functions correctly. Primarily,
     * this scans the symbol tree to ensure scope and entry points are in
     * order. For example, while it's likely syntactically correct to
     * have some variable referenced in code, this should make sure that
     * said variable can be accessed in the current scope. Another example
     * would be ensuring that a "main" function exists to know where to start
     * the resulting program in execution.
     * @param tokens Artifact from the lex phase. May not be necessarily useful here.
     * @param symbolTree Symbol tree to heavily analyze.
     */
    void analyze(List<Token> tokens, SymbolTree symbolTree);
}
