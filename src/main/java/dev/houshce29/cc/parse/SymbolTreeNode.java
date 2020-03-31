package dev.houshce29.cc.parse;

import dev.houshce29.cc.common.GrammarComponent;
import dev.houshce29.cc.lex.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * A single, non-terminating node within the SymbolTree.
 */
public class SymbolTreeNode implements GrammarComponent {
    private final String id;
    private final List<GrammarComponent> children = new ArrayList<>();

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
    public List<GrammarComponent> getChildren() {
        return children;
    }

    /**
     * Returns the child component at the given index.
     * @param index Index of child.
     * @return Child component.
     */
    public GrammarComponent get(int index) {
        return children.get(index);
    }

    /**
     * Returns the child node at the given index.
     * If this child is not a tree node, an exception
     * will be thrown.
     * @param index Index of the child.
     * @return Tree node at index.
     */
    public SymbolTreeNode getNode(int index) {
        return cast(get(index));
    }
    /**
     * Returns the child token at the given index.
     * If this child is not a token, an exception
     * will be thrown.
     * @param index Index of the child.
     * @return Token at index.
     */

    public Token getToken(int index) {
        return Token.cast(get(index));
    }

    /**
     * @return The total number of tokens, recursively, are present from this
     *         node down.
     */
    public int tokenCount() {
        int count = 0;
        for (GrammarComponent component : getChildren()) {
            if (component instanceof SymbolTreeNode) {
                count += ((SymbolTreeNode) component).tokenCount();
            }
            else if (component instanceof Token) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns true if the passed in sentence describes
     * this node's children. The sentence should be the IDs
     * of the children, as defined in the grammar.
     * @param sentence Sentence to determine if this is of.
     * @return `true` if the sentence describes the children of
     *         this node.
     */
    public boolean is(String... sentence) {
        if (sentence.length != children.size()) {
            return false;
        }
        for (int i = 0; i < sentence.length; i++) {
            // No match.
            if (!sentence[i].equals(children.get(i).getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the index of the given word. If not found,
     * -1 is returned instead.
     * @param word Word to search for in children.
     * @return Index of word, or -1.
     */
    public int indexOf(String word) {
        for (int i = 0; i < children.size(); i++) {
            if (word.equals(get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns whether or not this node contains the
     * given word at least once.
     * @param word word to search for in children.
     * @return Whether or not this node contains the word.
     */
    public boolean contains(String word) {
        return indexOf(word) > -1;
    }

    /**
     * @return The size of this node (e.g. number of children components).
     */
    public int size() {
        return children.size();
    }

    @Override
    public String toString() {
        return toString("| ");
    }

    /**
     * Casts the component into a symbol tree node if possible.
     * This is a useful utility method for semantic analysis and
     * code/model generation, where the grammar most likely ensured
     * that the correct component was created, but of course, there's
     * always the slight chance that something went wrong :).
     * @param component Component to cast to a node.
     * @return Symbol tree node.
     * @throws IllegalArgumentException if this component cannot be casted to a tree node.
     */
    public static SymbolTreeNode cast(GrammarComponent component) throws IllegalArgumentException {
        if (component instanceof SymbolTreeNode) {
            return (SymbolTreeNode) component;
        }
        throw new IllegalArgumentException(component.getId() + " was expected to be a tree node.");
    }

    private String toString(String treeDepthPadding) {
        String childPadding = treeDepthPadding + "- ";
        StringBuilder builder = new StringBuilder(treeDepthPadding)
                .append(getId());
        for (GrammarComponent component : getChildren()) {
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
