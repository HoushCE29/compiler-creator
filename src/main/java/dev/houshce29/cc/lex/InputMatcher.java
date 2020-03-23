package dev.houshce29.cc.lex;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Internal wrapper that helps control the lexer.
 */
final class InputMatcher {
    private final Pattern pattern;
    private final MatchingStrategy strategy;

    InputMatcher(String regex, MatchingStrategy strategy) {
        this.pattern = Pattern.compile(regex);
        this.strategy = strategy;
    }

    /**
     * Determines if the input matches.
     * @param in Input to determine if matches.
     * @return `true` if the input matches.
     */
    public boolean matches(String in) {
        return pattern.matcher(in).matches();
    }

    /**
     * Strategy to determine when to consume
     * a matching input.
     * @return Matching strategy.
     */
    public MatchingStrategy getStrategy() {
        return strategy;
    }

    /**
     * Returns the pattern for this matcher.
     * @return Pattern for this matcher.
     */
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return pattern.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(strategy, pattern);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InputMatcher)) {
            return false;
        }
        InputMatcher other = (InputMatcher) obj;
        return Objects.equals(pattern, other.getPattern())
                && Objects.equals(strategy, other.getStrategy());
    }
}
