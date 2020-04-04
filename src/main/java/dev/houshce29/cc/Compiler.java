package dev.houshce29.cc;

import dev.houshce29.cc.analyze.DefaultSemanticAnalyzer;
import dev.houshce29.cc.analyze.SemanticAnalyzer;
import dev.houshce29.cc.common.CompilationException;
import dev.houshce29.cc.generate.DefaultGenerator;
import dev.houshce29.cc.generate.Generator;
import dev.houshce29.cc.lex.Lexer;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.Parser;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.List;

/**
 * Compiler that can be defined to fit any language.
 */
public final class Compiler {
    private final Lexer lexer;
    private final Parser parser;
    private final SemanticAnalyzer analyzer;
    private final Generator generator;

    /**
     * Internally creates a new instance.
     * @param lexer Lexer for building tokens.
     * @param parser Parser for building AST.
     * @param analyzer Analyzer for semantics.
     * @param generator Generator for compiler.
     */
    private Compiler(Lexer lexer,
                     Parser parser,
                     SemanticAnalyzer analyzer,
                     Generator generator) {

        this.lexer = lexer;
        this.parser = parser;
        this.analyzer = analyzer;
        this.generator = generator;
    }

    /**
     * Compiles the given input.
     * @param input Input to compile.
     * @return Anything generated; if this is a code generator,
     *         the returned value is likely `null`.
     */
    public Object compile(String input) {
        try {
            List<Token> tokens = lexer.lex(input);
            SymbolTree symbolTree = parser.parse(tokens);
            analyzer.analyze(tokens, symbolTree);
            return generator.generate(tokens, symbolTree);
        }
        catch (Throwable t) {
            throw new CompilationException(t);
        }
    }

    /**
     * @return This compiler disassembled back into its builder.
     */
    public Builder toBuilder() {
        return Compiler.newBuilder()
                .setLexer(lexer)
                .setParser(parser)
                .setSemanticAnalyzer(analyzer)
                .setGenerator(generator);
    }

    /**
     * @return New compiler builder instance.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder for creating a compiler.
     */
    public static final class Builder {
        private Lexer lexer;
        private Parser parser;
        private SemanticAnalyzer analyzer;
        private Generator generator;

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
         * Sets the generator.
         * @param generator Generator to set for this compiler.
         * @return This builder.
         */
        public Builder setGenerator(Generator generator) {
            this.generator = generator;
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
                generator = new DefaultGenerator();
            }
            return new Compiler(lexer, parser, analyzer, generator);
        }
    }
}
