package dev.houshce29.cc.lex;

import dev.houshce29.cc.common.CompilerComponent;
import dev.houshce29.cc.common.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Service that builds tokens.
 */
public final class Lexer implements CompilerComponent {
    public static final Lexer DEFAULT_LEXER = Lexer.newBuilder()
            .on(RegexFactory.anythingRegex())
                .error(context -> new UnsupportedOperationException("No lexer definition for this compiler."))
            .build();
    private final List<Pair<InputMatcher, Function<ScanContext, ?>>> factory;
    private final BiConsumer<String, List<Token>> afterFunction;

    /**
     * Privately creates a new lexer.
     * @param factory Ordered list of pairs (context => function) that creates tokens
     *                or errors out based on regex.
     * @param afterFunction Custom logic to run after lexing.
     */
    private Lexer(List<Pair<InputMatcher, Function<ScanContext, ?>>> factory,
                  BiConsumer<String, List<Token>> afterFunction) {
        this.factory = factory;
        this.afterFunction = afterFunction;
    }

    /**
     * Plucks an ordered list of tokens from the raw input. This list
     * will NOT contain ignored tokens.
     * @param rawInput Raw input to pluck tokens out of.
     * @return Ordered list of tokens.
     */
    public List<Token> lex(String rawInput) {
        List<Token> tokens = new ArrayList<>();
        ScanContext scanContext = new ScanContext();
        String scanned = "";
        int scanPosition = 0;
        // Scan the entire input length
        while (scanPosition < rawInput.length()) {
            // Main control flag that's set to true when a token was generated.
            boolean generated = false;
            // Try each provider in order
            __factoryProviderLoop__:
            for (Pair<InputMatcher, Function<ScanContext, ?>> provider : factory) {
                // Obtain strategy to determine when to consume
                MatchingStrategy strategy = provider.getKey().getStrategy();
                Optional<? extends Token> lastMatch = Optional.empty();
                int lastMatchEndPosition = scanPosition;
                // Set the end position by starting from the scan position and moving forward one by one
                for (int endPosition = scanPosition; endPosition < rawInput.length(); endPosition++) {
                    // Scan a new substring
                    scanned = rawInput.substring(scanPosition, endPosition + 1);
                    Optional<? extends Token> maybeToken = getToken(provider, scanContext, scanned);

                    // If there's a token, generation was successful
                    if (maybeToken.isPresent()) {
                        lastMatchEndPosition = endPosition;
                        switch (strategy) {
                            case SPAN:
                            case MAX:
                                lastMatch = maybeToken;
                                break;
                            default:
                                generated = true;
                                // Push ahead to the current position
                                scanPosition = endPosition + 1;
                                Token token = maybeToken.get();
                                // Only add this token if not ignored.
                                if (!token.isIgnored()) {
                                    tokens.add(token);
                                }
                                // Get out of this loop; flow back to reset the provider loop.
                                break __factoryProviderLoop__;
                        }
                    }
                    // Middle case: SPAN, no match this sequence, previous match found
                    else if (strategy == MatchingStrategy.SPAN && lastMatch.isPresent()) {
                        generated = true;
                        scanPosition = endPosition;
                        Token token = lastMatch.get();
                        // Only add this token if not ignored.
                        if (!token.isIgnored()) {
                            tokens.add(token);
                        }
                        // Get out of this loop; flow back to reset the provider loop.
                        break __factoryProviderLoop__;
                    }

                    // End of input case
                    if (endPosition >= rawInput.length() - 1 && lastMatch.isPresent()) {
                        generated = true;
                        Token token = lastMatch.get();
                        scanPosition = lastMatchEndPosition + 1;
                        // Only add this token if not ignored
                        if (!token.isIgnored()) {
                            tokens.add(token);
                        }
                        break __factoryProviderLoop__;
                    }
                } // end input position loop
            } // end factory provider loop
            // If not generated, there was an unexpected token.
            if (!generated) {
                throw new IllegalArgumentException("Invalid token [" + scanned + "] on line " + scanContext.getLineNumber() + ".");
            }
        } // end main control loop
        afterFunction.accept(rawInput, tokens);
        return tokens;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("LEXER\n");
        for (Pair<InputMatcher, Function<ScanContext, ?>> provider : factory) {
            builder.append("  ")
                    .append(provider.getKey())
                    .append("\n");
        }

        return builder.toString();
    }

    /**
     * Maybe returns a single token
     * @param currentProvider Current factory function.
     * @param scanContext Current state of the lexer.
     * @param in Raw string input.
     * @return Optional maybe containing a token, if the provider doesn't contain an exception, which would
     *         have been raised prior to returning anything.
     */
    private Optional<? extends Token> getToken(Pair<InputMatcher, Function<ScanContext, ?>> currentProvider,
                                               ScanContext scanContext,
                                               String in) {

        // If no match, return empty
        if (!currentProvider.getKey().matches(in)) {
            return Optional.empty();
        }
        // Obtain the object from the function
        scanContext.capture(in);
        Object generated = currentProvider.getValue().apply(scanContext);

        // If a runtime exception, throw it to terminate compiler.
        if (generated instanceof RuntimeException) {
            throw (RuntimeException) generated;
        }

        // If a token, return it.
        else if (generated instanceof Token) {
            return Optional.of((Token) generated);
        }

        // If we're here, this compiler was misconfigured.
        throw new UnsupportedOperationException("Expected to create token or throw exception; instead got: " + generated);
    }

    /**
     * @return A new builder for building a Lexer.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder for creating new Lexers.
     */
    public static final class Builder {
        private List<Pair<InputMatcher, Function<ScanContext, ?>>> factory = new ArrayList<>();
        private BiConsumer<String, List<Token>> afterFunction;

        private Builder() {
        }

        /**
         * Specifies a regex to listen for. NOTE: ORDER MATTER! For instance,
         * if the language has two syntax literals in which one is a substring of the
         * other such as 'else' and 'elseif', then 'elseif' should be defined BEFORE
         * 'else'; doing so the other way will cause the lexer to falsely capture a
         * token for 'else' upon scanning over some 'elseif' substring input. This
         * will use the default matching strategy, GREEDY.
         * @param regex Regex to match substring input to in order to either trigger
         *              token creation or an exception to be thrown.
         * @return Intermediate builder to define what happens on this event.
         */
        public TokenFunctionBuilder on(String regex) {
            return on(regex, MatchingStrategy.GREEDY);
        }

        /**
         * Specifies a regex to listen for. NOTE: ORDER MATTER! For instance,
         * if the language has two syntax literals in which one is a substring of the
         * other such as 'else' and 'elseif', then 'elseif' should be defined BEFORE
         * 'else'; doing so the other way will cause the lexer to falsely capture a
         * token for 'else' upon scanning over some 'elseif' substring input.
         * @param regex Regex to match substring input to in order to either trigger
         *              token creation or an exception to be thrown.
         * @param strategy Strategy for matching and consuming tokens. This can help
         *                 ensure that the correct values are consumed.
         * @return Intermediate builder to define what happens on this event.
         */
        public TokenFunctionBuilder on(String regex, MatchingStrategy strategy) {
            return new TokenFunctionBuilder(this, regex, strategy);
        }

        /**
         * Defines a single piece of logic to run _after_ lexing has finished.
         * This is a function that accepts the raw input and resulting tokens.
         * Note that the passed in list is mutable and can be modified before
         * moving onto the parser.
         * @param afterFunction Function to run after lexing.
         * @return This builder.
         */
        public Builder after(BiConsumer<String, List<Token>> afterFunction) {
            this.afterFunction = afterFunction;
            return this;
        }

        /**
         * Defines a single piece of logic to run _after_ lexing has finished.
         * This is a function that accepts the resulting tokens.
         * Note that the passed in list is mutable and can be modified before
         * moving onto the parser.
         * @param afterFunction Function to run after lexing.
         * @return This builder.
         */
        public Builder after(Consumer<List<Token>> afterFunction) {
            this.afterFunction = (input, tokens) -> afterFunction.accept(tokens);
            return this;
        }

        /**
         * Builds the Lexer.
         * @return New Lexer.
         */
        public Lexer build() {
            if (afterFunction == null) {
                afterFunction = (s, lt) -> {};
            }
            return new Lexer(factory, afterFunction);
        }

        /**
         * Internal method to push in a finalized definition.
         * @param def Definition to push in.
         * @return This builder, for conveniently chaining methods together.
         */
        private Builder push(Pair<InputMatcher, Function<ScanContext, ?>> def) {
            factory.add(def);
            return this;
        }
    }

    /**
     * Intermediate builder for defining what to do upon scanning
     * a substring matching the regex.
     */
    public static final class TokenFunctionBuilder {
        private Builder currentBuilder;
        private String currentRegex;
        private MatchingStrategy strategy;

        private TokenFunctionBuilder(Builder currentBuilder,
                                     String currentRegex,
                                     MatchingStrategy strategy) {
            this.currentBuilder = currentBuilder;
            this.currentRegex = currentRegex;
            this.strategy = strategy;
        }

        /**
         * Defines a function to fire to create a token when string
         * input matches the regex defined.
         * @param def Function that takes in the line number and matching
         *            string and returns a new token.
         * @return The token factory builder.
         */
        public Builder create(Function<ScanContext, Token> def) {
            return currentBuilder.push(Pair.of(new InputMatcher(currentRegex, strategy), def));
        }

        /**
         * Defines a function to fire to create an exception to be thrown
         * when string input matches the regex defined.
         * @param def Function that takes in the line number and matching
         *            string and returns a new exception to be thrown.
         * @return The token factory builder.
         */
        public Builder error(Function<ScanContext, RuntimeException> def) {
            return currentBuilder.push(Pair.of(new InputMatcher(currentRegex, strategy), def));
        }
    }
}
