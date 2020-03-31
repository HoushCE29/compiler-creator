package dev.houshce29.cc.lex;

/**
 * A helper factory for use in defining a {@link Lexer}.
 */
public class RegexFactory {
    private static final String ANY_WHITESPACE_REGEX = "\\s";
    private static final String ANY_LENGTH_WHITESPACE_REGEX = ANY_WHITESPACE_REGEX + "*";
    private static final String LINE_SEPARATOR_REGEX = "\\R";
    private static final String ANYTHING_REGEX = ".*";
    private static final String ANY_NUMBER_REGEX = "-?[0-9]+|-?[0-9]*\\.[0-9]+";
    private static final String ANY_BOOLEAN_REGEX = "true|false";
    private static final String ANY_STRING_LITERAL_REGEX = "\\\"([^\\\\\"]+|\\\\.|\\\\\\\\)*\\\"";

    /**
     * Prevent instantiation.
     */
    private RegexFactory() {
    }

    /**
     * Pads the input regex with the "any-amount-of-whitespace" regex on both sides.
     * @param regex Regex to pad with any-amount-of-whitespace regex.
     * @return Newly generated regex to account for zero or more whitespaces on either side.
     */
    public static String padWithAnyAmountWhitespace(String regex) {
        return ANY_LENGTH_WHITESPACE_REGEX + regex + ANY_LENGTH_WHITESPACE_REGEX;
    }

    /**
     * @return The line separator regex.
     */
    public static String lineSeparatorRegex() {
        return LINE_SEPARATOR_REGEX;
    }

    /**
     * @return The singular any whitespace regex.
     */
    public static String anyWhitespaceRegex() {
        return ANY_WHITESPACE_REGEX;
    }

    /**
     * @return The any-amount-of-whitespace regex (to account for 0 or more whitespaces).
     */
    public static String anyAmountWhitespaceRegex() {
        return ANY_LENGTH_WHITESPACE_REGEX;
    }

    /**
     * @return Regex for any string literal between DOUBLE quotes.
     */
    public static String stringLiteralRegex() {
        return ANY_STRING_LITERAL_REGEX;
    }

    /**
     * @return Regex for any number literal (negative, integers, floating point, etc.).
     */
    public static String numberLiteralRegex() {
        return ANY_NUMBER_REGEX;
    }

    /**
     * @return Boolean literals (true or false).
     */
    public static String booleanLiteralRegex() {
        return ANY_BOOLEAN_REGEX;
    }

    /**
     * @return The anything regex.
     */
    public static String anythingRegex() {
        return ANYTHING_REGEX;
    }
}
