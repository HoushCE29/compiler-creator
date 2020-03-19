package dev.houshce29.cc.lex;

import dev.houshce29.cc.internal.qa.Complex;
import dev.houshce29.cc.common.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Service that builds tokens.
 */
public final class Lexer {
    private final List<Pair<Pattern, Function<Context, ?>>> factory;

    /**
     * Privately creates a new lexer.
     * @param factory Ordered list of pairs (context => function) that creates tokens
     *                or errors out based on regex.
     */
    private Lexer(List<Pair<Pattern, Function<Context, ?>>> factory) {
        this.factory = factory;
    }

    /**
     * Plucks an ordered list of tokens from the raw input. This list
     * will NOT contain ignored tokens.
     * @param rawInput Raw input to pluck tokens out of.
     * @return Ordered list of tokens.
     */
    @Complex("Multi-loop dependency on primitive values.")
    public List<? extends Token> getTokens(String rawInput) {
        List<Token> tokens = new ArrayList<>();
        Context context = new Context();
        String scanned = "";
        int scanPosition = 0;
        // Scan the entire input length
        while (scanPosition < rawInput.length()) {
            // Main control flag that's set to true when a token was generated.
            boolean generated = false;
            // Try each provider in order
            __factoryProviderLoop__:
            for (Pair<Pattern, Function<Context, ?>> provider : factory) {
                // Set the end position by starting from the scan position and moving forward one by one
                for (int endPosition = scanPosition; endPosition < rawInput.length(); endPosition++) {
                    // Scan a new substring
                    scanned = rawInput.substring(scanPosition, endPosition + 1);
                    Optional<? extends Token> maybeToken = getToken(provider, context, scanned);

                    // If there's a token, generation was successful
                    if (maybeToken.isPresent()) {
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
                } // end input position loop
            } // end factory provider loop
            // If not generated, there was an unexpected token.
            if (!generated) {
                throw new IllegalArgumentException("Invalid token [" + scanned + "] on line " + context.getLineNumber() + ".");
            }
        }
        return tokens;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("LEXER\n");
        for (Pair<Pattern, Function<Context, ?>> provider : factory) {
            builder.append("  ")
                    .append(provider.getKey())
                    .append("\n");
        }

        return builder.toString();
    }

    /**
     * Maybe returns a single token
     * @param currentProvider Current factory function.
     * @param context Current state of the lexer.
     * @param in Raw string input.
     * @return Optional maybe containing a token, if the provider doesn't contain an exception, which would
     *         have been raised prior to returning anything.
     */
    private Optional<? extends Token> getToken(Pair<Pattern, Function<Context, ?>> currentProvider,
                                               Context context,
                                               String in) {

        // If no match, return empty
        if (!currentProvider.getKey().matcher(in).matches()) {
            return Optional.empty();
        }
        // Obtain the object from the function
        context.capture(in);
        Object generated = currentProvider.getValue().apply(context);

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
        private List<Pair<Pattern, Function<Context, ?>>> factory = new ArrayList<>();

        private Builder() {
        }

        /**
         * Specifies a regex to listen for. NOTE: ORDER MATTER! For instance,
         * if the language has two syntax literals in which one is a substring of the
         * other such as 'else' and 'elseif', then 'elseif' should be defined BEFORE
         * 'else'; doing so the other way will cause the lexer to falsely capture a
         * token for 'else' upon scanning over some 'elseif' substring input.
         * @param regex Regex to match substring input to in order to either trigger
         *              token creation or an exception to be thrown.
         * @return Intermediate builder to define what happens on this event.
         */
        public TokenFunctionBuilder on(String regex) {
            return new TokenFunctionBuilder(this, regex);
        }

        /**
         * Builds the Lexer.
         * @return New Lexer.
         */
        public Lexer build() {
            return new Lexer(factory);
        }

        /**
         * Internal method to push in a finalized definition.
         * @param def Definition to push in.
         * @return This builder, for conveniently chaining methods together.
         */
        private Builder push(Pair<Pattern, Function<Context, ?>> def) {
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

        private TokenFunctionBuilder(Builder currentBuilder,
                                     String currentRegex) {
            this.currentBuilder = currentBuilder;
            this.currentRegex = currentRegex;
        }

        /**
         * Defines a function to fire to create a token when string
         * input matches the regex defined.
         * @param def Function that takes in the line number and matching
         *            string and returns a new token.
         * @return The token factory builder.
         */
        public Builder doCreate(Function<Context, Token> def) {
            return currentBuilder.push(Pair.of(Pattern.compile(currentRegex), def));
        }

        /**
         * Defines a function to fire to create an exception to be thrown
         * when string input matches the regex defined.
         * @param def Function that takes in the line number and matching
         *            string and returns a new exception to be thrown.
         * @return The token factory builder.
         */
        public Builder doThrow(Function<Context, RuntimeException> def) {
            return currentBuilder.push(Pair.of(Pattern.compile(currentRegex), def));
        }
    }
}
