package dev.houshce29.cc.parse;

import dev.houshce29.cc.common.GrammarComponent;
import dev.houshce29.cc.lex.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Abstract symbol tree containing parsed syntax.
 */
public class SymbolTree {
    private final SymbolTreeNode root;

    /**
     * Creates a new instance off of the given node as the root.
     * @param root Root of the tree.
     */
    public SymbolTree(SymbolTreeNode root) {
        this.root = root;
    }

    /**
     * @return The root of the tree for traversal.
     */
    public SymbolTreeNode getRoot() {
        return root;
    }

    /**
     * Finds all nodes within the tree of the given ID.
     * @param id ID to search for.
     * @return List of nodes of the given ID.
     */
    public List<SymbolTreeNode> findNodes(String id) {
        return find(id, SymbolTreeNode.class);
    }

    /**
     * Finds all tokens within the tree of the given ID.
     * @param id ID to search for.
     * @return List of tokens of the given ID.
     */
    public List<Token> findTokens(String id) {
        return find(id, Token.class);
    }

    /**
     * Flattens this tree into an ordered list. Note that
     * hierarchical elements (e.g. parent-child relationships)
     * will be inline, such that:
     *
     * <pre>{@code
     *     ROOT
     *       SUM
     *         LITERAL:1
     *         PLUS:+
     *         LITERAL:2
     *       DIFF
     *         LITERAL:3
     *         MINUS:-
     *         LITERAL:1
     * }</pre>
     *
     * would become:
     *
     * <pre>{@code
     *   [ROOT, SUM, LITERAL:1, PLUS:+, LITERAL:2, DIFF, LITERAL:3, MINUS:-, LITERAL:1]
     * }</pre>
     * @return
     */
    public List<GrammarComponent> flatten() {
        return flatten(root);
    }

    /**
     * Flattens this tree into a stringified, ordered list of grammar components.
     * @return Flattened tree, with components as strings.
     */
    public List<String> flattenAsString() {
        List<String> asString = new ArrayList<>();
        for (GrammarComponent component : flatten()) {
            StringBuilder builder = new StringBuilder(component.getId());
            if (component instanceof Token) {
                builder.append(":")
                        .append(((Token) component).getValue());
            }
            asString.add(builder.toString());
        }
        return asString;
    }

    /**
     * Recursively flattens a node's structure.
     * @param node Node to flatten.
     * @return Ordered list of grammar components.
     */
    private List<GrammarComponent> flatten(SymbolTreeNode node) {
        List<GrammarComponent> flat = new ArrayList<>();
        flat.add(node);
        for (GrammarComponent component : node.getChildren()) {
            if (component instanceof SymbolTreeNode) {
                flat.addAll(flatten((SymbolTreeNode) component));
            }
            else {
                flat.add(component);
            }
        }
        return flat;
    }

    /**
     * Finds all components within the tree of the given type and ID.
     * @param id ID to search for.
     * @param type Type of component to return.
     * @param <T> Type of component to return.
     * @return List of components of the given type and ID.
     */
    public <T extends GrammarComponent> List<T> find(String id, Class<T> type) {
        return findIn(root, id, type);
    }

    /**
     * Selects the grammar component at the given path.
     * @param path Path to select by.
     * @return Optional maybe containing the target grammar component.
     */
    public Optional<GrammarComponent> select(String... path) {
        return selectIn(root, Arrays.asList(path));
    }

    @Override
    public String toString() {
        return "AST:\n" + getRoot().toString();
    }

    /**
     * Internal recursive method to find all matching components within a node and downward.
     * @param node Node to search in (including its descendants).
     * @param id ID to search for.
     * @param type Type of component to return.
     * @param <T> Type of component to return.
     * @return List of components of the given type and ID within the node.
     */
    private <T extends GrammarComponent> List<T> findIn(SymbolTreeNode node, String id, Class<T> type) {
        List<T> found = new ArrayList<>();
        for (GrammarComponent component : node.getChildren()) {
            // Add if type and ID are equal
            if (type.isInstance(component) && id.equals(component.getId())) {
                found.add(type.cast(component));
            }
            // If a node, examine further down the tree as well and add all
            if (component instanceof SymbolTreeNode) {
                found.addAll(findIn((SymbolTreeNode) component, id, type));
            }
        }
        return found;
    }

    /**
     * Internal recursive method to select a grammar component following the path.
     * @param node Starting node/subtree.
     * @param path Path to target a grammar component.
     * @return Optional maybe containing the target grammar component.
     */
    private Optional<GrammarComponent> selectIn(SymbolTreeNode node, List<String> path) {
        // Sanity check: empty list
        if (path.isEmpty()) {
            return Optional.empty();
        }
        for (GrammarComponent component : node.getChildren()) {
            // Strictly follow path
            if (component.getId().equals(path.get(0))) {
                // If this is the target component, return it.
                if (path.size() == 1) {
                    return Optional.of(component);
                }
                // If a node, drill continue to drill down
                if (component instanceof SymbolTreeNode) {
                    return selectIn((SymbolTreeNode) component, path.subList(1, path.size()));
                }
                // Otherwise, we've hit a snag; return empty.
                else {
                    return Optional.empty();
                }
            }
        }
        // Return empty if no matching component is found.
        return Optional.empty();
    }
}
