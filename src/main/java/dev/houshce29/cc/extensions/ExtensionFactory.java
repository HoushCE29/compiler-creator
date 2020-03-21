package dev.houshce29.cc.extensions;

import dev.houshce29.cc.extensions.api.AfterCodeGenerationHook;
import dev.houshce29.cc.extensions.api.AfterCompilationHook;
import dev.houshce29.cc.extensions.api.AfterLexHook;
import dev.houshce29.cc.extensions.api.AfterParseHook;
import dev.houshce29.cc.extensions.api.AfterSemanticAnalysisHook;
import dev.houshce29.cc.extensions.api.BeforeCodeGenerationHook;
import dev.houshce29.cc.extensions.api.BeforeCompilationHook;
import dev.houshce29.cc.extensions.api.BeforeLexHook;
import dev.houshce29.cc.extensions.api.BeforeParseHook;
import dev.houshce29.cc.extensions.api.BeforeSemanticAnalysisHook;
import dev.houshce29.cc.extensions.api.OnFailureHook;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Factory for simple, inline hooks.
 */
public final class ExtensionFactory {
    private ExtensionFactory() {
    }

    /**
     * Returns a new failure hook.
     * @param function Consumer that processes some thrown throwable.
     * @return New failure hook.
     */
    public static OnFailureHook onFailure(Consumer<Throwable> function) {
        return new InternalOnFailureHook(function);
    }

    /**
     * Returns a new before compilation hook.
     * @param function Consumer that processes the raw input to the compiler.
     * @return New before compilation hook.
     */
    public static BeforeCompilationHook beforeCompilation(Consumer<String> function) {
        return new InternalBeforeCompilationHook(function);
    }

    /**
     * Returns a new before lex hook.
     * @param function Consumer that processes the raw input to the compiler.
     * @return New before lex hook.
     */
    public static BeforeLexHook beforeLex(Consumer<String> function) {
        return new InternalBeforeLexHook(function);
    }

    /**
     * Returns a new after lex hook.
     * @param function Consumer that processes the list of tokens.
     * @return New after lex hook.
     */
    public static AfterLexHook afterLex(Consumer<List<Token>> function) {
        return new InternalAfterLexHook(function);
    }

    /**
     * Returns a new before parse hook.
     * @param function Consumer that processes the list of tokens.
     * @return New before parse hook.
     */
    public static BeforeParseHook beforeParse(Consumer<List<Token>> function) {
        return new InternalBeforeParseHook(function);
    }

    /**
     * Returns a new after parse hook.
     * @param function Consumer that processes the list of tokens and symbol tree.
     * @return New after parse hook.
     */
    public static AfterParseHook afterParse(BiConsumer<List<Token>, SymbolTree> function) {
        return new InternalAfterParseHook(function);
    }

    /**
     * Returns a new before analysis hook.
     * @param function Consumer that processes the list of tokens and symbol tree.
     * @return New before analysis hook.
     */
    public static BeforeSemanticAnalysisHook beforeSemanticAnalysis(BiConsumer<List<Token>, SymbolTree> function) {
        return new InternalBeforeSemanticAnalysisHook(function);
    }

    /**
     * Returns a new after analysis hook.
     * @param function Consumer that processes the list of tokens and symbol tree.
     * @return New after analysis hook.
     */
    public static AfterSemanticAnalysisHook afterSemanticAnalysis(BiConsumer<List<Token>, SymbolTree> function) {
        return new InternalAfterSemanticAnalysisHook(function);
    }

    /**
     * Returns a new before generation hook.
     * @param function Consumer that processes the list of tokens and symbol tree.
     * @return New before generation hook.
     */
    public static BeforeCodeGenerationHook beforeCodeGeneration(BiConsumer<List<Token>, SymbolTree> function) {
        return new InternalBeforeCodeGenerationHook(function);
    }

    /**
     * Returns a new after generation hook.
     * @param function Consumer that processes the list of tokens and symbol tree.
     * @return New after generation hook.
     */
    public static AfterCodeGenerationHook afterCodeGeneration(BiConsumer<List<Token>, SymbolTree> function) {
        return new InternalAfterCodeGenerationHook(function);
    }

    /**
     * Returns a new after compilation hook.
     * @param function Consumer that processes the list of tokens and symbol tree.
     * @return New after compilation hook.
     */
    public static AfterCompilationHook afterCompilation(BiConsumer<List<Token>, SymbolTree> function) {
        return new InternalAfterCompilationHook(function);
    }

    // ====== INTERNAL IMPLEMENTATIONS:

    private static final class InternalOnFailureHook extends OnFailureHook {
        private final Consumer<Throwable> function;

        private InternalOnFailureHook(Consumer<Throwable> function) {
            this.function = function;
        }

        @Override
        public void execute(Throwable throwable) {
            function.accept(throwable);
        }
    }

    private static final class InternalAfterCodeGenerationHook extends AfterCodeGenerationHook {
        private final BiConsumer<List<Token>, SymbolTree> function;

        private InternalAfterCodeGenerationHook(BiConsumer<List<Token>, SymbolTree> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens, SymbolTree symbolTree) {
            function.accept(tokens, symbolTree);
        }
    }

    private static final class InternalAfterCompilationHook extends AfterCompilationHook {
        private final BiConsumer<List<Token>, SymbolTree> function;

        private InternalAfterCompilationHook(BiConsumer<List<Token>, SymbolTree> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens, SymbolTree symbolTree) {
            function.accept(tokens, symbolTree);
        }
    }

    private static final class InternalAfterLexHook extends AfterLexHook {
        private final Consumer<List<Token>> function;

        private InternalAfterLexHook(Consumer<List<Token>> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens) {
            function.accept(tokens);
        }
    }

    private static final class InternalAfterParseHook extends AfterParseHook {
        private final BiConsumer<List<Token>, SymbolTree> function;

        private InternalAfterParseHook(BiConsumer<List<Token>, SymbolTree> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens, SymbolTree symbolTree) {
            function.accept(tokens, symbolTree);
        }
    }

    private static final class InternalAfterSemanticAnalysisHook extends AfterSemanticAnalysisHook {
        private final BiConsumer<List<Token>, SymbolTree> function;

        private InternalAfterSemanticAnalysisHook(BiConsumer<List<Token>, SymbolTree> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens, SymbolTree symbolTree) {
            function.accept(tokens, symbolTree);
        }
    }

    private static final class InternalBeforeCodeGenerationHook extends BeforeCodeGenerationHook {
        private final BiConsumer<List<Token>, SymbolTree> function;

        private InternalBeforeCodeGenerationHook(BiConsumer<List<Token>, SymbolTree> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens, SymbolTree symbolTree) {
            function.accept(tokens, symbolTree);
        }
    }

    private static final class InternalBeforeCompilationHook extends BeforeCompilationHook {
        private final Consumer<String> function;

        private InternalBeforeCompilationHook(Consumer<String> function) {
            this.function = function;
        }

        @Override
        public void execute(String rawInput) {
            function.accept(rawInput);
        }
    }

    private static final class InternalBeforeLexHook extends BeforeLexHook {
        private final Consumer<String> function;

        private InternalBeforeLexHook(Consumer<String> function) {
            this.function = function;
        }

        @Override
        public void execute(String rawInput) {
            function.accept(rawInput);
        }
    }

    private static final class InternalBeforeParseHook extends BeforeParseHook {
        private final Consumer<List<Token>> function;

        private InternalBeforeParseHook(Consumer<List<Token>> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens) {
            function.accept(tokens);
        }
    }

    private static final class InternalBeforeSemanticAnalysisHook extends BeforeSemanticAnalysisHook {
        private final BiConsumer<List<Token>, SymbolTree> function;

        private InternalBeforeSemanticAnalysisHook(BiConsumer<List<Token>, SymbolTree> function) {
            this.function = function;
        }

        @Override
        public void execute(List<Token> tokens, SymbolTree symbolTree) {
            function.accept(tokens, symbolTree);
        }
    }
}
