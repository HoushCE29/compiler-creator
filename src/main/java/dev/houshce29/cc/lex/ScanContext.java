package dev.houshce29.cc.lex;

import java.util.HashMap;
import java.util.Map;

/**
 * Bundle of contextual lexer info. This includes:
 *   * Current captured value
 *   * Line number
 *   * Line position
 *   * Variables
 */
public final class ScanContext {
    private final Map<String, Object> variables = new HashMap<>();
    private String capturedValue = "";
    private int lineNumber = 1;

    /**
     * Returns the current line number. It is up to the
     * consumer of the context to determine when and to perform
     * the incrementing of the line number.
     * @return Current line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Increments the line number.
     */
    public void incrementLineNumber() {
        lineNumber++;
    }

    /**
     * Increments the line number and returns a blank ignored token.
     * @return Default ignored token.
     */
    public IgnoredToken incrementLineNumberAndIgnore() {
        incrementLineNumber();
        return ignore();
    }

    /**
     * @return Default ignored token.
     */
    public IgnoredToken ignore() {
        return new IgnoredToken();
    }

    /**
     * Increments the line number and returns the token passed in.
     * @param returnToken Token to be returned after incrementing.
     * @return Token.
     */
    public Token incrementLineNumberAndReturn(Token returnToken) {
        incrementLineNumber();
        return returnToken;
    }

    /**
     * Returns the captured value. This can then be used to create
     * tokens.
     * @return Returns the value captured in the lex definition.
     */
    public String getCapturedValue() {
        return capturedValue;
    }

    /**
     * Internal method used to capture values.
     * @param capturedValue Value that was captured.
     */
    void capture(String capturedValue) {
        this.capturedValue = capturedValue;
    }

    /**
     * @return Mutable map meant to communicate between definitions.
     */
    public Map<String, Object> getVariables() {
        return variables;
    }
}
