package dev.houshce29.cc.parse;

import dev.houshce29.cc.common.IdentifiableGrammarComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a collection of tokens or other phrases that form grammar.
 */
public class Phrase implements IdentifiableGrammarComponent {
    private final String id;
    private final List<List<String>> sentences;

    /**
     * Privately creates a phrase.
     * @param id Unique identifier of this phrase.
     *           WARNING: having this identifier the same
     *           as a token can cause the token to be ignored.
     * @param sentences List of list of tokens and other phrases making up grammar.
     */
    private Phrase(String id, List<List<String>> sentences) {
        this.id = id;
        this.sentences = sentences;
    }

    /**
     * @return All sentences for this phrase.
     */
    public List<List<String>> getSentences() {
        return sentences;
    }

    @Override
    public String getId() {
        return id;
    }

    public static Builder newBuilder(String id) {
        return new Builder(id);
    }

    /**
     * Builder for creating phrases.
     */
    public static final class Builder {
        private final String id;
        private List<List<String>> sentences = new ArrayList<>();

        private Builder(String id) {
            this.id = id;
        }

        /**
         * Adds a sentence to the phrase being built.
         * @param sentence Array of each word (e.g. token or phrase)
         *                 for this phrase being built.
         * @return This builder.
         */
        public Builder addSentence(String... sentence) {
            return this.addSentence(Arrays.asList(sentence));
        }

        /**
         * Adds a sentence to the phrase being built.
         * @param sentence Ordered list of each word (e.g. token or phrase)
         *                 for this phrase being built.
         * @return This builder.
         */
        public Builder addSentence(List<String> sentence) {
            sentences.add(sentence);
            return this;
        }

        /**
         * Builds a new phrase instance.
         * @return New phrase instance.
         */
        public Phrase build() {
            return new Phrase(id, sentences);
        }
    }
}
