package dev.houshce29.cc.common;

/**
 * Exception thrown upon failed compilation.
 */
public class CompilationException extends RuntimeException {
    public CompilationException(Throwable cause) {
        super("Compilation failed.", cause);
    }
}
