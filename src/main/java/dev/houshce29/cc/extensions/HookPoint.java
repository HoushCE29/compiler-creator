package dev.houshce29.cc.extensions;

/**
 * Specifies when a hook runs.
 */
public enum HookPoint {
    /**
     * Run upon compilation failure.
     */
    ON_FAILURE(-1),

    /**
     * Run before everything.
     */
    BEFORE_COMPILE(0),

    /**
     * Run after BEFORE_COMPILE hooks, but just before lexer.
     */
    BEFORE_LEX(1),

    /**
     * Run after lexer.
     */
    AFTER_LEX(2),

    /**
     * Run after AFTER_LEX hooks, but just before parser.
     */
    BEFORE_PARSE(3),

    /**
     * Run after parser.
     */
    AFTER_PARSE(4),

    /**
     * Run after AFTER_PARSE hooks, but just before semantic analyzer.
     */
    BEFORE_SEMANTIC_ANALYSIS(5),

    /**
     * Run after semantic analyzer.
     */
    AFTER_SEMANTIC_ANALYSIS(6),

    /**
     * Run after AFTER_SEMANTIC_ANALYZER hooks, but
     * just before code generator.
     */
    BEFORE_CODE_GENERATION(7),

    /**
     * Run after code generator.
     */
    AFTER_CODE_GENERATION(8),

    /**
     * Run after all compiler components and hooks.
     */
    AFTER_COMPILE(9);

    private final int sequenceNumber;

    HookPoint(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return Sequence/order of the hook point.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
