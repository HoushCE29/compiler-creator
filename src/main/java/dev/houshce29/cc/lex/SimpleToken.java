package dev.houshce29.cc.lex;

import java.util.Objects;

/**
 * A generic implementation of a token.
 */
public class SimpleToken implements Token {
    private static final String STRING_FORMAT = "%s: %s";
    private final String id;
    private final String value;
    private final int lineNumber;

    /**
     * Creates a new simple token.
     * @param id Unique identifier of this token.
     * @param value Value of this token.
     * @param lineNumber Line number that this token shows up on.
     */
    public SimpleToken(String id, String value, int lineNumber) {
        this.id = id;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    /**
     * Creates a new token whose identifier and value
     * are the same.
     * @param idAndValue ID and value of this token.
     * @param lineNumber Line number that this token shows up on.
     */
    public SimpleToken(String idAndValue, int lineNumber) {
        this(idAndValue, idAndValue, lineNumber);
    }

    /**
     * Creates a new simple token.
     * @param id Unique identifier of this token.
     * @param scanned Scan context to pluck value and line number from.
     */
    public SimpleToken(String id, ScanContext scanned) {
        this(id, scanned.getCapturedValue(), scanned.getLineNumber());
    }

    @Override
    public final String getValue() {
        return value;
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final int getLineNumber() {
        return lineNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token other = (Token) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getValue(), value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }

    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, value);
    }
}
