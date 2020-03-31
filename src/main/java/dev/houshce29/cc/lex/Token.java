package dev.houshce29.cc.lex;

import dev.houshce29.cc.common.GrammarComponent;

/**
 * Represents a grammatical token created during the lexer phase.
 */
public interface Token extends GrammarComponent {

    /**
     * Returns the captured value of this token. This can be some static
     * syntactic literal (e.g. RCURLY => '}' or WHILE => 'while') as well as a dynamic value
     * captured from the code being compiled (e.g. STRING-LITERAL => '"foo"').
     * @return Value of this token, either some static value or a captured dynamic value.
     */
    String getValue();

    /**
     * @return The line number that this exact token is on.
     */
    int getLineNumber();

    /**
     * When set to {@code true}, this token will be ignored during parsing.
     * Examples in the Java language are white spaces, new lines, and comments.
     * @return Whether or not this token should be ignored during parsing.
     * @see IgnoredToken
     */
    default boolean isIgnored() {
        return false;
    }

    /**
     * Casts the component into a token if possible.
     * This is a useful utility method for semantic analysis and
     * code/model generation, where the grammar most likely ensured
     * that the correct component was created, but of course, there's
     * always the slight chance that something went wrong :).
     * @param component Component to cast to a token.
     * @return Token.
     * @throws IllegalArgumentException if this component cannot be casted to a token.
     */
    static Token cast(GrammarComponent component) throws IllegalArgumentException {
        if (component instanceof Token) {
            return (Token) component;
        }
        throw new IllegalArgumentException(component.getId() + " was expected to be a token.");
    }

    /**
     * Returns the value of the component, after being casted to a token.
     * @param component Component that is expected to be a token.
     * @return Value of the token.
     * @throws IllegalArgumentException if this component cannot be casted to a token.
     */
    static String valueOf(GrammarComponent component) throws IllegalArgumentException {
        return cast(component).getValue();
    }
}
