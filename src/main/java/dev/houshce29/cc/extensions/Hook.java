package dev.houshce29.cc.extensions;

/**
 * Simple interface for writing extensions.
 */
public interface Hook {

    /**
     * Runs custom logic.
     * @param objects Objects pushed in by standard compiler process.
     */
    void execute(Object... objects);

    /**
     * Tells the compiler which point in the compilation process to run.
     * @return Hook point to run at.
     */
    HookPoint getHookPoint();

    /**
     * Tells the compiler, for some hook point, the order in which
     * to run this hook.
     * @return The sequence to run this hook in.
     */
    default int getSequence() {
        return 0;
    }
}
