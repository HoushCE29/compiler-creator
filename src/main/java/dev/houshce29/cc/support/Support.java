package dev.houshce29.cc.support;

import dev.houshce29.cc.Compiler;

/**
 * Provides support for creating compilers from some file.
 */
public interface Support {

    /**
     * Creates a compiler defined at the given file.
     * @param filePath Path to the file.
     * @return New compiler instance.
     */
    Compiler get(String filePath);
}
