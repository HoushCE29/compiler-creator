# Compiler Creator
A simple Java framework for creating compilers.

## Building
To build this project, simply run:
```
gradlew clean build
```

The resulting `jar` is generated to:
```
/build/libs/compiler-creator.jar
```

## Components
  * [`Lexer`](src/main/java/dev/houshce29/cc/lex/Lexer.java) - First component of the compiler that generates tokens based on regex matching while scanning input.
  * [`Parser`](src/main/java/dev/houshce29/cc/parse/Parser.java) - Second component of the compiler that scans for syntactic correctness while constructing a tree to glue the tokens and grammar together in a logical format.
  * [`SemanticAnalyzer`](src/main/java/dev/houshce29/cc/analyze/SemanticAnalyzer.java) - Third component of the compiler that scans for semantic correctness.
  * [`Generator`](src/main/java/dev/houshce29/cc/generate/Generator.java) - Final component of the compiler that generates something from the compilation artifacts (tokens and tree) which can either be in the form of some Java object or simply written out as code to the file system.

## Lexer
This component is responsible for the initial logical sorting of raw input in the form of a list of [tokens](src/main/java/dev/houshce29/cc/lex/SimpleToken.java).
A given token has 3 primary properties:
 1. ID - Defines the _type_ of token.
 2. Value - Either a captured or hard-coded value.
 3. Line Number - Line on which the token began on. This is mostly for error reporting purposes.
 
The lexer itself is quite simple to define. For each definition, there is a regex and a factory function to either produce a token or raise an error. Each definition is used in the order in which it is defined in with respect to other definitions, and will be used until the end of the input is reached and no match is found.

The lexer internally will traverse the input character by character until a match is made against some regex and its respective [matching strategy](src/main/java/dev/houshce29/cc/lex/MatchingStrategy.java). The matching strategies are as defined:
  * `GREEDY` - The default strategy, will consume the scanned input upon matching the regex instantly. This is a typical strategy to use for keywords and literal characters.
    - Example regex's: `while`, `for`, `true`
  * `SPAN` - Matches a span. Once the first match against the regex is found, this will tell the lexer to keep scanning until the scanned input no longer matches. This is useful for right-ambiguous regex's, such as number literals.
    - Example regex: `[0-9]+`
  * `MAX` - Matches the largest scanned input possible. This is the least efficient strategy, but may be required in certain situations. This tells the lexer to scan until the end of the input and tracks the most recent match. Once input is fully traversed, the most recent match gets consumed. This is useful for middle-ambiguous regex's.
    - Example regex: `\\{\\{.+\\}\\}`

Use the lexer builder to build a lexer.
```java
Lexer myLexer = Lexer.newBuilder()
    .on(RegexFactory.lineSeparatorRegex())
        .incrementLineNumber()
    .ignore(RegexFactory.anyAmountWhitespaceRegex())
    .on("my first regex")
        .generate("TOKEN_1")
    .on("my explicitly greedy regex", MatchingStrategy.GREEDY)
        .generate("TOKEN_2")
    .on("my spanning regex [ya]+", MatchingStrategy.SPAN)
        .generate("TOKEN_3")
    .on("my max-reach \\.+ regex", MatchingStrategy.MAX)
        .generate("TOKEN_4")
    .on("my custom logic token creation regex")
        .create(context -> new MyCustomToken(context.getCapturedValue(), context.getLineNumber()))
    .on("my default error regex")
        .error()
    .on("my custom error regex")
        .error(context -> new IllegalArgumentException("Failed at: " + context.getCapturedValue() + " on line " + context.getLineNumber()))
    .after(tokens -> myCustomLogicService::postLexerHookLogic)
    .build();
```

## Parser
This component enforces syntax and organizes the tokens into a symbol tree.
In terms of the compiler-creator library, this component uses unorthodox terminology and definitions with regards to defining grammar:
  * [`Grammar`](src/main/java/dev/houshce29/cc/parse/Grammar.java) - A collection of **phrases**, with one phrase declared as the **root**.
    - `root` - The **phrase** to start at when traversing the token list to begin producing the symbol tree.
  * [`Phrase`](src/main/java/dev/houshce29/cc/parse/Phrase.java) - An identifiable entity that contains a collection of **sentences**. Each **sentence** is visited in order during parsing.
    - `phraseId` - The unique identifier of the phrase. In fact, if this id is the same as some token's, the token will be ignored in favor of the phrase.
  * `sentence` - An ordered list of **words**.
  * `word` - A string that is either a phrase ID or token ID.
  
**IMPORTANT**: This parser does NOT allow left-ambiguity. If the grammar has left-ambiguity in it, the parser will fail.

Use the parser builder to build a parser.

```java
/*
  GRAMMAR ::= PROGRAM

  PROGRAM ::= TOKEN_1 NODE_1 TOKEN_2
            | TOKEN_1 TOKEN_2

  NODE_1  ::= TOKEN_3 TOKEN_4 NODE_2
            | TOKEN_4
  
  NODE_2  ::= TOKEN_3
            | TOKEN_4
            | NODE_1 TOKEN_1 
 */
Parser myParser = Parser.newBuilder("PROGRAM")
        .sentence("TOKEN_1", "NODE_1", "TOKEN_2")
        .sentence("TOKEN_1", "TOKEN_2")
    .branch("NODE_1")
        .sentence("TOKEN_3", "TOKEN_4", "NODE_2")
        .sentence("TOKEN_4")
    .branch("NODE_2")
        .sentence("TOKEN_3")
        .sentence("TOKEN_4")
        .sentence("NODE_1", "TOKEN_1")
    .build();
```

Or use the grammar builder to inject into the parser:
```java
Grammar myGrammar = Grammar.from(Phrase.newBuilder("PROGRAM")
        .addSentence("TOKEN_1", "NODE_1", "TOKEN_2")
        .addSentence("TOKEN_1", "TOKEN_2")
        .build())
    .addPhrase(Phrase.newBuilder("NODE_1")
        .addSentence("TOKEN_3", "TOKEN_4", "NODE_2")
        .addSentence("TOKEN_4")
        .build())
    .addPhrase(Phrase.newBuilder("NODE_2")
        .addSentence("TOKEN_3")
        .addSentence("TOKEN_4")
        .addSentence("NODE_1", "TOKEN_1")
        .build())
    .build();
Parser myParser = Parser.of(myGrammar);
```

## Semantic Analyzer
This component is a more open-ended implementation, contrary to the lexer and parser.
This is meant to analyze the symbol tree for semantic correctness, such as the existence of a main function or type correctness.

Implement the [semantic analyzer interface](src/main/java/dev/houshce29/cc/analyze/SemanticAnalyzer.java) to create a semantic analyzer.

## Generator
This component is the final phase that creates something.
This can either be some Java model or generated code written out to some file.

Implement the [generator interface](src/main/java/dev/houshce29/cc/generate/Generator.java) to create a generator that returns a Java model,
or implement the [code generator abstract class](src/main/java/dev/houshce29/cc/generate/CodeGenerator.java) to create an implementation that writes code out to a file.

## Compiler
This is the part that glues all these components together and is actionable.
Define and run the [compiler](src/main/java/dev/houshce29/cc/Compiler.java) as such:
```java
Compiler myCompiler = Compiler.newBuilder()
    .setLexer(myLexer)
    .setParser(myParser)
    .setSemanticAnalyzer(myAnalyzer)
    .setGenerator(myGenerator)
    .build();

 . . .

Object result = myCompiler.compile(someRawInput);
```

