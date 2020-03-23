package dev.houshce29.cc.lex;

/**
 * Strategy for matching input. There are various ways to
 * ensure that input is completely consumed for matching:
 * <br />
 * <ul>
 *     <li>
 *         Greedy - As soon as a match is found, immediately consume it.
 *                  This is the most efficient method.
 *     </li>
 *     <li>
 *         Span - While input scan is matching, wait to consume it until
 *                a character scanned makes the input not match anymore.
 *                Consumption will be of the last span matching.
 *                This is a lesser efficient method that greedily scanning.
 *     </li>
 *     <li>
 *         Max - Scan to the end of the input and consume the last matching
 *               character sequence. This is the least efficient method.
 *     </li>
 * </ul>
 *
 */
public enum MatchingStrategy {

    /**
     * Default strategy. This strategy will tell the lexer to consume
     * the first-matching input sequence.
     * <br />
     * For example, if the regex for producing tokens is
     * <pre>a+</pre>
     * and the raw input is
     * <pre>aaa</pre>
     * then the result would be 3 consecutive tokens with the value of 'a'. This
     * is because the lexer reads in a character at a time, and immediately matches,
     * thus resulting in 3 tokens.
     */
    GREEDY,

    /**
     * Strategy that matches a span. This strategy will defer telling the
     * lexer to consume anything until the running input sequence no longer
     * matches, at which the last match will be then consumer by the lexer.
     * <br />
     * For example, if the regex for producing tokens is
     * <pre>a+</pre>
     * and the raw input is
     * <pre>aaaba</pre>
     * then the result would be 1 token with the value of 'aaa', and whatever
     * consumes the rest of the input. This grabs the immediate span of matching
     * input and consumes it.
     */
    SPAN,

    /**
     * Strategy that matches as much as possible. This strategy will literally
     * have the lexer scan to the end of the input and tell the lexer to consume
     * the last matching input sequence.
     * <br />
     * For example, if the regex for producing tokens is
     * <pre>a.+a</pre>
     * and the raw input is
     * <pre>abbabbax</pre>
     * then the result would be 1 token with the value of 'abbabba', and whatever
     * consumes the rest of the input. This doesn't just grab the immediate span
     * of matching input, but rather will attempt to stretch the sequence to the
     * maximum-sized match.
     */
    MAX;
}
