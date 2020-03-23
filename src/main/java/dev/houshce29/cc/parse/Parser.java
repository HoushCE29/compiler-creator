package dev.houshce29.cc.parse;

import dev.houshce29.cc.lex.SimpleToken;
import dev.houshce29.cc.lex.Token;

import java.util.List;
import java.util.Optional;

/**
 * Service that validates that the incoming tokens conform to
 * the grammar/syntax and then creates a symbol tree from them.
 */
public final class Parser {
    public static final Parser DEFAULT_PARSER = Parser.newBuilder("<EMPTY>").build();
    private final Grammar grammar;

    /**
     * Privately creates new instance.
     * @param grammar Grammar to use for this parser.
     */
    private Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    /**
     * Parses the token list into a symbol tree.
     * @param tokens Tokens to parse into symbol tree.
     * @return Symbol tree parsed from the tokens conforming to the base grammar.
     */
    public SymbolTree parse(List<Token> tokens) {
        try {
            ParseFailureSite failureSite = new ParseFailureSite(tokens);
            Optional<SymbolTreeNode> root = parseUsingPhrase(tokens, grammar.getRoot(), failureSite);
            if (!root.isPresent()) {
                Token token = failureSite.getFailureToken();
                throw new IllegalArgumentException("Syntax error near token '" + token.getValue() + "' on line " + token.getLineNumber() + ".");
            }
            SymbolTreeNode rootNode = root.get();
            int tokenCount = rootNode.tokenCount();
            if (tokenCount < tokens.size()) {
                Token token = tokens.get(tokenCount);
                throw new IllegalArgumentException("Unexpected token '" + token.getValue() + "' on line " + token.getLineNumber() + ".");
            }
            return new SymbolTree(root.get());
        }
        catch (StackOverflowError err) {
            throw new UnsupportedOperationException("FATAL: Parser's grammar is too deep or left-ambiguous.");
        }
    }

    @Override
    public String toString() {
        return "PARSER\n" + grammar;
    }

    /**
     * Returns a new parser from the grammar.
     * @param grammar Grammar that defines the parser.
     * @return New parser.
     */
    public static Parser of(Grammar grammar) {
        return new Parser(grammar);
    }

    /**
     * Returns a new parser builder.
     * @param rootPhraseId ID of the root phrase in the grammar.
     * @return New builder instance.
     */
    public static Builder newBuilder(String rootPhraseId) {
        return new Builder(rootPhraseId);
    }

    /**
     * Internally parses tokens within the constraint of a given phrase.
     * @param tokens Tokens list or sublist to parse.
     * @param current Current phrase to parse against.
     * @param site Reported failure site. Used to report parse failures.
     * @return Tree node if parsing against the current phrase resolves.
     */
    private Optional<SymbolTreeNode> parseUsingPhrase(List<Token> tokens, Phrase current, ParseFailureSite site) {
        for (List<String> sentence : current.getSentences()) {
            Optional<SymbolTreeNode> node = parseUsingSentence(current.getId(), sentence, tokens, site);
            if (node.isPresent()) {
                site.clear();
                return node;
            }
        }
        // We're targeting a specific path within the token sub list,
        // so it's not a big deal if this doesn't resolve.
        site.report(tokens);
        return Optional.empty();
    }

    /**
     * Internally parses tokens within the constraints of a given sentence.
     * @param phraseId ID of the phrase that owns the sentence.
     * @param sentence Sentence to attempt to parse with.
     * @param tokens Token list or sublist to parse.
     * @param site Reported failure site. Used to report parse failures.
     * @return Tree node if parsing against the current sentence resolves.
     */
    private Optional<SymbolTreeNode> parseUsingSentence(String phraseId,
                                                        List<String> sentence,
                                                        List<Token> tokens,
                                                        ParseFailureSite site) {

        // Depth of token list to recursively push
        int depth = 0;
        SymbolTreeNode node = new SymbolTreeNode(phraseId);
        for (String id : sentence) {
            // Unexpected token case for some recursive phrase
            if (depth == tokens.size()) {
                return Optional.empty();
            }
            Optional<Phrase> phrase = find(id);
            // This is a phrase, thus need to dig in recursively and dig out children.
            // If this child does not parse out, this tree path does not work.
            if (phrase.isPresent()) {
                Optional<SymbolTreeNode> child = parseUsingPhrase(tokens.subList(depth, tokens.size()), phrase.get(), site);
                if (!child.isPresent()) {
                    site.report(tokens);
                    return Optional.empty();
                }
                SymbolTreeNode childNode = child.get();
                node.getChildren().add(childNode);
                depth += childNode.tokenCount();
            }
            // Else check token match
            else if (id.equals(tokens.get(depth).getId())) {
                // Consume the token and push the depth up
                node.getChildren().add(tokens.get(depth));
                depth++;
            }
            // Else, this doesn't match
            else {
                site.report(tokens);
                return Optional.empty();
            }
        }
        // If the entire sentence is traversed, then a node is successfully formed.
        site.clear();
        return Optional.of(node);
    }

    /**
     * Finds a phrase if it exists.
     * @param id ID of the phrase.
     * @return Optional maybe containing a phrase.
     */
    private Optional<Phrase> find(String id) {
        return Optional.ofNullable(grammar.getPhrases().get(id));
    }

    /**
     * Holder that contains a token that failed the parse phase.
     * This will track the latest failure in terms of "token-depth";
     * that is, the token furthest in the token list that caused a
     * failure.
     */
    private static final class ParseFailureSite {
        private static final Token NOTHING = new SimpleToken("", 1);
        private int remainingDistance;
        private final int totalDistance;
        private Token token = NOTHING;

        /**
         * Creates a new instance from the token list.
         * @param initialTokenList Token list.
         */
        private ParseFailureSite(List<Token> initialTokenList) {
            this.totalDistance = initialTokenList.size();
            this.remainingDistance = initialTokenList.size();
            if (!initialTokenList.isEmpty()) {
                token = initialTokenList.get(0);
            }
        }

        /**
         * @return The approximate failing token.
         */
        private Token getFailureToken() {
            return token;
        }

        /**
         * Reports an approximate failure site.
         * @param token Approximate token failing parse.
         * @param remainingDistance Remaining distance to the end of the current token sublist.
         */
        private void report(Token token, int remainingDistance) {
            if (remainingDistance < this.remainingDistance) {
                this.token = token;
            }
        }

        /**
         * Reports an approximate failing site.
         * @param tokens Sub-list of remaining tokens to be parsed.
         */
        private void report(List<Token> tokens) {
            if (!tokens.isEmpty()) {
                report(tokens.get(0), tokens.size());
            }
        }

        /**
         * Clears the current fail site data. Due to the fact that
         * the parser traverses a tree, there will be a lot of false-
         * positives pointing at deeply-nested failures, so upon creating
         * a new node, this method should be called to ensure that any
         * future failures are recorded.
         */
        private void clear() {
            this.token = NOTHING;
            this.remainingDistance = totalDistance;
        }
    }

    /**
     * Simplified builder for building a parser.
     */
    public static final class Builder {
        private Phrase.Builder currentPhrase;
        private Grammar.Builder grammar;

        private Builder(String rootId) {
            this.currentPhrase = Phrase.newBuilder(rootId);
        }

        /**
         * Adds a sentence to the current phrase.
         * @param sentence Sentence to be added to current phrase.
         * @return This builder.
         */
        public Builder sentence(String... sentence) {
            currentPhrase.addSentence(sentence);
            return this;
        }

        /**
         * Branches the grammar to start a new phrase definition.
         * @param id ID of the next phrase.
         * @return This builder, iterated to a new phrase.
         */
        public Builder branch(String id) {
            applyCurrentPhrase();
            currentPhrase = Phrase.newBuilder(id);
            return this;
        }

        /**
         * Builds the parser.
         * @return New parser.
         */
        public Parser build() {
            applyCurrentPhrase();
            return Parser.of(grammar.build());
        }

        /**
         * Internally adds the phrase to the grammar.
         * If the grammar is null, then it's assumed that
         * the current phrase is the root phrase.
         */
        private void applyCurrentPhrase() {
            if (grammar == null) {
                grammar = Grammar.from(currentPhrase.build());
            }
            else {
                grammar.addPhrase(currentPhrase);
            }
        }
    }
}
