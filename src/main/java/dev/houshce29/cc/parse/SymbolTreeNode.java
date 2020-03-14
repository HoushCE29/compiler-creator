package dev.houshce29.cc.parse;

import dev.houshce29.cc.common.IdentifiableGrammarComponent;
import dev.houshce29.cc.lex.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * A single, non-terminating node within the SymbolTree.
 */
public class SymbolTreeNode implements IdentifiableGrammarComponent {
    private final String id;
    private final List<IdentifiableGrammarComponent> children = new ArrayList<>();

    /**
     * Creates a new instance.
     * @param id ID of this node.
     */
    public SymbolTreeNode(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * @return The mutable list of children for this node.
     *         From the Parser, these will either be other nodes
     *         or tokens.
     */
    public List<IdentifiableGrammarComponent> getChildren() {
        return children;
    }

    /**
     * @return The total number of tokens, recursively, are present from this
     *         node down.
     */
    public int tokenCount() {
        int count = 0;
        for (IdentifiableGrammarComponent component : getChildren()) {
            if (component instanceof SymbolTreeNode) {
                count += ((SymbolTreeNode) component).tokenCount();
            }
            else if (component instanceof Token) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return toString("| ");
    }

    private String toString(String treeDepthPadding) {
        String childPadding = treeDepthPadding + "- ";
        StringBuilder builder = new StringBuilder(treeDepthPadding)
                .append(getId());
        for (IdentifiableGrammarComponent component : getChildren()) {
            builder.append("\n");
            if (component instanceof SymbolTreeNode) {
                builder.append(((SymbolTreeNode) component).toString(childPadding));
            }
            else {
                builder.append(childPadding)
                        .append(component instanceof Token ? component.toString() : component.getId());
            }
        }
        return builder.toString();
    }
}
