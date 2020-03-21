package dev.houshce29.cc.extensions;

/**
 * Abstraction that handles the boilerplate features of a hook.
 */
public abstract class AbstractHook implements Hook {
    private final HookPoint hookPoint;
    private final int sequence;

    /**
     * Creates a new instance at the given hook point
     * with no regards to a sequence.
     * @param hookPoint Hook point to run hook.
     */
    public AbstractHook(HookPoint hookPoint) {
        this(hookPoint, 0);
    }

    /**
     * Creates a new instance at the given hook point
     * and will be executed at the given sequence in order.
     * @param hookPoint Hook point to run hook.
     * @param sequence Order to run this hook in at this hook point
     *                 with respect to other hooks.
     */
    public AbstractHook(HookPoint hookPoint, int sequence) {
        this.hookPoint = hookPoint;
        this.sequence = Math.max(sequence, 0);
    }

    @Override
    public final HookPoint getHookPoint() {
        return this.hookPoint;
    }

    @Override
    public int getSequence() {
        return this.sequence;
    }
}
