package dev.houshce29.cc.lex;

import dev.houshce29.cc.common.IdentifiableGrammarComponent;

/**
 * Represents a grammatical token created during the lexer phase.
 */
public interface Token extends IdentifiableGrammarComponent {

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
}
