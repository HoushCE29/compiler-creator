package dev.houshce29.cc.support.json.compiler;

import dev.houshce29.cc.Compiler;
import dev.houshce29.cc.support.json.model.Json;

/**
 * Compiler for JSON.
 */
public final class JsonCompiler {
    private final Compiler jsonCompiler = Compiler.newBuilder()
            .setLexer(JsonLexerFactory.get())
            .setParser(JsonParserFactory.get())
            .setSemanticAnalyzer(new JsonSemanticAnalyzer())
            .setGenerator(new JsonModelGenerator())
            .build();

    public Json compile(String input) {
        return (Json) jsonCompiler.compile(input);
    }
}
