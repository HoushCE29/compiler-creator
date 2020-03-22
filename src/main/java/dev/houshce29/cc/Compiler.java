package dev.houshce29.cc;

import dev.houshce29.cc.analyze.DefaultSemanticAnalyzer;
import dev.houshce29.cc.analyze.SemanticAnalyzer;
import dev.houshce29.cc.extensions.ExtensionExecutionEngine;
import dev.houshce29.cc.extensions.Hook;
import dev.houshce29.cc.extensions.HookPoint;
import dev.houshce29.cc.generate.CodeGenerator;
import dev.houshce29.cc.generate.DefaultCodeGenerator;
import dev.houshce29.cc.lex.Lexer;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.Parser;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Compiler that can be defined to fit any language.
 */
public final class Compiler {
    private final Lexer lexer;
    private final Parser parser;
    private final SemanticAnalyzer analyzer;
    private final CodeGenerator generator;
    private final ExtensionExecutionEngine extensionEngine;

    /**
     * Internally creates a new instance.
     * @param lexer Lexer for building tokens.
     * @param parser Parser for building AST.
     * @param analyzer Analyzer for semantics.
     * @param generator Generator for code.
     * @param extensions Extensions to this compiler.
     */
    private Compiler(Lexer lexer,
                     Parser parser,
                     SemanticAnalyzer analyzer,
                     CodeGenerator generator,
                     Collection<Hook> extensions) {

        this.lexer = lexer;
        this.parser = parser;
        this.analyzer = analyzer;
        this.generator = generator;
        this.extensionEngine = ExtensionExecutionEngine.create(extensions);
    }

    /**
     * Compiles the given input.
     * @param input Input to compile.
     */
    public void compile(String input) {
        // Compile with or without extensions
        if (extensionEngine.hasExtensions()) {
            compileWithExtensions(input);
        }
        else {
            compileNoExtensions(input);
        }
    }

    /**
     * Extends this compiler by creating a copy of this compiler
     * and applying the new extensions. It is important to emphasize
     * that this will NOT change this instance of the compiler; the
     * new extensions are applied to a clone of this bearing said
     * extensions. A clone is what is returned by this method.
     * @param extensions Extensions to apply to a clone of this compiler.
     * @return New compiler instance containing the given extensions.
     */
    public Compiler extend(Hook... extensions) {
        return extend(Arrays.asList(extensions));
    }

    /**
     * Extends this compiler by creating a copy of this compiler
     * and applying the new extensions. It is important to emphasize
     * that this will NOT change this instance of the compiler; the
     * new extensions are applied to a clone of this bearing said
     * extensions. A clone is what is returned by this method.
     * @param extensions Extensions to apply to a clone of this compiler.
     * @return New compiler instance containing the given extensions.
     */
    public Compiler extend(Collection<Hook> extensions) {
        Collection<Hook> newExtensions = extensionEngine.getExtensions();
        newExtensions.addAll(extensions);
        return new Compiler(lexer, parser, analyzer, generator, newExtensions);
    }

    private void compileNoExtensions(String input) {
        try {
            List<Token> tokens = lexer.lex(input);
            SymbolTree symbolTree = parser.parse(tokens);
            analyzer.analyze(tokens, symbolTree);
            generator.generate(tokens, symbolTree);
        }
        catch (Throwable t) {
            handleError(t);
        }
    }

    private void compileWithExtensions(String input) {
        try {
            compileWithExtensionsRaw(input);
        }
        catch (Throwable t) {
            extensionEngine.runForFailure(t);
            handleError(t);
        }
    }

    private void compileWithExtensionsRaw(String input) {
        extensionEngine.run(HookPoint.BEFORE_COMPILE, input);

        // Lex
        extensionEngine.run(HookPoint.BEFORE_LEX, input);
        List<Token> tokens = lexer.lex(input);
        extensionEngine.run(HookPoint.AFTER_LEX, tokens);

        // Parse
        extensionEngine.run(HookPoint.BEFORE_PARSE, tokens);
        SymbolTree symbolTree = parser.parse(tokens);
        extensionEngine.run(HookPoint.AFTER_PARSE, tokens, symbolTree);

        // Semantic analyze
        extensionEngine.run(HookPoint.BEFORE_SEMANTIC_ANALYSIS, tokens, symbolTree);
        analyzer.analyze(tokens, symbolTree);
        extensionEngine.run(HookPoint.AFTER_SEMANTIC_ANALYSIS, tokens, symbolTree);

        // Code generate
        extensionEngine.run(HookPoint.BEFORE_CODE_GENERATION, tokens, symbolTree);
        generator.generate(tokens, symbolTree);
        extensionEngine.run(HookPoint.AFTER_CODE_GENERATION, tokens, symbolTree);

        extensionEngine.run(HookPoint.AFTER_COMPILE, tokens, symbolTree);
    }

    private void handleError(Throwable t) {
        System.err.println(t.getMessage());
        System.exit(-1);
    }

    /**
     * Builder for creating a compiler.
     */
    public static final class Builder {
        private Lexer lexer;
        private Parser parser;
        private SemanticAnalyzer analyzer;
        private CodeGenerator generator;
        private final List<Hook> hooks = new ArrayList<>();

        private Builder() {
        }

        /**
         * Sets the lexer.
         * @param lexer Lexer to set for this compiler.
         * @return This builder.
         */
        public Builder setLexer(Lexer lexer) {
            this.lexer = lexer;
            return this;
        }

        /**
         * Sets the lexer.
         * @param lexer Lexer to set for this compiler.
         * @return This builder.
         */
        public Builder setLexer(Lexer.Builder lexer) {
            return setLexer(lexer.build());
        }

        /**
         * Sets the parser.
         * @param parser Parser to set for this compiler.
         * @return This builder.
         */
        public Builder setParser(Parser parser) {
            this.parser = parser;
            return this;
        }

        /**
         * Sets the parser.
         * @param parser Parser to set for this compiler.
         * @return This builder.
         */
        public Builder setParser(Parser.Builder parser) {
            return setParser(parser.build());
        }

        /**
         * Sets the semantic analyzer.
         * @param analyzer Analyzer to set for this compiler.
         * @return This builder.
         */
        public Builder setSemanticAnalyzer(SemanticAnalyzer analyzer) {
            this.analyzer = analyzer;
            return this;
        }

        /**
         * Sets the code generator.
         * @param generator Generator to set for this compiler.
         * @return This builder.
         */
        public Builder setCodeGenerator(CodeGenerator generator) {
            this.generator = generator;
            return this;
        }

        /**
         * Adds one or more extensions to the compiler.
         * @param hooks Extensions to add.
         * @return This builder.
         */
        public Builder addExtensions(Hook... hooks) {
            return addExtensions(Arrays.asList(hooks));
        }

        /**
         * Add the collection of extensions to the compiler.
         * @param hooks Extensions to add.
         * @return This builder.
         */
        public Builder addExtensions(Collection<Hook> hooks) {
            this.hooks.addAll(hooks);
            return this;
        }

        /**
         * Builds the compiler. If necessary, any missing, undefined
         * compiler components will be set to some default.
         * @return New compiler instance.
         */
        public Compiler build() {
            // Default all these things if missing.
            // While it's tempting to declare that an exception
            // should be thrown here if these are not present,
            // it's also important to remember that compiler
            // engineering can be a crash course, so allowing for
            // testing of a single written segment at a time is
            // essential for compiler development.
            if (lexer == null) {
                lexer = Lexer.DEFAULT_LEXER;
            }
            if (parser == null) {
                parser = Parser.DEFAULT_PARSER;
            }
            if (analyzer == null) {
                analyzer = new DefaultSemanticAnalyzer();
            }
            if (generator == null) {
                generator = new DefaultCodeGenerator();
            }
            hooks.removeIf(Objects::isNull);
            return new Compiler(lexer, parser, analyzer, generator, hooks);
        }
    }
}
