package dev.houshce29.cc.parse;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the language's grammar.
 */
public class Grammar {
    private final Phrase root;
    private final Map<String, Phrase> phrases;

    /**
     * Privately creates a new instance.
     * @param root Root phrase that starts the tree.
     * @param phrases All phrases pushed into a map keyed from their IDs.
     */
    private Grammar(Phrase root, Map<String, Phrase> phrases) {
        this.root = root;
        this.phrases = phrases;
    }

    /**
     * @return The starting root phrase.
     */
    public Phrase getRoot() {
        return root;
    }

    /**
     * Returns the map of all phrases, keyed from the phrase ID.
     * @return Phrase map.
     */
    public Map<String, Phrase> getPhrases() {
        return phrases;
    }

    /**
     * Begins the creation of grammar by setting the starting phrase.
     * @param root Root phrase.
     * @return Builder to add follow-up phrases to.
     */
    public static Builder from(Phrase root) {
        return new Builder(root);
    }

    /**
     * Builder for building the grammar.
     */
    public static final class Builder {
        private final Phrase root;
        private final Map<String, Phrase> phrases = new HashMap<>();

        private Builder(Phrase root) {
            this.root = root;
            phrases.put(root.getId(), this.root);
        }

        /**
         * Adds a phrase to the grammar being built.
         * @param phrase Phrase to add.
         * @return This builder.
         */
        public Builder addPhrase(Phrase phrase) {
            phrases.put(phrase.getId(), phrase);
            return this;
        }

        /**
         * Adds a phrase to the grammar being built.
         * @param phraseBuilder Phrase to add.
         * @return This builder.
         */
        public Builder addPhrase(Phrase.Builder phraseBuilder) {
            return this.addPhrase(phraseBuilder.build());
        }

        /**
         * Builds the grammar.
         * @return New grammar instance.
         */
        public Grammar build() {
            return new Grammar(root, phrases);
        }
    }
}
