package dev.houshce29.cc.lex;

/**
 * Convenience token for ignoring
 */
public class IgnoredToken extends SimpleToken {
    private static final String DEFAULT_ID = "__CC_IGNORED_TOKEN__";
    private static final String DEFAULT_VALUE = "";
    private static final int DEFAULT_LINE_NUMBER = -1;

    /**
     * Creates a new instance.
     * @param id ID of the token.
     * @param value Value of the token.
     * @param lineNumber Line number at which this token started at.
     */
    public IgnoredToken(String id, String value, int lineNumber) {
        super(id, value, lineNumber);
    }

    /**
     * Creates a new default instance.
     */
    public IgnoredToken() {
        this(DEFAULT_ID, DEFAULT_VALUE, DEFAULT_LINE_NUMBER);
    }

    @Override
    public final boolean isIgnored() {
        return true;
    }
}
