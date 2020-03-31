package dev.houshce29.cc.support.json.compiler;

import dev.houshce29.cc.analyze.SemanticAnalyzer;
import dev.houshce29.cc.common.GrammarComponent;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;
import dev.houshce29.cc.parse.SymbolTreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Analyzes json.
 */
public class JsonSemanticAnalyzer implements SemanticAnalyzer {

    @Override
    public void analyze(List<Token> tokens, SymbolTree symbolTree) {
        Set<String> duplicateKeys = new HashSet<>();
        analyzeObject(symbolTree.getRoot(), duplicateKeys, "OBJECT");
        if (!duplicateKeys.isEmpty()) {
            throw new IllegalArgumentException("Duplicate key(s): " + duplicateKeys);
        }
    }

    private void analyzeObject(SymbolTreeNode object, Set<String> duplicates, String path) {
        if (object.contains("BODY")) {
            inspectBody(object.getNode(1), new HashSet<>(), duplicates, path);
        }
    }

    private void analyzeValue(SymbolTreeNode value, Set<String> duplicates, String path) {
        if (JsonParserFactory.isJsonObject(value.get(0))) {
            analyzeObject(value.getNode(0), duplicates, path);
        }
        else if (value.contains("VALUE")) {
            analyzeValue(value.getNode(1), duplicates, path + "[" + 0 + "]");
            analyzeArray(value.getNode(2), duplicates, path, 1);
        }
        // Anything else, nothing else to do
    }

    private void analyzeArray(SymbolTreeNode arrayEnd, Set<String> duplicates, String path, int index) {
        // Only care if there's a value
        if (arrayEnd.contains("VALUE")) {
            analyzeValue(arrayEnd.getNode(1), duplicates, path + "[" + index + "]");
            analyzeArray(arrayEnd.getNode(2), duplicates, path, index + 1);
        }
    }

    private void inspectBody(SymbolTreeNode body, Set<String> scanned, Set<String> duplicates, String path) {
        String currentPath = path + "." + body.getToken(0).getValue();
        // Try to add. If already added, this is duplicate.
        if (!scanned.add(currentPath)) {
            duplicates.add(currentPath);
        }
        // Looks silly, but this is how multiple body properties are added.
        if (body.contains("BODY")) {
            // Send in body node, current scans, duplicates, and the initial path
            inspectBody(body.getNode(4), scanned, duplicates, path);
        }
        // Send in the value for further analysis
        analyzeValue(body.getNode(2), duplicates, currentPath);
    }
}
